EC2 Deploy guide (demo)

Required repository secrets (add via GitHub Settings → Secrets):

- AWS_ACCESS_KEY_ID — access key with permissions to create EC2/ALB resources
- AWS_SECRET_ACCESS_KEY — secret key
- AWS_REGION — region to deploy (e.g. us-east-1)
- EC2_KEY_NAME — the name of an existing EC2 KeyPair in the target account (used to SSH into the instance)

Optional / recommended:
- JWT_SECRET — production JWT secret (application currently reads env JWT_SECRET if present).

How to run (manual):

1. Build & push image locally or use GitHub workflow `deploy-ec2`.
   - Example image tag: ghcr.io/<owner>/<repo>:<tag>
2. In GitHub Actions, open `Actions` → `Deploy to EC2 (manual)` and click `Run workflow`, provide the `image_tag` input.
3. The workflow will build/push the container (if you provided a repo image tag pointing to GHCR it will push), then run Terraform to create an EC2 and ALB.
4. Terraform `apply` will output the ALB DNS name; use that URL to access the application.

Notes:
- This is a demo deployment for CV purposes. It uses the default VPC and a single instance. For production, create a dedicated VPC, use autoscaling, and secure credentials.
- The workflow requires the Terraform files under `infra/terraform` to exist (they do in this repo).
