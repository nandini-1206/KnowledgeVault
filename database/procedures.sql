
USE KnowledgeVault;

DROP PROCEDURE IF EXISTS GetUserResources;
DROP PROCEDURE IF EXISTS SearchResources;
DROP PROCEDURE IF EXISTS GetReadingHistory;
DROP PROCEDURE IF EXISTS GetCollectionResources;
DROP PROCEDURE IF EXISTS GetResourceNotes;

DELIMITER $$

CREATE PROCEDURE GetUserResources(
    IN p_user_id INT
)
BEGIN
    SELECT *
    FROM RESOURCE
    WHERE user_id = p_user_id
    ORDER BY created_at DESC;
END$$


CREATE PROCEDURE SearchResources(
    IN p_user_id INT,
    IN p_keyword VARCHAR(200)
)
BEGIN
    SELECT *
    FROM RESOURCE
    WHERE user_id = p_user_id
      AND title LIKE CONCAT('%', p_keyword, '%')
    ORDER BY title;
END$$


CREATE PROCEDURE GetReadingHistory(
    IN p_user_id INT
)
BEGIN
    SELECT
        rh.history_id,
        r.resource_id,
        r.title,
        rh.accessed_at,
        rh.duration
    FROM READING_HISTORY rh
    JOIN RESOURCE r
        ON rh.resource_id = r.resource_id
    WHERE rh.user_id = p_user_id
    ORDER BY rh.accessed_at DESC;
END$$


CREATE PROCEDURE GetCollectionResources(
    IN p_user_id INT,
    IN p_collection_id INT
)
BEGIN
    SELECT
        r.resource_id,
        r.title,
        r.description,
        r.URL,
        r.file_path,
        r.is_favorite
    FROM COLLECTION_RESOURCE cr
    JOIN COLLECTION c
        ON cr.collection_id = c.collection_id
    JOIN RESOURCE r
        ON cr.resource_id = r.resource_id
    WHERE c.user_id = p_user_id
      AND c.collection_id = p_collection_id
    ORDER BY r.title;
END$$


CREATE PROCEDURE GetResourceNotes(
    IN p_user_id INT,
    IN p_resource_id INT
)
BEGIN
    SELECT
        note_id,
        resource_id,
        note_content,
        created_at,
        updated_at
    FROM NOTE
    WHERE user_id = p_user_id
      AND resource_id = p_resource_id
    ORDER BY created_at DESC;
END$$

DELIMITER ;
