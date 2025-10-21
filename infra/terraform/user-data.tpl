#!/bin/bash
set -e

# Install Docker
yum update -y
amazon-linux-extras install -y docker
service docker start
usermod -a -G docker ec2-user

# Pull the image and run container
docker pull ${image} || true
docker rm -f devconnect || true
docker run -d --name devconnect -p 8080:8080 --restart unless-stopped ${image}
