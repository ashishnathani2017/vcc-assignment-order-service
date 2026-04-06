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
