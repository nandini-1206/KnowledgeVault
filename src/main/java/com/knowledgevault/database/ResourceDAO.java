
package com.knowledgevault.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.CallableStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

public class ResourceDAO {

    public int addResource(int userId, String title, String description, String url, String filePath) {
        String query = """
                INSERT INTO RESOURCE (user_id, title, description, url, file_path)
                VALUES (?, ?, ?, ?, ?)
                """;

        if ((url == null || url.isBlank()) && (filePath == null || filePath.isBlank())) {
            System.out.println("Provide either a URL or a file path.");
            return -1;
        }

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setInt(1, userId);
            statement.setString(2, title);
            statement.setString(3, description);
            statement.setString(4, url);
            statement.setString(5, filePath);

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                try (ResultSet result = statement.getGeneratedKeys()) {
                    if (result.next()) {
                        return result.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error adding resource!");
            e.printStackTrace();
        }
        return -1;
    }

    public boolean updateResource(int resourceId, int userId, String title, String description, String url, String filePath) {
        String query = """
                UPDATE RESOURCE
                SET title = ?, description = ?, url = ?, file_path = ?
                WHERE resource_id = ? AND user_id = ?
                """;

        if ((url == null || url.isBlank()) && (filePath == null || filePath.isBlank())) {
            System.out.println("Provide either a URL or a file path.");
            return false;
        }

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setString(1, title);
            statement.setString(2, description);
            statement.setString(3, url);
            statement.setString(4, filePath);
            statement.setInt(5, resourceId);
            statement.setInt(6, userId);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating resource!");
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteResource(int resourceId, int userId) {
        String query = """
                DELETE FROM RESOURCE
                WHERE resource_id = ? AND user_id = ?
                """;

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setInt(1, resourceId);
            statement.setInt(2, userId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting resource!");
            e.printStackTrace();
        }
        return false;
    }

    public List<Resource> getResourcesByUser(int userId) {
        List<Resource> resources = new ArrayList<>();

        String query = """
                SELECT resource_id, user_id, title, description, url, file_path, is_favorite
                FROM RESOURCE
                WHERE user_id = ?
                """;

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setInt(1, userId);

            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    resources.add(new Resource(
                        result.getInt("resource_id"),
                        result.getInt("user_id"),
                        result.getString("title"),
                        result.getString("description"),
                        result.getString("url"),
                        result.getString("file_path"),
                        result.getBoolean("is_favorite")
                    ));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving resources!");
            e.printStackTrace();
        }
        return resources;
    }

    public List<Resource> getUserResourcesUsingProcedure(int userId) {
        List<Resource> resources = new ArrayList<>();
        String query = "{CALL GetUserResources(?)}";

        try (
            Connection connection = DBConnection.getConnection();
            CallableStatement statement = connection.prepareCall(query)
        ) {
            statement.setInt(1, userId);

            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    resources.add(new Resource(
                        result.getInt("resource_id"),
                        result.getInt("user_id"),
                        result.getString("title"),
                        result.getString("description"),
                        result.getString("url"),
                        result.getString("file_path"),
                        result.getBoolean("is_favorite")
                    ));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error executing GetUserResources!");
            e.printStackTrace();
        }
        return resources;
    }

    public List<Resource> searchResources(int userId, String keyword) {
        List<Resource> resources = new ArrayList<>();
        String query = "{CALL SearchResources(?, ?)}";

        try (
            Connection connection = DBConnection.getConnection();
            CallableStatement statement = connection.prepareCall(query)
        ) {
            statement.setInt(1, userId);
            statement.setString(2, keyword);

            try (ResultSet result = statement.executeQuery()) {
                resources = readResourceResults(result, userId);
            }
        } catch (SQLException e) {
            System.out.println("Error executing SearchResources!");
            e.printStackTrace();
        }
        return resources;
    }

    public List<Map<String, Object>> getReadingHistory(int userId) {
        List<Map<String, Object>> history = new ArrayList<>();
        String query = "{CALL GetReadingHistory(?)}";

        try (
            Connection connection = DBConnection.getConnection();
            CallableStatement statement = connection.prepareCall(query)
        ) {
            statement.setInt(1, userId);

            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("history_id", result.getInt("history_id"));
                    row.put("resource_id", result.getInt("resource_id"));
                    row.put("title", result.getString("title"));
                    row.put("accessed_at", result.getTimestamp("accessed_at"));
                    row.put("duration", result.getInt("duration"));
                    history.add(row);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error executing GetReadingHistory!");
            e.printStackTrace();
        }
        return history;
    }

    public List<Resource> getCollectionResources(int userId, int collectionId) {
        List<Resource> resources = new ArrayList<>();
        String query = "{CALL GetCollectionResources(?, ?)}";

        try (
            Connection connection = DBConnection.getConnection();
            CallableStatement statement = connection.prepareCall(query)
        ) {
            statement.setInt(1, userId);
            statement.setInt(2, collectionId);

            try (ResultSet result = statement.executeQuery()) {
                resources = readResourceResults(result, userId);
            }
        } catch (SQLException e) {
            System.out.println("Error executing GetCollectionResources!");
            e.printStackTrace();
        }
        return resources;
    }

    public List<Map<String, Object>> getResourceNotes(int userId, int resourceId) {
        List<Map<String, Object>> notes = new ArrayList<>();
        String query = "{CALL GetResourceNotes(?, ?)}";

        try (
            Connection connection = DBConnection.getConnection();
            CallableStatement statement = connection.prepareCall(query)
        ) {
            statement.setInt(1, userId);
            statement.setInt(2, resourceId);

            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("note_id", result.getInt("note_id"));
                    row.put("resource_id", result.getInt("resource_id"));
                    row.put("note_content", result.getString("note_content"));
                    row.put("created_at", result.getTimestamp("created_at"));
                    row.put("updated_at", result.getTimestamp("updated_at"));
                    notes.add(row);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error executing GetResourceNotes!");
            e.printStackTrace();
        }
        return notes;
    }

    private List<Resource> readResourceResults(ResultSet result, int userId) throws SQLException {
        List<Resource> resources = new ArrayList<>();

        while (result.next()) {
            resources.add(new Resource(
                result.getInt("resource_id"),
                userId,
                result.getString("title"),
                result.getString("description"),
                result.getString("url"),
                result.getString("file_path"),
                result.getBoolean("is_favorite")
            ));
        }
        return resources;
    }
}
