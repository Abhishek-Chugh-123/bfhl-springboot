# BFHL API — Spring Boot

REST API for Chitkara API Round — 24 June.

## Endpoint
**POST** `/bfhl`

## ⚠️ Before Deployment — Update Your Details

Open `src/main/java/com/bfhl/service/BfhlServiceImpl.java` and change these 4 lines:

```java
private static final String FULL_NAME   = "john_doe";      // your name, lowercase, underscore
private static final String DOB         = "17091999";       // ddmmyyyy
private static final String EMAIL       = "john@xyz.com";   // your email
private static final String ROLL_NUMBER = "ABCD123";        // your roll number
```

## Run Locally

```bash
mvn spring-boot:run
# API available at http://localhost:8080/bfhl
```

## Test Locally

```bash
curl -X POST http://localhost:8080/bfhl \
  -H "Content-Type: application/json" \
  -d '{"data": ["a", "1", "334", "4", "R", "$"]}'
```

## Run Tests

```bash
mvn test
```

## Deploy on Render (Free)

1. Push this project to a GitHub repo
2. Go to https://render.com → New → Web Service
3. Connect your GitHub repo
4. Set:
   - **Build Command:** `mvn clean package -DskipTests`
   - **Start Command:** `java -jar target/bfhl-api-1.0.0.jar`
   - **Environment:** Java
5. Deploy → your URL will be `https://your-service.onrender.com/bfhl`

## Deploy on Railway (Free)

1. Push to GitHub
2. Go to https://railway.app → New Project → Deploy from GitHub
3. Railway auto-detects Spring Boot
4. Your URL: `https://your-app.up.railway.app/bfhl`

## Project Structure

```
src/
├── main/java/com/bfhl/
│   ├── BfhlApplication.java          # Entry point
│   ├── controller/BfhlController.java # POST /bfhl
│   ├── service/
│   │   ├── BfhlService.java          # Interface
│   │   └── BfhlServiceImpl.java      # Business logic
│   ├── dto/
│   │   ├── BfhlRequest.java          # Request DTO
│   │   ├── BfhlResponse.java         # Response DTO
│   │   └── ErrorResponse.java        # Error DTO
│   └── exception/
│       └── GlobalExceptionHandler.java
└── test/java/com/bfhl/controller/
    └── BfhlControllerTest.java       # 7 test cases
```
