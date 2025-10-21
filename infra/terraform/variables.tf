variable "aws_region" {
  type    = string
  default = "us-east-1"
}

variable "instance_type" {
  type    = string
  default = "t3.small"
}

variable "key_name" {
  type    = string
  default = ""
  description = "Name of an existing EC2 KeyPair for SSH access. Required to SSH and debug the instance."
}

variable "allowed_ssh_cidr" {
  type    = string
  default = "0.0.0.0/0"
  description = "CIDR range allowed to SSH to the instance (for demos keep open, lock down for production)."
}

variable "image" {
  type    = string
  default = "ghcr.io/mianmasroorahmad/devconnect:latest"
  description = "Container image to run on the EC2 instance (built & pushed by CI)."
}
