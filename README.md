# Devconnect

Local development notes

Run the app (development):

PowerShell (set a session-only env var):

```powershell
$env:JWT_SECRET = '0123456789ABCDEF0123456789ABCDEF' # use at least 32 bytes (or Base64 32 bytes)
mvn -DskipTests package
java -jar target/devconnect-0.0.1-SNAPSHOT.jar
```

Or run with an explicit JVM property (one-shot):

```powershell
java -Djwt.secret=0123456789ABCDEF0123456789ABCDEF -jar target/devconnect-0.0.1-SNAPSHOT.jar
```

Notes:
- Prefer setting the secret via environment variables or a secret manager for production. Do not commit production secrets to source control.
- The application accepts either a Base64-encoded 32-byte key or a raw UTF-8 secret of length >= 32 bytes.

## Docker

Build and run locally using Docker and Docker Compose (uses Postgres):

1. Build the Docker image (optional - compose will build automatically):

```powershell
docker build -t devconnect:local .
```

2. Start the app and Postgres with compose:

```powershell
docker-compose up --build
```

3. Environment variables

- `JWT_SECRET` (required) — the JWT signing secret (32+ bytes or Base64 32 bytes). In `docker-compose.yml` a placeholder `changeme_change_in_prod_make_long` is used for local testing; replace it for anything public-facing.
- `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD` — database connection variables used in the `docker-compose.yml` example.

Notes:
- Use `docker-compose down -v` to remove the database volume when you want to reset the DB.
- For production deployments, push your built image to a registry (ECR/DockerHub) and deploy to EC2/ECS behind an ALB. Keep secrets in AWS Secrets Manager or Parameter Store.

## Deploying (CI & manual EC2 deploy)

This repo includes GitHub Actions workflows to build, scan, and (manually) deploy the app to an EC2 instance.

Required repository secrets (add these under Settings → Secrets → Actions):

- `JWT_SECRET` — your production JWT secret (32+ bytes or Base64 32 bytes).
- `AWS_ACCESS_KEY_ID` / `AWS_SECRET_ACCESS_KEY` / `AWS_REGION` — credentials for Terraform to create EC2/ALB resources.
- `EC2_KEY_NAME` — name of an existing EC2 key pair in the target AWS region for SSH access.

To run the manual deploy workflow:

1. Build & push a container image to GHCR (the `deploy-ec2` workflow will do this for you if you invoke it).
2. Go to Actions → Deploy to EC2 (manual) and run the workflow, passing the image tag (e.g. `ghcr.io/owner/repo:sha`).
3. The workflow will run `terraform apply` in `infra/terraform` and output the ALB DNS name.

Before running Terraform in a team or production environment, configure a remote backend (S3 + DynamoDB) to store state securely and enable locking. See `infra/terraform/backend.tf.example` for a sample configuration.
