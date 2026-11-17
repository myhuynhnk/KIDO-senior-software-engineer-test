---
name: auth-security-review
description: Review JWT authentication, password change, and RBAC in the KIDO assessment application.
---
# KIDO Auth Security Review

## Scope
Review the Java Spring Boot backend and React API calls for authentication and user-management security.

## Steps
1. Inspect login and JWT signing/verification. Check issuer, expiration, algorithm, and secret handling.
2. Check password hashing, verification of current password, and invalidation of previous JWTs after password change.
3. Verify `/api/admin/**` is protected server-side, not only hidden in React.
4. Check request validation, 400/401/403 responses, and accidental disclosure of hashes, secrets, or stack traces.
5. Identify missing unit/integration tests and reproduce risks with concrete test cases.
6. Never claim a test passed without running it. Do not modify files unless explicitly asked.

## Output
Report: severity, file, evidence, risk, recommended fix, and suggested test. Distinguish verified findings from hypotheses.
