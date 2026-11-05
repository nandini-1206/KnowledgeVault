CREATE DATABASE  KnowledgeVault;
USE KnowledgeVault;

CREATE TABLE USER (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50),
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE RESOURCE (
    resource_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    URL VARCHAR(2048),
    file_path VARCHAR(500),
    is_favorite BOOLEAN DEFAULT FALSE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_resource_user
        FOREIGN KEY (user_id)
        REFERENCES USER(user_id),

    CONSTRAINT uq_resource_owner
        UNIQUE (resource_id, user_id),

    CONSTRAINT chk_resource_location
        CHECK (URL IS NOT NULL OR file_path IS NOT NULL)
);

CREATE TABLE TOPIC (
    topic_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    parent_topic_id INT,
    topic_name VARCHAR(100) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_topic_user
        FOREIGN KEY (user_id)
        REFERENCES USER(user_id),

    CONSTRAINT uq_topic_owner
        UNIQUE (topic_id, user_id),

    CONSTRAINT uq_user_topic_name
        UNIQUE (user_id, topic_name),

    CONSTRAINT fk_topic_parent_owner
        FOREIGN KEY (parent_topic_id, user_id)
        REFERENCES TOPIC(topic_id, user_id)
);

CREATE TABLE COLLECTION (
    collection_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    collection_name VARCHAR(100) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_collection_user
        FOREIGN KEY (user_id)
        REFERENCES USER(user_id),

    CONSTRAINT uq_collection_owner
        UNIQUE (collection_id, user_id),

    CONSTRAINT uq_user_collection_name
        UNIQUE (user_id, collection_name)
);


CREATE TABLE NOTE (
    note_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    resource_id INT NOT NULL,
    note_content TEXT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_note_user
        FOREIGN KEY (user_id)
        REFERENCES USER(user_id),

    CONSTRAINT fk_note_resource_owner
        FOREIGN KEY (resource_id, user_id)
        REFERENCES RESOURCE(resource_id, user_id)
);

CREATE TABLE READING_HISTORY (
    history_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    resource_id INT NOT NULL,
    accessed_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    duration INT NOT NULL DEFAULT 0,

    CONSTRAINT fk_history_user
        FOREIGN KEY (user_id)
        REFERENCES USER(user_id),

    CONSTRAINT fk_history_resource_owner
        FOREIGN KEY (resource_id, user_id)
        REFERENCES RESOURCE(resource_id, user_id),

    CONSTRAINT chk_history_duration
        CHECK (duration >= 0)
);

CREATE TABLE RESOURCE_TOPIC (
    resource_id INT NOT NULL,
    topic_id INT NOT NULL,

    PRIMARY KEY (resource_id, topic_id),

    CONSTRAINT fk_rt_resource
        FOREIGN KEY (resource_id)
        REFERENCES RESOURCE(resource_id),

    CONSTRAINT fk_rt_topic
        FOREIGN KEY (topic_id)
        REFERENCES TOPIC(topic_id)
);


CREATE TABLE COLLECTION_RESOURCE (
    collection_id INT NOT NULL,
    resource_id INT NOT NULL,

    PRIMARY KEY (collection_id, resource_id),

    CONSTRAINT fk_cr_collection
        FOREIGN KEY (collection_id)
        REFERENCES COLLECTION(collection_id),

    CONSTRAINT fk_cr_resource
        FOREIGN KEY (resource_id)
        REFERENCES RESOURCE(resource_id)
);


USE KnowledgeVault;

INSERT INTO USER
(user_id, first_name, last_name, email, password)
VALUES
(1, 'Aarav', 'Sharma', 'aarav@example.com', 'TEMP_HASH_1'),
(2, 'Riya', 'Verma', 'riya@example.com', 'TEMP_HASH_2');

INSERT INTO RESOURCE
(resource_id, user_id, title, description, URL, file_path, is_favorite)
VALUES
(101, 1, 'Italian Cooking Guide', 'A guide to Italian recipes',
 'https://example.com/italian-recipes', NULL, TRUE),
(102, 1, 'Travel Photography', 'Collection of photography tips',
 NULL, 'storage/travel_photography.pdf', FALSE),
(103, 2, 'Indoor Gardening', 'Tips for growing indoor plants',
 'https://example.com/gardening', NULL, TRUE);

INSERT INTO TOPIC
(topic_id, user_id, parent_topic_id, topic_name)
VALUES
(201, 1, NULL, 'Food'),
(202, 1, 201, 'Italian Cuisine'),
(203, 1, NULL, 'Travel'),
(204, 2, NULL, 'Gardening');

INSERT INTO COLLECTION
(collection_id, user_id, collection_name)
VALUES
(301, 1, 'Weekend Ideas'),
(302, 1, 'Things to Explore'),
(303, 2, 'Home Projects');

INSERT INTO NOTE
(note_id, user_id, resource_id, note_content)
VALUES
(401, 1, 101, 'Try the pasta recipe this weekend.'),
(402, 1, 101, 'Remember to buy fresh basil.'),
(403, 2, 103, 'Try growing mint near the window.');

INSERT INTO READING_HISTORY
(history_id, user_id, resource_id, accessed_at, duration)
VALUES
(501, 1, 101, '2026-11-01 10:00:00', 1200),
(502, 1, 101, '2026-11-02 15:30:00', 900),
(503, 1, 102, '2026-11-03 12:00:00', 1800),
(504, 2, 103, '2026-11-03 16:00:00', 600);

INSERT INTO RESOURCE_TOPIC
(resource_id, topic_id)
VALUES
(101, 201),
(101, 202),
(102, 203),
(103, 204);

INSERT INTO COLLECTION_RESOURCE
(collection_id, resource_id)
VALUES
(301, 101),
(302, 102),
(303, 103);

SELECT 'Sample data inserted successfully!' AS Status;

