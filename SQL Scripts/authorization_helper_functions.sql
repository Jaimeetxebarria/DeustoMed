create or replace function custom_auth.user_id() returns uuid as $$
  select nullif(current_setting('custom_auth.user_id', true), '')::text;
$$ language sql stable;

create or replace function custom_auth.user_type() returns text as $$
  select nullif(current_setting('custom_auth.user_type', true), '')::text;
$$ language sql stable;