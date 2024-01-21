-- Message
alter table public.message enable row level security;
create policy "Message only viewable by themself" on public.message for all to authenticated using (custom_auth.user_id() = fk_patient_id or custom_auth.user_id() = fk_doctor_id);