-- ============================================================
-- AI-Powered Legal Aid Chatbot (Kituo Cha Sheria)
-- MySQL schema, generated from erd.drawio
-- ============================================================

CREATE DATABASE IF NOT EXISTS legal_aid_chatbot
    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE legal_aid_chatbot;

-- ------------------------------------------------------------
-- Lookup table: normalises category out of LEGAL_QUERY /
-- LEGAL_KNOWLEDGE / LEGAL_PROFESSIONAL (lecturer feedback)
-- ------------------------------------------------------------
CREATE TABLE legal_category (
    category_id   INT AUTO_INCREMENT PRIMARY KEY,
    name          VARCHAR(100) NOT NULL UNIQUE,
    description   TEXT
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- Actors
-- ------------------------------------------------------------
CREATE TABLE user (
    user_id         INT AUTO_INCREMENT PRIMARY KEY,
    name            VARCHAR(150) NOT NULL,
    email           VARCHAR(255) NOT NULL UNIQUE,
    password_hash   VARCHAR(255) NOT NULL,
    registered_at   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    -- Kenya Data Protection Act 2019: explicit consent tracking
    consent_given   BOOLEAN NOT NULL DEFAULT FALSE,
    consent_date    DATETIME NULL
) ENGINE=InnoDB;

CREATE TABLE legal_professional (
    professional_id             INT AUTO_INCREMENT PRIMARY KEY,
    name                        VARCHAR(150) NOT NULL,
    email                       VARCHAR(255) NOT NULL UNIQUE,
    password_hash               VARCHAR(255) NOT NULL,
    license_number              VARCHAR(100) NOT NULL UNIQUE,
    specialization_category_id  INT NULL,
    status                      ENUM('active','inactive') NOT NULL DEFAULT 'active',
    registered_at               DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_professional_category
        FOREIGN KEY (specialization_category_id) REFERENCES legal_category(category_id)
        ON DELETE SET NULL
) ENGINE=InnoDB;

CREATE TABLE admin (
    admin_id        INT AUTO_INCREMENT PRIMARY KEY,
    name            VARCHAR(150) NOT NULL,
    email           VARCHAR(255) NOT NULL UNIQUE,
    password_hash   VARCHAR(255) NOT NULL,
    registered_at   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- Conversations and queries
-- ------------------------------------------------------------
CREATE TABLE conversation (
    conversation_id   INT AUTO_INCREMENT PRIMARY KEY,
    -- nullable: guest sessions have no user_id (anonymous access, per use case diagram)
    user_id           INT NULL,
    start_time        DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    end_time          DATETIME NULL,
    status            ENUM('open','closed') NOT NULL DEFAULT 'open',
    CONSTRAINT fk_conversation_user
        FOREIGN KEY (user_id) REFERENCES user(user_id)
        ON DELETE SET NULL
) ENGINE=InnoDB;

CREATE TABLE legal_query (
    query_id          INT AUTO_INCREMENT PRIMARY KEY,
    conversation_id   INT NOT NULL,
    category_id       INT NULL,  -- nullable: may be unclassified until AI processes it
    question_text     TEXT NOT NULL,
    submitted_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_query_conversation
        FOREIGN KEY (conversation_id) REFERENCES conversation(conversation_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_query_category
        FOREIGN KEY (category_id) REFERENCES legal_category(category_id)
        ON DELETE SET NULL
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- AI response (confidence_score / model_version / disclaimer_shown
-- make the escalation decision auditable, per lecturer feedback)
-- ------------------------------------------------------------
CREATE TABLE ai_response (
    response_id        INT AUTO_INCREMENT PRIMARY KEY,
    query_id           INT NOT NULL UNIQUE,
    response_text      TEXT NOT NULL,
    confidence_score    DECIMAL(4,3) NOT NULL,   -- e.g. 0.000 - 1.000
    model_version      VARCHAR(50) NOT NULL,
    disclaimer_shown   BOOLEAN NOT NULL DEFAULT TRUE,
    generated_at       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_response_query
        FOREIGN KEY (query_id) REFERENCES legal_query(query_id)
        ON DELETE CASCADE
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- Escalation (now has an assignable professional + status domain)
-- ------------------------------------------------------------
CREATE TABLE escalation (
    escalation_id              INT AUTO_INCREMENT PRIMARY KEY,
    query_id                   INT NOT NULL UNIQUE,
    assigned_professional_id   INT NULL,  -- nullable until a professional is assigned
    reason                     VARCHAR(255) NOT NULL,
    status                     ENUM('pending','assigned','resolved') NOT NULL DEFAULT 'pending',
    escalated_at               DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_escalation_query
        FOREIGN KEY (query_id) REFERENCES legal_query(query_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_escalation_professional
        FOREIGN KEY (assigned_professional_id) REFERENCES legal_professional(professional_id)
        ON DELETE SET NULL
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- Output of "Provide Legal Assistance" (previously had nowhere to live)
-- ------------------------------------------------------------
CREATE TABLE professional_response (
    prof_response_id   INT AUTO_INCREMENT PRIMARY KEY,
    escalation_id       INT NOT NULL UNIQUE,
    professional_id     INT NOT NULL,
    response_text       TEXT NOT NULL,
    responded_at         DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_profresponse_escalation
        FOREIGN KEY (escalation_id) REFERENCES escalation(escalation_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_profresponse_professional
        FOREIGN KEY (professional_id) REFERENCES legal_professional(professional_id)
        ON DELETE RESTRICT
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- Feedback (supports the Agile / stakeholder-feedback claim)
-- Only registered users can rate -- guests have no user_id to attach it to.
-- ------------------------------------------------------------
CREATE TABLE feedback (
    feedback_id    INT AUTO_INCREMENT PRIMARY KEY,
    response_id    INT NOT NULL,
    user_id        INT NOT NULL,
    rating         TINYINT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    comment        TEXT NULL,
    submitted_at   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_feedback_response
        FOREIGN KEY (response_id) REFERENCES ai_response(response_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_feedback_user
        FOREIGN KEY (user_id) REFERENCES user(user_id)
        ON DELETE CASCADE
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- Legal knowledge base
-- ------------------------------------------------------------
CREATE TABLE legal_knowledge (
    knowledge_id    INT AUTO_INCREMENT PRIMARY KEY,
    category_id     INT NOT NULL,
    topic           VARCHAR(255) NOT NULL,
    content         TEXT NOT NULL,
    source          VARCHAR(255) NULL,
    last_updated    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_knowledge_category
        FOREIGN KEY (category_id) REFERENCES legal_category(category_id)
        ON DELETE RESTRICT
) ENGINE=InnoDB;

-- Many-to-many: which knowledge entries informed which query's response
CREATE TABLE query_knowledge (
    query_knowledge_id   INT AUTO_INCREMENT PRIMARY KEY,
    query_id             INT NOT NULL,
    knowledge_id         INT NOT NULL,
    CONSTRAINT fk_qk_query
        FOREIGN KEY (query_id) REFERENCES legal_query(query_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_qk_knowledge
        FOREIGN KEY (knowledge_id) REFERENCES legal_knowledge(knowledge_id)
        ON DELETE CASCADE,
    UNIQUE KEY uq_query_knowledge (query_id, knowledge_id)
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- Audit log (Kenya Data Protection Act 2019 traceability;
-- replaces the vague "View System Records" use case).
-- actor_type/target_entity are descriptive, not FKs, since a
-- single log table spans many different source tables.
-- ------------------------------------------------------------
CREATE TABLE audit_log (
    audit_id        INT AUTO_INCREMENT PRIMARY KEY,
    actor_type      ENUM('user','legal_professional','admin','system') NOT NULL,
    actor_id        INT NULL,
    action          VARCHAR(255) NOT NULL,
    target_entity   VARCHAR(100) NOT NULL,
    target_id       INT NULL,
    timestamp       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- Seed data: initial legal categories (matches the three areas
-- named in system-architecture.md section 11)
-- ------------------------------------------------------------
INSERT INTO legal_category (name, description) VALUES
    ('Tenant Rights', 'General information on landlord/tenant and rental housing issues'),
    ('Employment Disputes', 'General information on employment-related disputes and rights'),
    ('Land Ownership', 'General information on land ownership and land-related issues');
