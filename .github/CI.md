CI for DevConnect

This repository's CI is implemented in `.github/workflows/ci.yml` and performs the following steps:

- `build-and-test`: Runs Maven tests under JDK 21. The workflow provides a temporary `JWT_SECRET` for CI-only runs. In production, replace this with a secret stored in GitHub Secrets and/or your environment.

- `docker-scan`: Builds a Docker image and runs Trivy to produce a JSON vulnerability report (`trivy-report.json`) which is uploaded as an artifact.

Required repository settings / secrets

- `GITHUB_TOKEN` (automatically provided to workflows): the workflow requests `packages: write` permission so the token can push packages to GHCR. If your organization disallows package write via `GITHUB_TOKEN`, add a personal access token with `write:packages` and store it as `GHCR_PAT` and change the workflow to use that secret for `docker/login-action`.

Tips

- If you want the pipeline to fail on HIGH/CRITICAL findings, add a step to parse `trivy-report.json` and exit non-zero when any such vulnerabilities are found.
- The Dockerfile uses multi-stage build. Keep the base image tags reasonably pinned to avoid unexpected upstream tag deletions.

Mock deployment (runner smoke test)

- The workflow contains a `mock-deploy` job that runs only on `push` to `main` or `master`.
- It packages the application, starts the JAR on the workflow runner, waits for the `/actuator/health` endpoint, runs a smoke curl + jq check, then stops the process.
- This is intended to simulate a deploy and validate that the packaged artifact boots successfully in the CI environment.

Support

If you want, I can:

- Add automatic failure on HIGH/CRITICAL vulnerabilities.
- Publish the built image to GHCR on main/master only and add release notes.
- Harden the Dockerfile for smaller attack surface and reproducible builds.
