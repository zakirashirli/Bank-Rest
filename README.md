
## 🏦 Bank-Rest API

**Bank-Rest** — это RESTful API на Spring Boot для управления пользователями, банковскими картами и переводами.
Проект поддерживает авторизацию по JWT-токенам, разграничение ролей и работу с базой данных MySQL.

---

### ⚙️ Основные возможности

* 🔐 Регистрация и вход пользователя (JWT-аутентификация)
* 💳 Создание, блокировка, активация и удаление карт
* 💸 Переводы между собственными картами
* 📜 Пагинация и фильтрация
* 👮 Роли пользователей: `USER`, `ADMIN`
* 📘 Документация API через Swagger UI

---

### 🛠️ Технологии

* **Java 25**, **Spring Boot 3.5**
* **Spring Security (JWT)**
* **Spring Data JPA (Hibernate)**
* **MySQL**
* **Lombok**
* **Swagger / OpenAPI**

---

### ⚡️ Установка и запуск

1. **Клонировать репозиторий**

   ```bash
   git clone https://github.com/<твой-username>/Bank-Rest.git
   cd Bank-Rest
   ```

2. **Создать базу данных MySQL**

   ```sql
   CREATE DATABASE bank_system;
   ```

3. **Настроить подключение к базе**
   В файле `src/main/resources/application.properties` укажи свои данные:

   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/bank_system
   spring.datasource.username=root
   spring.datasource.password=1234
   ```

4. **Запустить приложение**

   ```bash
   mvn spring-boot:run
   ```

   или через IntelliJ IDEA — запусти `BankRestApplication`.

---

### 🔑 Основные эндпоинты

| Метод | Эндпоинт             | Описание                           | Авторизация |
| ----- | -------------------- | ---------------------------------- | ----------- |
| POST  | `/api/auth/register` | Регистрация нового пользователя    | ❌           |
| POST  | `/api/auth/login`    | Авторизация и получение JWT токена | ❌           |
| POST  | `/api/cards`         | Создать новую карту                | ✅           |
| GET   | `/api/cards`         | Получить список карт пользователя  | ✅           |
| POST  | `/api/transfers`     | Перевести деньги между картами     | ✅           |
| GET   | `/api/transfers`     | Получить историю переводов         | ✅           |

**Swagger UI:**
👉 `http://localhost:8080/swagger-ui/index.html`

---

### 🧠 Примеры запросов

**Регистрация пользователя:**

```json
{
  "username": "zakir",
  "password": "1234"
}
```

**Создание карты:**

```json
{
  "number": "1234567890123456",
  "holder": "Zakir Ashirli",
  "expiryMonth": 12,
  "expiryYear": 2028,
  "initialBalanceMinor": 100000,
  "currency": "AZN"
}
```

**Перевод средств:**

```json
{
  "fromCardId": "uuid-отправителя",
  "toCardId": "uuid-получателя",
  "amountMinor": 5000
}




