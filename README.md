# Spring Boot Kafka Producer-Consumer System

This project demonstrates a **Spring Boot-based Kafka Producer and Consumer architecture** where:
- The **Producer application** creates or updates Kafka topics dynamically based on incoming REST API requests.
- The **Consumer application** consumes messages from these topics and processes them.

---

## 🔧 Architecture Overview

### Components:
1. **Producer Application (port 8080)**
   - Exposes a REST endpoint `/api/projects`.
   - Accepts a JSON payload representing a `ProjectEvent`.
   - Based on `eventType` (CREATE/UPDATE), dynamically creates or updates Kafka topics.
   - Uses `KafkaAdminClient` to manage topic creation safely.

2. **Consumer Application (port 8081)**
   - Consumes messages from Kafka topics created by the Producer.
   - Uses `@KafkaListener` to listen to topics dynamically.
   - Disables Kafka’s auto topic creation feature to ensure controlled topic management.

3. **Kafka Broker (Docker)**
   - Managed using `docker-compose`.
   - Includes **Kafka**, **Zookeeper**, and an optional **Kafka UI** (like Kafdrop or AKHQ).

---

## ⚙️ Kafka Docker Setup

Your `docker-compose-kafka.yml` file might look like this:

```yaml
version: '3.8'
services:
  zookeeper:
    image: confluentinc/cp-zookeeper:latest
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
      ZOOKEEPER_TICK_TIME: 2000

  kafka:
    image: confluentinc/cp-kafka:latest
    ports:
      - "9092:9092"
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://localhost:9092
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
      KAFKA_AUTO_CREATE_TOPICS_ENABLE: 'false'  # Disable auto topic creation

  kafka-ui:
    image: provectuslabs/kafka-ui:latest
    ports:
      - "8085:8080"
    environment:
      KAFKA_CLUSTERS_0_NAME: local
      KAFKA_CLUSTERS_0_BOOTSTRAPSERVERS: kafka:9092
```

Run it using:
```bash
docker-compose -f docker-compose-kafka.yml up -d
```

Access Kafka UI at 👉 `http://localhost:8085`

---

## 🚀 Producer Application

### 📍 Endpoint: `/api/projects` (POST)

Used to send project events to Kafka.

#### Request Body:
```json
{
  "projectId": "P123",
  "name": "MyProject",
  "description": "First project",
  "type": "CREATE",
  "timestamp": "2025-10-31T10:00:00Z"
}
```

#### Behavior:
- If `type = CREATE` → creates a new topic (e.g. `project-create`).
- If `type = UPDATE` → sends messages to the update topic (`project-update`).
- Topics are created programmatically using `KafkaAdminClient`.

### Example Service Call (from another app)
```java
@Service
@AllArgsConstructor
public class KafkaProducerClient {
    private final RestTemplate restTemplate;

    public String sendProjectEvent(ProjectEvent event) {
        String url = "http://localhost:8080/api/projects";
        return restTemplate.postForObject(url, event, String.class);
    }
}
```

### Required Bean
```java
@Configuration
public class AppConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```

---

## 🧠 Why Use `KafkaAdminClient`?
`KafkaAdminClient` allows programmatic topic creation and validation at runtime.  
- ✅ Ensures **controlled topic management**.
- ✅ Prevents **unwanted topics** being created automatically.
- ✅ Helps enforce **naming conventions** and **replication policies**.

---

## 🚫 Why Disable Auto Topic Creation?
We set `KAFKA_AUTO_CREATE_TOPICS_ENABLE=false` because:
- Auto-created topics have **default configurations** (e.g. 1 partition, replication factor 1).
- It can lead to **inconsistent setups** and **production issues**.
- The application handles topic creation explicitly and safely.

---

## 🧾 Consumer Application

### Key Points:
- Runs on port `8081`.
- Listens to specific topics created by the Producer.
- Uses `@KafkaListener` to consume messages.

```java
@Service
public class ProjectEventConsumer {
    @KafkaListener(topics = "project-create", groupId = "project-group")
    public void consumeCreate(String message) {
        System.out.println("Consumed CREATE message: " + message);
    }

    @KafkaListener(topics = "project-update", groupId = "project-group")
    public void consumeUpdate(String message) {
        System.out.println("Consumed UPDATE message: " + message);
    }
}
```

---

## 🧩 End-to-End Flow

1. **Start Kafka** via Docker.
2. **Run Producer App** (`mvn spring-boot:run` on port 8080).
3. **Run Consumer App** (`mvn spring-boot:run` on port 8081).
4. Send request:
   ```bash
   curl -X POST http://localhost:8080/api/projects    -H "Content-Type: application/json"    -d '{
     "projectId": "P123",
     "name": "Demo",
     "description": "Kafka demo",
     "type": "CREATE",
     "timestamp": "2025-10-31T10:00:00Z"
   }'
   ```
5. The **Producer** creates the topic and sends the message.
6. The **Consumer** receives and logs the event.

---

## 🧰 Troubleshooting

| Issue | Possible Fix |
|-------|---------------|
| `Cannot connect to Kafka broker` | Ensure Docker Kafka container is running on port `9092`. |
| `Topic does not exist` | Check if Kafka auto-create is disabled; ensure Producer created the topic. |
| `RestTemplate bean missing` | Add a `@Bean` for `RestTemplate` in your config. |
| `Consumer not receiving messages` | Verify `groupId` and topic name are correct. |

---

## ✅ Summary
- Kafka topics are **not auto-created** but handled by the Producer.
- The Consumer dynamically consumes those topics.
- `KafkaAdminClient` ensures reliable topic creation and management.
- The setup runs seamlessly on **Windows local** using **Docker**.

---
