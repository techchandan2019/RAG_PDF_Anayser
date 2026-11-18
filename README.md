# DocuMind AI – Intelligent Document Assistant

DocuMind AI is an AI-powered document assistant that allows users to upload PDF, DOC, and DOCX documents and ask questions about their content.

The application uses React for the frontend and Spring Boot with Spring AI for the backend.

## 🚀 Features

- Upload PDF, DOC, and DOCX documents.
- Extract and process document content using Spring AI.
- Generate embeddings and store document information.
- Ask questions about uploaded documents.
- Retrieve relevant document content using RAG (Retrieval-Augmented Generation).
- Display AI-generated answers in a conversational interface.
- Modern, responsive AI-agent UI.

## 🛠️ Technology Stack

### Frontend
- React.js
- Vite
- Axios
- Lucide React
- React Markdown
- Framer Motion

### Backend
- Java
- Spring Boot
- Spring AI
- REST APIs
- OpenAI / Ollama (depending on configuration)

### Database
- PostgreSQL / MySQL / other configured database
- Vector Store (depending on backend configuration)

---

# 📁 Project Structure

```text
document-chat-project/
│
├── ai-document-agent/       # React Frontend
│   ├── src/
│   ├── public/
│   ├── package.json
│   └── .env
│
└── spring-ai-backend/       # Spring Boot Backend
    ├── src/
    ├── pom.xml
    └── application.properties
```

---

# ⚙️ Prerequisites

Install the following before running the application:

1. Java JDK 17 or later (as required by your backend).
2. Node.js 20 or later.
3. npm.
4. Maven (if not using the Maven wrapper).
5. Your configured database and vector store.
6. OpenAI API key or Ollama installation, depending on your AI model configuration.

---

# 🔧 Backend Setup – Spring Boot + Spring AI

## Step 1: Open the backend project

Navigate to the Spring Boot backend directory:

```bash
cd spring-ai-backend
```

## Step 2: Configure application properties

Open:

```text
src/main/resources/application.properties
```

Configure your database and AI model credentials.

Example:

```properties
server.port=8080

# Database configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/documentdb
spring.datasource.username=postgres
spring.datasource.password=YOUR_DB_PASSWORD

# Configure your Spring AI model
spring.ai.openai.api-key=${OPENAI_API_KEY}
```

Note: Configure the vector store and embedding model according to your Spring AI implementation.

## Step 3: Configure OpenAI API key

Set your API key as an environment variable.

### Windows PowerShell

```powershell
$env:OPENAI_API_KEY="your-openai-api-key"
```

### Linux / macOS

```bash
export OPENAI_API_KEY="your-openai-api-key"
```

Do not commit API keys or database passwords to GitHub.

## Step 4: Start the backend application

Using Maven:

```bash
mvn spring-boot:run
```

Or using Maven wrapper:

Windows:

```bash
mvnw.cmd spring-boot:run
```

Linux / macOS:

```bash
./mvnw spring-boot:run
```

The backend will start at:

```text
http://localhost:8080
```

---

# 🎨 Frontend Setup – React

## Step 1: Open a new terminal

Navigate to the React project directory:

```bash
cd ai-document-agent
```

## Step 2: Install dependencies

```bash
npm install
```

## Step 3: Configure environment variables

Create a `.env` file in the React project root.

```env
VITE_API_BASE_URL=http://localhost:8080/api
```

This URL connects the React application to the Spring Boot backend.

## Step 4: Start the React application

```bash
npm run dev
```

The frontend will start at:

```text
http://localhost:5173
```

Open this URL in your browser.

---

# 🔄 Application Workflow

1. Start the Spring Boot backend.
2. Start the React frontend.
3. Open the frontend in your browser.
4. Upload a PDF, DOC, or DOCX document.
5. React sends the document to the Spring Boot upload API.
6. Spring AI extracts text, creates chunks, and generates embeddings.
7. Document information and embeddings are stored in the configured database/vector store.
8. Backend returns the document ID.
9. React stores the returned document ID.
10. Enter a question in the AI chat.
11. React sends the document ID and question to the backend.
12. Spring AI retrieves relevant document chunks and generates an answer.
13. The answer is displayed in the React chat interface.

---

# 🔗 Backend API Endpoints

## 1. Upload Document

**Endpoint:**

```http
POST /api/documents/upload
```

Content-Type:

```text
multipart/form-data
```

Parameter:

```text
file: PDF / DOC / DOCX
```

Example response:

```json
{
  "documentId": 101,
  "fileName": "spring-ai-guide.pdf",
  "status": "SUCCESS"
}
```

## 2. Ask Question

**Endpoint:**

```http
POST /api/chat/ask
```

Content-Type:

```text
application/json
```

Request:

```json
{
  "documentId": 101,
  "question": "What is Spring AI?"
}
```

Example response:

```json
{
  "documentId": 101,
  "answer": "Spring AI is a framework that simplifies AI integration in Java applications."
}
```

---

# 🛡️ CORS Configuration

If the frontend and backend run on different ports, configure CORS in the Spring Boot application.

Allow the React development origin:

```text
http://localhost:5173
```

If Spring Security is enabled, configure CORS support in the Spring Security filter chain as well.

---

# 🐛 Troubleshooting

### 1. CORS Error

Verify that your Spring Boot backend allows requests from:

```text
http://localhost:5173
```

### 2. Backend Connection Refused

Make sure Spring Boot is running on:

```text
http://localhost:8080
```

### 3. Database Connection Error

Verify:
- Database server is running.
- Database name is correct.
- Username and password are correct.
- Database and vector-store configuration are valid.

### 4. OpenAI API Error

Verify:
- API key is configured.
- API key is valid.
- Required model access and billing are available.

### 5. Document Upload Error

Check:
- Supported file type.
- Maximum configured upload size.
- Backend logs.
- Document processing and embedding configuration.

---

# ▶️ Quick Start

Open two separate terminals.

**Terminal 1 – Backend**

```bash
cd spring-ai-backend
mvn spring-boot:run
```

**Terminal 2 – Frontend**

```bash
cd ai-document-agent
npm install
npm run dev
```

Open the application:

http://localhost:5173

---

## 📌 Future Enhancements

- Multiple document support.
- Conversation history.
- Streaming AI responses.
- Source document references.
- Document preview.
- User authentication and authorization.

---

**DocuMind AI – Upload. Ask. Understand.**

Powered by React, Spring Boot, and Spring AI.
