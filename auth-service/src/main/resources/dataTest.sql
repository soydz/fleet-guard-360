-- Habilitar pgcrypto para usar crypt/gen_salt (requerido para BCrypt)
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Limpieza básica para evitar conflictos y duplicados en relaciones
DELETE FROM public.role_permission;
DELETE FROM public.user_role;
DELETE FROM public.app_user;

-- -------------------------------
-- 1. Permisos (evitar duplicados)
-- -------------------------------
INSERT INTO public."permission" (permission_name)
SELECT v.permission_name
FROM (VALUES
    ('CREATE'),
    ('READ'),
    ('UPDATE'),
    ('DELETE')
) AS v(permission_name)
WHERE NOT EXISTS (
    SELECT 1 FROM public."permission" p WHERE p.permission_name = v.permission_name
);

-- -------------------------------
-- 2. Roles (evitar duplicados)
-- -------------------------------
INSERT INTO public."role" (role_name)
SELECT v.role_name
FROM (VALUES
    ('CONDUCTOR'),
    ('ADMINISTRADOR'),
    ('OPERADOR')
) AS v(role_name)
WHERE NOT EXISTS (
    SELECT 1 FROM public."role" r WHERE r.role_name = v.role_name
);

-- -------------------------------
-- 3. Tipos de identificación (se guarda como ORDINAL)
-- -------------------------------
-- Asegura que existan exactamente los 6 valores (0..5)
INSERT INTO public.type_of_id (user_id_type)
SELECT v.user_id_type
FROM (VALUES (0),(1),(2),(3),(4),(5)) AS v(user_id_type)
WHERE NOT EXISTS (
    SELECT 1 FROM public.type_of_id t WHERE t.user_id_type = v.user_id_type
);

-- -------------------------------
-- 4. Usuarios de prueba (columnas alineadas con la entidad User)
--    disable: FALSE (usuario habilitado)
--    account_expired/account_locked/credential_expired: FALSE (no expirado/no bloqueado)
--    type_of_id: FK por subconsulta al id de type_of_id según ordinal
--    password: BCrypt usando pgcrypto (crypt + gen_salt('bf'))
-- -------------------------------
INSERT INTO public.app_user
(id, name, lastname, email, password, disable, account_expired, account_locked, credential_expired, type_of_id)
VALUES
(1, 'Lucas', 'Ramirez', 'lucas.ramirez@example.com', crypt('lucas', gen_salt('bf', 10)), FALSE, FALSE, FALSE, FALSE, (
    SELECT id FROM public.type_of_id WHERE user_id_type = 0 ORDER BY id ASC LIMIT 1
)),
(2, 'Maria', 'Lopez', 'maria.lopez@example.com', crypt('maria', gen_salt('bf', 10)), FALSE, FALSE, FALSE, FALSE, (
    SELECT id FROM public.type_of_id WHERE user_id_type = 1 ORDER BY id ASC LIMIT 1
)),
(3, 'Juan', 'Perez', 'juan.perez@example.com', crypt('juan', gen_salt('bf', 10)), FALSE, FALSE, FALSE, FALSE, (
    SELECT id FROM public.type_of_id WHERE user_id_type = 2 ORDER BY id ASC LIMIT 1
));

-- -------------------------------
-- 5. Asignar roles a usuarios (por nombres, sin asumir IDs)
-- -------------------------------
-- Lucas -> ADMINISTRADOR
INSERT INTO public.user_role (user_id, role_id)
SELECT u.id, r.id
FROM public.app_user u, public."role" r
WHERE u.email = 'lucas.ramirez@example.com' AND r.role_name = 'ADMINISTRADOR';

-- Maria -> CONDUCTOR
INSERT INTO public.user_role (user_id, role_id)
SELECT u.id, r.id
FROM public.app_user u, public."role" r
WHERE u.email = 'maria.lopez@example.com' AND r.role_name = 'CONDUCTOR';

-- Juan -> OPERADOR
INSERT INTO public.user_role (user_id, role_id)
SELECT u.id, r.id
FROM public.app_user u, public."role" r
WHERE u.email = 'juan.perez@example.com' AND r.role_name = 'OPERADOR';

-- -------------------------------
-- 6. Asignar permisos a roles (por nombres)
-- -------------------------------
-- ADMINISTRADOR: todos los permisos
INSERT INTO public.role_permission (role_id, permission_id)
SELECT r.id, p.id
FROM public."role" r, public."permission" p
WHERE r.role_name = 'ADMINISTRADOR';

-- CONDUCTOR: solo READ
INSERT INTO public.role_permission (role_id, permission_id)
SELECT r.id, p.id
FROM public."role" r, public."permission" p
WHERE r.role_name = 'CONDUCTOR'
  AND p.permission_name IN ('READ');

-- OPERADOR: solo READ y UPDATE
INSERT INTO public.role_permission (role_id, permission_id)
SELECT r.id, p.id
FROM public."role" r, public."permission" p
WHERE r.role_name = 'OPERADOR'
  AND p.permission_name IN ('READ','UPDATE');