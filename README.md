# 🚀 DIY Feature Flag Service (Spring Boot + DynamoDB)

A fully working Spring Boot microservice to manage feature flags using AWS DynamoDB.  
Includes API key authentication and a complete, robust RESTful API for creating, retrieving, updating, and deleting flags.

---

## 📦 Features

- ✅ Full CRUD & PATCH API for feature flag management  
- ✅ DynamoDB integration for persistent storage  
- ✅ Standardized environments (DEV, QA, UAT, PROD) with default values on creation  
- ✅ Case-insensitive and validated partial updates using PATCH  
- ✅ Professional RESTful responses with proper HTTP status codes (201, 204, 404, 400)  
- ✅ Simple API Key authentication  
- ✅ Docker-ready for local DynamoDB testing  

---

## 🔧 Local Setup Instructions

### ✅ Prerequisites

Ensure you have the following installed:

- Java 11+  
- Maven  
- Docker Desktop (must be running)  
- AWS CLI  

### ⚙️ Setup Steps

#### 1. Run Local DynamoDB

```bash
docker run -p 8000:8000 amazon/dynamodb-local
```

#### 2. Configure AWS CLI with Dummy Credentials

Run the following command and enter placeholder values:

```bash
aws configure
```

```text
AWS Access Key ID [None]: FAKEKEY  
AWS Secret Access Key [None]: FAKESECRET  
Default region name [None]: us-east-1  
Default output format [None]:  
```

#### 3. Create the DynamoDB Table

```bash
aws dynamodb create-table \
    --table-name FeatureFlag \
    --attribute-definitions AttributeName=name,AttributeType=S \
    --key-schema AttributeName=name,KeyType=HASH \
    --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5 \
    --endpoint-url http://localhost:8000 \
    --region us-east-1
```

#### 4. Run the Spring Boot Application

```bash
mvn spring-boot:run
```

App will be available at:  
[http://localhost:8080](http://localhost:8080)

---

## 🔐 Authentication

All API requests must include the following header:

```
Key: X-API-KEY  
Value: secret-api-key (defined in application.properties)
```

---

## 📘 API Endpoints

| Method | Path              | Description                                                                 |
|--------|-------------------|-----------------------------------------------------------------------------|
| POST   | `/flags`          | Creates a new flag. Environments default to `false`.                        |
| GET    | `/flags/{name}`   | Retrieves a specific flag. Returns 404 if not found.                        |
| PATCH  | `/flags/{name}`   | Partially updates a flag. Case-insensitive keys. Rejects invalid keys.     |
| DELETE | `/flags/{name}`   | Deletes a flag. Returns 204 No Content on success.                          |

---

## 📝 Example API Requests

### 🔹 Create a New Flag

```bash
curl -X POST http://localhost:8080/flags \
 -H 'Content-Type: application/json' \
 -H 'X-API-KEY: secret-api-key' \
 -d '{"name": "new-login-flow", "updateDetails": "Initial setup for new login"}'
```

✅ **Success Response**:  
`201 Created` with the full `FeatureFlag` object in response body.

---

### 🔹 Partially Update a Flag (Case-Insensitive)

```bash
curl -X PATCH http://localhost:8080/flags/new-login-flow \
 -H 'Content-Type: application/json' \
 -H 'X-API-KEY: secret-api-key' \
 -d '{"environmentValues": {"prod": true}}'
```

✅ **Success Response**:  
`200 OK` with updated `FeatureFlag` object.

---

### 🔹 Invalid Update Request

```bash
curl -X PATCH http://localhost:8080/flags/new-login-flow \
 -H 'Content-Type: application/json' \
 -H 'X-API-KEY: secret-api-key' \
 -d '{"environmentValues": {"STAGING": true}}'
```

❌ **Error Response**:  
`400 Bad Request`

```json
{
  "message": "Invalid environment key in patch request: 'STAGING'. Valid keys are: [DEV, QA, UAT, PROD]"
}
```


---

## 💬 Contact

For questions or suggestions, feel free to open an issue or reach out.
