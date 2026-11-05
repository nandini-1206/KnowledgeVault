
USE KnowledgeVault;

DELIMITER $$

CREATE TRIGGER PreventTopicCycle
BEFORE UPDATE ON TOPIC
FOR EACH ROW
BEGIN
    DECLARE current_parent INT;
    DECLARE next_parent INT;

    SET current_parent = NEW.parent_topic_id;

    WHILE current_parent IS NOT NULL DO

        IF current_parent = NEW.topic_id THEN
            SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT =
            'Circular topic hierarchy is not allowed';
        END IF;

        SET next_parent = NULL;

        SELECT parent_topic_id
        INTO next_parent
        FROM TOPIC
        WHERE topic_id = current_parent;

        SET current_parent = next_parent;

    END WHILE;

END$$

DELIMITER ;

USE KnowledgeVault;

DELIMITER $$

CREATE TRIGGER ValidateResourceTopic
BEFORE INSERT ON RESOURCE_TOPIC
FOR EACH ROW
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM RESOURCE r
        JOIN TOPIC t
            ON r.user_id = t.user_id
        WHERE r.resource_id = NEW.resource_id
          AND t.topic_id = NEW.topic_id
    ) THEN

        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT =
        'Resource and topic must belong to the same user';

    END IF;
END$$

DELIMITER ;
USE KnowledgeVault;

DELIMITER $$

CREATE TRIGGER ValidateResourceTopicUpdate
BEFORE UPDATE ON RESOURCE_TOPIC
FOR EACH ROW
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM RESOURCE r
        JOIN TOPIC t ON r.user_id = t.user_id
        WHERE r.resource_id = NEW.resource_id
          AND t.topic_id = NEW.topic_id
    ) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT =
        'Resource and topic must belong to the same user';
    END IF;
END$$

DELIMITER ;
USE KnowledgeVault;

DELIMITER $$

CREATE TRIGGER ValidateCollectionResource
BEFORE INSERT ON COLLECTION_RESOURCE
FOR EACH ROW
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM COLLECTION c
        JOIN RESOURCE r ON c.user_id = r.user_id
        WHERE c.collection_id = NEW.collection_id
          AND r.resource_id = NEW.resource_id
    ) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT =
        'Collection and resource must belong to the same user';
    END IF;
END$$

DELIMITER ;
USE KnowledgeVault;

DELIMITER $$

CREATE TRIGGER ValidateCollectionResourceUpdate
BEFORE UPDATE ON COLLECTION_RESOURCE
FOR EACH ROW
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM COLLECTION c
        JOIN RESOURCE r ON c.user_id = r.user_id
        WHERE c.collection_id = NEW.collection_id
          AND r.resource_id = NEW.resource_id
    ) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT =
        'Collection and resource must belong to the same user';
    END IF;
END$$

DELIMITER ;

