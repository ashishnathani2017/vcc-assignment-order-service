# vcc-assignment-order-service

Standalone Java Spring Boot order service with an H2 in-memory database.

APIs:
- `POST /api/orders`
- `GET /api/orders`
- `GET /api/orders?restaurantId={id}`
- `GET /api/orders/{id}`
- `PUT /api/orders/{id}`
- `DELETE /api/orders/{id}`

Run:

```bash
mvn spring-boot:run
```

Test:

```bash
mvn -Dmaven.repo.local=.m2 test
```

## AWS ECS Deployment

This service is ready to run on AWS ECS Fargate.

Included deployment assets:
- `Dockerfile` for container image builds
- `.github/workflows/deploy-ecs.yml` for GitHub Actions based ECR + ECS deployment
- `ecs/task-definition.json` as the ECS task definition template

Recommended AWS setup:
- ECR repository named `order-service`
- ECS cluster named `applications`
- ECS service named `order-service`
- Application Load Balancer health check path: `/actuator/health/readiness`
- CloudWatch log group: `/ecs/order-service`

Required GitHub secrets:
- `AWS_REGION`
- `AWS_ROLE_ARN`

Notes:
- The app still uses H2 in-memory storage by default, so data resets when the task restarts.
- For production persistence, switch `SPRING_DATASOURCE_*` environment variables to an RDS database.
