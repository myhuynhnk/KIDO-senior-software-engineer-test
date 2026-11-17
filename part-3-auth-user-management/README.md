# Part 3 – Authentication & User Management

A small full-stack assessment project focused on **Java/Spring Boot backend**, JWT authentication, account management and basic Admin/User RBAC. Frontend UI is intentionally minimal.

## Features
- Login using username/password, signed JWT access token (15 minutes)
- View current account profile
- Change password (verify current password, confirm new password, BCrypt hashing)
- Admin-only user list, enforced by Spring Security
- Logout (client clears token)
- Standardized validation and business error responses

## Architecture

```text
React (localhost:5173)
   | REST/JSON + Bearer JWT
   v
Spring Boot (localhost:8080)
   | Spring Security -> Controller -> Service -> Repository
   v
PostgreSQL (localhost:5432)
```

Single backend application organized by feature: `authentication`, `user`, `security`, `common`. No Docker or deployment required.

## Stack
- Java **17** (also runnable on Java 21 with compatible dependencies)
- Spring Boot **3.3.13**, Spring Security, Spring Data JPA, PostgreSQL
- Spring Security OAuth2 Resource Server + Nimbus JOSE JWT (HS256)
- React 18, Vite 5, Bootstrap 5

## API
| Method | Path | Access | Description |
|---|---|---|---|
| POST | `/api/auth/login` | Public | Returns JWT |
| GET | `/api/users/me` | Authenticated | Profile |
| PUT | `/api/users/me/password` | Authenticated | Change password |
| GET | `/api/admin/users` | ADMIN | List users |

## Local setup (Windows PowerShell)

Prerequisites: JDK 17 or 21, Maven 3.9+, Node.js 20+, PostgreSQL 14+. No Docker needed.

1. Create an empty PostgreSQL database called `kido_auth` (e.g. `CREATE DATABASE kido_auth;`). Tables are created automatically on startup **for this local demo only**.
2. In PowerShell, set environment variables **in the same terminal where you run Maven**:

```powershell
$env:DB_URL = 'jdbc:postgresql://localhost:5432/kido_auth'
$env:DB_USER = 'postgres'
$env:DB_PASSWORD = 'your-local-db-password'
$env:JWT_SECRET = 'replace-with-your-own-random-secret-at-least-32-bytes-long'
$env:DEMO_ADMIN_PASSWORD = 'your-admin-demo-password'
$env:DEMO_USER_PASSWORD = 'your-user-demo-password'
cd part-3-auth-user-management/backend
mvn spring-boot:run
```

3. In another terminal:

```powershell
cd part-3-auth-user-management/frontend
npm install
npm run dev
```

4. Open http://localhost:5173. Login as `admin` or `user` using the passwords configured above. Demo accounts are seeded **only when absent**. If you change demo environment passwords later, existing database passwords will not change automatically.

If you are already in the `part-3-auth-user-management` directory, omit that prefix from the `cd` commands.

**Important:** Do not commit real DB passwords, JWT secrets or credentials. Set a random JWT secret (at least 32 UTF-8 bytes). The example values above are placeholders, not production secrets.

## Main flows
- Login: React -> `/api/auth/login` -> Spring AuthenticationManager -> BCrypt verification -> signed JWT -> React memory.
- Protected request: Bearer JWT -> signature/expiry/issuer + user/token-version validation -> controller -> service -> DB.
- Password change: validate request -> verify current password -> BCrypt hash new password -> update user and increment token version -> previous JWT becomes invalid -> frontend signs out.
- RBAC: USER receives 403 for `/api/admin/users`; ADMIN receives user list. Hiding Admin navigation is only a UI convenience.

## Test checklist (not yet verified)
- Valid and invalid login
- Unauthorized profile request -> 401
- USER access to Admin API -> 403
- ADMIN access -> 200
- Wrong current password -> 400
- Successful password change; old password fails; old JWT rejected
- Invalid input -> 400

## AI usage and custom skill
AI helped draft the implementation. Review process and evidence: [docs/AI-USAGE.md](docs/AI-USAGE.md).
Project-specific skill: [auth-security-review](.agents/skills/auth-security-review/SKILL.md). The skill must be **run and its results recorded** before claiming completion.

## Scope and trade-offs
- Demo only: no registration, forgot-password, refresh tokens, production deployment, Docker or CI/CD.
- JWT is held in React memory (refresh requires login). Client logout clears its copy; previously issued tokens remain valid until expiry unless password change increments token version.
- Token-version check reads the DB on protected requests: simple immediate invalidation, at the cost of a DB query.
- `ddl-auto: update` and demo account seeding are for local assessment convenience, not production migrations.
- Frontend is intentionally minimal; backend security and validation are the priority.
