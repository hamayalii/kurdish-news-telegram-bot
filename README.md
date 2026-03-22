# 📰 Multilingual to Kurdish News Telegram Bot

A highly efficient, automated Telegram Bot built with **Java & Spring Boot** that aggregates global news from various international RSS feeds (BBC, DW, NPR, etc.), translates them into flawless Central Kurdish (Sorani) using AI, and broadcasts them to a Telegram channel/chat.

## ✨ Key Features

* **🌍 Multilingual RSS Aggregation:** Fetches real-time news from multiple international sources automatically.
* **🧠 AI-Powered Translation:** Integrates with LLM APIs (Cohere/DeepSeek) using advanced prompt engineering to provide highly accurate, context-aware translations into Kurdish (Sorani).
* **🗄️ Duplicate Prevention:** Uses a **PostgreSQL** database to store processed article URLs, ensuring that users never receive the same news twice.
* **🚫 Custom News Filtering:** Built-in logic to automatically detect and filter out specific categories (e.g., sports news) based on URL and title keyword analysis.
* **⏱️ Automated Broadcasting:** Utilizes Spring Boot's `@Scheduled` tasks to fetch, process, and send news at specific intervals automatically.
* **📱 Interactive Commands:** Supports manual triggers like `/news` with background processing (multithreading) to avoid blocking the bot.

## 🛠️ Tech Stack

* **Language:** Java 17+
* **Framework:** Spring Boot
* **Database:** PostgreSQL (Spring Data JPA)
* **API Integration:** Telegram Bots API (`TelegramLongPollingBot`), Cohere AI API
* **Other Tools:** Maven, Git

## 🚀 Getting Started

### Prerequisites
* Java Development Kit (JDK) 17 or higher
* PostgreSQL installed and running
* A Telegram Bot Token (from BotFather)
* An AI API Key (Cohere/DeepSeek)

### Installation

1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/hamayalii/kurdish-news-telegram-bot.git](https://github.com/YourUsername/kurdish-news-telegram-bot.git)
    cd kurdish-news-telegram-bot
    ```

2.  **Configure Environment Variables:**
    * Locate the `src/main/resources/application.properties.example` file.
    * Rename it or copy it to `application.properties`.
    * Fill in your actual database credentials, Telegram token, and AI API key.
    * *Note: `application.properties` is ignored by git to keep your secrets safe.*

3.  **Run the application:**
    ```bash
    ./mvnw spring-boot:run
    ```

## 🤝 Contributing
Contributions, issues, and feature requests are welcome! Feel free to check the issues page.

## 📝 License
This project is open-source and available under the MIT License.