Terraform to create a demo EC2 + ALB for running the devconnect container.

Usage (example):

1. Configure AWS credentials (env or profile).
2. Edit `variables.tf` or pass `-var` values. At minimum set `key_name` to an existing EC2 KeyPair.
3. terraform init
4. terraform apply -var 'image=ghcr.io/your/repo:tag'

Notes:
- This uses the default VPC and first subnet.
- For production use create a dedicated VPC, subnets, and hardened security groups.
