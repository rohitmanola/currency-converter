# Currency Converter Application

This is a simple Spring Boot application that converts one currency to another. It gets real-time exchange rates from an external API. You can use this application to get current rates and convert amounts between different currencies.

## Features

- Get the latest exchange rates for a base currency.
- Convert an amount from one currency to another.
- Clear error messages when something goes wrong.
- Unit tests for basic functionality.

## Prerequisites

- Java 21 or higher.
- Maven.

## Getting Started

### 1. Clone the Repository

Open your terminal and run:

```
git clone https://github.com/ramkousik/currency-converter.git
cd currency-converter
```

### 2. Build the Application

To build the project, run:

```
mvn clean install
```

### 3. Run the Application

To start the application, run:

```
mvn spring-boot:run
```

The application will run at [http://localhost:8080](http://localhost:8080).

### 4. Running the Tests

To run the unit tests, use the command:

```
mvn test
```

## Configuration

The external API URL is set in the `src/main/resources/application.properties` file. You can change it if needed. For example:

```properties
exchange.rate.api.url=https://v6.exchangerate-api.com/v6/db3e2c09392696b8c15af8e4/latest/{base}
```

## API Documentation

### 1. GET `/api/rates`

**Overview:**  
This endpoint gets the latest exchange rates for a given base currency. It calls an external API and returns a JSON response with update times, links to documentation, and a list of conversion rates.

**Query Parameter:**

- **base** (optional): This is the currency code you want to use as the base. Even if a default like `USD` might be used on the client side, you must send a valid currency code as required by the external API.

**Example Request:**

```
GET http://localhost:8080/api/rates?base=USD
```

**Example Successful Response:**

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
    - If you send an invalid base currency code, the external API returns an error. Our service wraps this error and sends a **503 Service Unavailable** response.
    - If there is another problem, such as a network error, you will also get a **503 Service Unavailable** response.

  In these cases, the error message is plain text, for example:
  ```
  External API error: unsupported-code
  ```

---

### 2. POST `/api/convert`

**Overview:**  
This endpoint converts an amount from one currency to another. It uses the latest exchange rates from the external API to calculate the conversion.

**Request Body:**  
Send a JSON object with these fields:
- **from:** The currency code you want to convert from.
- **to:** The currency code you want to convert to.
- **amount:** The number you want to convert.

**Example Request:**

```json
{
  "from": "USD",
  "to": "EUR",
  "amount": 100
}
```

**Example Successful Response:**

```json
{
  "from": "USD",
  "to": "EUR",
  "amount": 100,
  "convertedAmount": 95.0
}
```

> **Note:** The value of `convertedAmount` depends on the current exchange rate from the external API.

**Error Handling:**
- **503 Service Unavailable:**
    - If you send an invalid **from** (base) currency code, the external API returns an error. Our service wraps this error and sends a **503 Service Unavailable** response.

- **400 Bad Request:**
    - If the **to** (target) currency code is not found in the `conversion_rates` map, our service throws an error and sends a **400 Bad Request** response with a message like:

      ```
      Invalid target currency code: XYZ
      ```

---

## Contact

For any questions, please contact ramkousikallam@gmail.com.
