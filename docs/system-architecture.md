# System Architecture

## 1. Overview

The AI-Powered Legal Aid Chatbot is designed to provide users with accessible, general legal information in English.

The system focuses on selected areas of Kenyan law:

- Tenant rights
- Employment disputes
- Land ownership

The chatbot is designed to provide general legal information and identify cases that may require assistance from qualified legal professionals.

---

## 2. High-Level Architecture

```text
                         USER
                          |
                          v
                +--------------------+
                |   JAVA FRONTEND    |
                |                    |
                | - Chat Interface   |
                | - User Input       |
                | - Response Display |
                +---------+----------+
                          |
                          | API Requests
                          v
                +--------------------+
                |   PYTHON BACKEND   |
                |                    |
                | - Request Handling |
                | - NLP Processing   |
                | - Intent Detection |
                | - AI Processing    |
                | - Response Logic   |
                +----+-----------+---+
                     |           |
                     v           v
             +-----------+   +----------------+
             |  MYSQL    |   | LEGAL          |
             | DATABASE  |   | KNOWLEDGE BASE |
             |           |   |                |
             | - Users   |   | - Tenant Law   |
             | - Cases   |   | - Employment   |
             | - Data    |   | - Land Rights  |
             +-----------+   +-------+--------+
                                     |
                                     v
                          +----------------------+
                          | CASE ESCALATION      |
                          |                      |
                          | Complex cases are   |
                          | referred to qualified|
                          | legal professionals |
                          +----------------------+

3. Presentation Layer

The presentation layer will be developed using Java.

It will provide the user interface through which users interact with the chatbot.

Main Components
Chat interface
User input
Response display
Conversation interface
Error and system messages

The frontend will communicate with the Python backend through API requests.

4. Application and AI Layer

The application and AI layer will be developed using Python.

This layer will process user questions and determine appropriate responses.

Main Components
Request Handling

Receives requests from the Java frontend and sends processed responses back to the user.

Natural Language Processing

Processes the user's English-language input to identify relevant information and user intent.

Intent Detection

Identifies the general category of the user's question, such as:

Tenant rights
Employment disputes
Land ownership
General legal information
AI Processing

The AI component analyzes the user's question and uses the structured legal knowledge available to the system to generate an appropriate response.

Response Logic

Determines whether the system can provide general information or whether the case should be escalated to a qualified legal professional.


5. Legal Knowledge Base

The system will contain a structured legal knowledge base covering the selected legal areas.

Initial Areas
Tenant rights
Employment disputes
Land ownership

The knowledge base will initially support English-language interactions.

The information will be structured so that the AI component can identify relevant legal information when responding to user questions.

6. Database Layer

The database layer will use MySQL.

The database will store structured system information required by the application.

Possible Data
User information
Conversation records
Case information
System records
Escalation records
Other application data

Database access will be handled through the Python backend rather than directly from the frontend.

7. Case Escalation Layer

The chatbot is not intended to replace qualified legal professionals.

When a user's situation is too complex or requires professional legal assistance, the system will identify the need for escalation.

The system may provide an appropriate message informing the user that they should seek assistance from a qualified legal professional.

This layer is important because the chatbot provides general legal information rather than professional legal representation.

8. System Communication Flow

The general communication process will be:
User
  |
  v
Java Frontend
  |
  v
Python Backend
  |
  v
Natural Language Processing
  |
  v
Intent Detection
  |
  v
Legal Knowledge Base
  |
  v
Response Generation
  |
  +----------------------+
  |                      |
  v                      v
General Information   Case Escalation
  |                      |
  +----------+-----------+
             |
             v
       Java Frontend
             |
             v
            User

9. Technology Stack
| Component               | Technology |
| ----------------------- | ---------- |
| Frontend                | Java       |
| Backend                 | Python     |
| AI/NLP                  | Python     |
| Database                | MySQL      |
| Version Control         | Git        |
| Repository              | GitHub     |
| Development Environment | VS Code    |

10. Language
The chatbot will initially support:
English

11. Initial Legal Scope
The initial version of the system will focus on:

Tenant Rights

General information relating to common tenant and landlord issues.

Employment Disputes

General information relating to selected employment-related disputes and rights.

Land Ownership

General information relating to selected land ownership and land-related issues.

The legal scope may be expanded in future versions.

12. Security and Privacy Considerations
The system should consider user privacy and security during development.

Potential measures include:
Secure database access
Input validation
Authentication where required
Protection of sensitive user information
Secure API communication
Appropriate access controls
Avoiding unnecessary storage of personal information

13. System Limitations
The chatbot will have limitations.
These include:
It will initially support English only.
It will cover only selected areas of Kenyan law.
It will provide general legal information rather than professional legal advice.
AI-generated responses may require verification.
Complex cases may require escalation to qualified legal professionals.
The system's effectiveness will depend on the quality and completeness of its legal knowledge base.

14. Architecture Status
Status: Proposed / In Development
This architecture may be modified during implementation as system requirements, testing results, and technical constraints are identified.
