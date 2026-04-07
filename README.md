# vcc-assignment-order-service

Standalone Java Spring Boot order service with an H2 in-memory database.

APIs:
- `POST /api/orders`
- `GET /api/orders`
- `GET /api/orders?restaurantId={id}`
- `GET /api/orders/{id}`
- `PUT /api/orders/{id}`
- `DELETE /api/orders/{id}`

Metrics:
- `GET /actuator/health`
- `GET /actuator/prometheus`

Run:

```bash
mvn spring-boot:run
```

Test:

```bash
mvn -Dmaven.repo.local=.m2 test
```

## AWS ECS Deployment

This service is ready to run on AWS ECS Fargate through CloudFormation.

Included deployment assets:
- `Dockerfile` for container image builds
- `.github/workflows/deploy-ecs.yml` for GitHub Actions based ECR + CloudFormation deployment
- `aws/cloudformation/ecs-fargate-service.yml` to provision the ECS service, ALB target group and listener rule, CloudWatch log group, autoscaling policies, alarms, and dashboard
- `ecs/task-definition.json` as a lower-level task definition reference

CloudWatch coverage:
- Log group: `/ecs/order-service`
- Dashboard: `order-service-operations`
- Alarms for high CPU, high memory, unhealthy targets, and target 5xx responses

Required GitHub secrets:
- `AWS_REGION`
- `AWS_ROLE_ARN`
- `AWS_VPC_ID`
- `AWS_SUBNET_IDS`
- `AWS_SECURITY_GROUP_IDS`
- `AWS_ALB_LISTENER_ARN`
- `AWS_ALB_FULL_NAME`
- `AWS_ECS_EXECUTION_ROLE_ARN`
- `AWS_ORDER_TASK_ROLE_ARN`
- `AWS_ALARM_TOPIC_ARN`

The GitHub Actions workflow will:
1. build and push the Docker image to ECR
2. deploy the CloudFormation stack
3. update ECS with the new image
4. keep logs, alarms, and dashboards in CloudWatch

Manual stack deployment example:

```bash
aws cloudformation deploy \
  --stack-name order-service \
  --template-file aws/cloudformation/ecs-fargate-service.yml \
  --parameter-overrides \
    ImageUri=<account-id>.dkr.ecr.<region>.amazonaws.com/order-service:<tag> \
    VpcId=<vpc-id> \
    SubnetIds=<subnet-1>,<subnet-2> \
    SecurityGroupIds=<sg-id> \
    ListenerArn=<listener-arn> \
    LoadBalancerFullName=app/<alb-name>/<alb-id> \
    TaskExecutionRoleArn=<execution-role-arn> \
    TaskRoleArn=<task-role-arn>
```

Notes:
- CloudWatch helps monitor service health and deployment state, but it does not preserve application data.
- The app still uses H2 in-memory storage by default, so data resets when the task restarts.
- For persistent order state in AWS, switch `SPRING_DATASOURCE_*` environment variables to an RDS database.
