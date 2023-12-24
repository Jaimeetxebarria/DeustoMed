--===================== CUSTOM AUTH =====================
create schema if not exists custom_auth;

drop table if exists custom_auth.session cascade;
create table
  custom_auth.session (
    id uuid primary key default gen_random_uuid (),
    user_id char(5) not null references person,
    fk_user_type smallint references user_type not null,
    access_token uuid not null,
    access_token_expiry timestamptz default current_timestamp + interval '5 minutes',
    session_expiry timestamptz default date_trunc('minute', current_timestamp + interval '6 hours')
  );

-- Start expired session deletion event
SELECT cron.schedule('delete expired sessions', '*/1 * * * *', 'delete from custom_auth.session where session_expiry <= current_timestamp');

drop table if exists custom_auth.refresh_token cascade;
create table
  custom_auth.refresh_token (
    id bigint generated by default as identity primary key,
    token uuid not null,
    revoked boolean default false,
    fk_session_id uuid references custom_auth.session on delete cascade
  );

-- Used with RPC to update the session token and reset the access token expiry using the default interval
create or replace function custom_auth.update_session_token(session_id uuid, new_access_token uuid)
returns timestamp with time zone as $$
begin
  update custom_auth.session set access_token = new_access_token, access_token_expiry = default where id = session_id;
  return (select access_token_expiry from custom_auth.session where id = session_id);
end;
$$ language plpgsql;

create or replace function custom_auth.find_session (session_id uuid, _access_token uuid)
returns table (session_user_id char(5), session_user_type text, is_token_expired boolean) security definer as $$
  begin
    return query
    select s.user_id, (select name from user_type where id = s.fk_user_type), s.access_token_expiry <= current_timestamp
    from custom_auth.session s
    where s.id = session_id and s.access_token = _access_token
    limit 1;
  end;
$$ language plpgsql;

create or replace function custom_auth.authenticate () returns void as $$
  declare
    session_id uuid;
    access_token uuid;
    session_user_id text;
    session_user_type text;
    is_access_token_expired boolean;
  begin
    select current_setting('request.cookies', true)::json->>'sessionId' into session_id;
    select current_setting('request.cookies', true)::json->>'accessToken' into access_token;

    -- Handle anonymous users and superusers
    if session_id is null then
        -- In a IRL situation use a configuration parameter. Here we can't because we aren't admin.
        if access_token = 'a77d4f49-be93-458b-8036-78c1ce6526bc'::uuid then
            set local role to service_role;
        else
            set local role to anon;
        end if;
        return;
    end if;

    select custom_auth.find_session(session_id, access_token) into session_user_id, session_user_type, is_access_token_expired;

    -- Handle session errors
    if session_user_id is null then
        raise exception 'Invalid session';
    end if;

    if is_access_token_expired then
        raise exception 'Expired access token';
    end if;

    -- Handle authenticated users
    if session_user_id is not null then
        set local role to authenticated;
        perform set_config('custom_auth.user_id', session_user_id, true);
  		perform set_config('custom_auth.user_type', session_user_type, true);
    end if;
  end;
$$ language plpgsql;

grant execute on function custom_auth.authenticate to anon;
grant usage on schema custom_auth to anon, service_role;
grant all on all tables in schema custom_auth to service_role;
alter default privileges for role postgres in schema custom_auth grant all on tables to service_role;

-- Set up the authentication trigger
alter role authenticator set pgrst.db_pre_request to 'custom_auth.authenticate';
notify pgrst, 'reload config';