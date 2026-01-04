# 🏭 Kafka Producer Application

## 📍 Overview
The **Kafka Producer Application** exposes a REST API endpoint `/api/projects` that receives project events (`CREATE` or `UPDATE`) and sends them to Kafka topics.  
If a topic does not exist when a `CREATE` event is received, the application uses **KafkaAdminClient** to dynamically create it.

---

## ⚙️ Technologies
- Java 17
- Spring Boot 3+
- Spring Kafka
- Lombok
- Docker Compose (for Kafka & Zookeeper)

---

## 🧩 API Endpoint

### POST `/api/projects`

#### Request Body
```json
{
  "projectId": "P1001",
  "name": "Kafka Integration Demo",
  "description": "Producer-Consumer example",
  "type": "CREATE",
  "timestamp": "2025-10-31T12:00:00Z"
}
```

#### Response
```json
{
  "status": "SUCCESS",
  "topic": "project-create",
  "message": "Event published successfully"
}
```

---

## 🧠 Why Use KafkaAdminClient?
| Reason | Explanation |
|--------|--------------|
| Controlled topic creation | Ensures a topic is created **only when needed**. |
| Avoid unwanted topics | Prevents accidental topics caused by typos or misconfigurations. |
| Proper configurations | Allows defining partitions, replication, and policies. |
| Centralized management | Keeps topic management inside producer only. |

### Example Code
```java
@Autowired
private KafkaAdmin kafkaAdmin;

public void createTopicIfNotExists(String topicName) {
    try (AdminClient admin = AdminClient.create(kafkaAdmin.getConfigurationProperties())) {
        if (!admin.listTopics().names().get().contains(topicName)) {
            NewTopic topic = new NewTopic(topicName, 1, (short) 1);
            admin.createTopics(Collections.singletonList(topic));
            log.info("✅ Created topic: {}", topicName);
        }
    } catch (Exception e) {
        log.error("❌ Failed to create topic: {}", e.getMessage());
    }
}
```

---

## 🧰 Kafka Docker Setup

```yaml
version: '3.8'
services:
  zookeeper:
    image: bitnami/zookeeper:latest
    environment:
      - ALLOW_ANONYMOUS_LOGIN=yes
    ports:
      - "2181:2181"

  kafka:
    image: bitnami/kafka:latest
    ports:
      - "9092:9092"
    environment:
      - KAFKA_BROKER_ID=1
      - KAFKA_CFG_ZOOKEEPER_CONNECT=zookeeper:2181
      - KAFKA_CFG_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092
      - KAFKA_CFG_LISTENERS=PLAINTEXT://:9092
      - KAFKA_AUTO_CREATE_TOPICS_ENABLE=false
```

---

## 🧭 Steps to Run

1. Start Kafka:
   ```bash
   docker compose -f docker-compose-kafka.yml up -d
   ```

2. Start Producer application (port `8080`)

3. Send event:
   ```bash
   curl -X POST http://localhost:8080/api/projects    -H "Content-Type: application/json"    -d '{
         "projectId":"P1001",
         "name":"Kafka Demo",
         "description":"Producer event creation",
         "type":"CREATE",
         "timestamp":"2025-10-31T12:00:00Z"
       }'
   ```

---

## ✅ Summary
- Only Producer creates topics dynamically.
- Uses KafkaAdminClient for topic management.
- Prevents unwanted auto-topic creation.
