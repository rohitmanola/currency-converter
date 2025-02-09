
---

# Currency Converter Service

This is a straightforward Spring Boot application designed to convert currencies. It retrieves up-to-date exchange rates from an external API. You can utilize this service to get current exchange rates and perform conversions between different currencies.

## Key Features

- Retrieve the most recent exchange rates for a specified base currency.
- Convert amounts between different currencies.
- Detailed error messages for any issues that arise.
- Unit tests to ensure core functionalities work as expected.

## Prerequisites

- Java version 21 or later.
- Maven.

## How to Set Up

### 1. Clone the Repository

To clone the repository, run the following commands in your terminal:

```
git clone https://github.com/rohitmanola/currency-converter.git
cd currency-converter
```

### 2. Build the Project

To compile the application, run:

```
mvn clean install
```

### 3. Run the Application

To launch the application, execute:

```
mvn spring-boot:run
```

The app will be available at [http://localhost:8080](http://localhost:8080).

### 4. Execute the Tests

To run the unit tests, use this command:

```
mvn test
```

## Configuration

The URL for the external API is defined in the `src/main/resources/application.properties` file. You can update the URL if necessary. Example:

```properties
exchange.rate.api.url=https://v6.exchangerate-api.com/v6/db3e2c09392696b8c15af8e4/latest/{base}
```

## API Reference

### 1. GET `/api/rates`

**Description:**  
This endpoint fetches the latest exchange rates for a specified base currency. It makes a request to an external API and returns a JSON object that includes the update time, documentation links, and a set of conversion rates.

**Query Parameters:**

- **base** (optional): The currency code to use as the base. Even if the client defaults to `USD`, you must specify a valid currency code according to the external API requirements.

**Example Request:**

```
GET http://localhost:8080/api/rates?base=USD
```

**Example Response:**

```json
{
  "result": "success",
  "documentation": "https://www.exchangerate-api.com/docs",
  "terms_of_use": "https://www.exchangerate-api.com/terms",
  "time_last_update_unix": 1738972801,
  "time_last_update_utc": "Sat, 08 Feb 2025 00:00:01 +0000",
  "time_next_update_unix": 1739059201,
  "time_next_update_utc": "Sun, 09 Feb 2025 00:00:01 +0000",
  "base_code": "USD",
  "conversion_rates": {
    "USD": 1,
    "AED": 3.6725,
    "AFN": 74.2357,
    "ALL": 95.1534,
    "AMD": 397.139,
    "ANG": 1.79,
    "AOA": 919.6515,
    "ARS": 1055.92,
    "AUD": 1.5939,
    "AWG": 1.79,
    "AZN": 1.7004
    // ... more currency rates
  }
}
```

**Error Handling:**
- **503 Service Unavailable:**
    - If you provide an invalid base currency, the external API will return an error. This service wraps that error and responds with **503 Service Unavailable**.
    - Other issues, like network errors, will also result in a **503 Service Unavailable** response.

  In these cases, the error message will be in plain text, such as:
  ```
  External API error: unsupported-code
  ```

---

### 2. POST `/api/convert`

**Description:**  
This endpoint performs currency conversion, taking an amount and converting it from one currency to another based on the current exchange rates.

**Request Body:**  
Send a JSON object with the following properties:
- **from:** The source currency code.
- **to:** The target currency code.
- **amount:** The amount to convert.

**Example Request:**

```json
{
  "from": "USD",
  "to": "EUR",
  "amount": 100
}
```

**Example Response:**

```json
{
  "from": "USD",
  "to": "EUR",
  "amount": 100,
  "convertedAmount": 95.0
}
```

> **Note:** The `convertedAmount` value is determined by the current exchange rate provided by the external API.

**Error Handling:**
- **503 Service Unavailable:**
    - If you use an invalid **from** (base) currency code, the external API will return an error. This service wraps it and responds with **503 Service Unavailable**.

- **400 Bad Request:**
    - If the **to** (target) currency code is not found in the `conversion_rates` map, the service will throw an error and respond with **400 Bad Request**, including a message like:

      ```
      Invalid target currency code: XYZ
      ```

---

## Contact Information

For further inquiries, please contact:  
manolarohit24@gmail.com

---
