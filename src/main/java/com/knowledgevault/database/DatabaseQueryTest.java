
package com.knowledgevault.database;
import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseQueryTest {

    public static void main(String[] args) {

        String query = "SELECT resource_id, title, url FROM RESOURCE";

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet result = statement.executeQuery()
        ) {

            System.out.println("KnowledgeVault Resources:");

            while (result.next()) {

                int resourceId = result.getInt("resource_id");
                String title = result.getString("title");
                String url = result.getString("url");

                System.out.println("-------------------------");
                System.out.println("Resource ID: " + resourceId);
                System.out.println("Title: " + title);
                System.out.println("URL: " + url);
            }

        } catch (SQLException e) {

            System.out.println("Error retrieving resources!");
            e.printStackTrace();

        }
        finally {
    try {
        AbandonedConnectionCleanupThread.checkedShutdown();
    } catch (Exception e) {
        System.out.println("Cleanup warning: " + e.getMessage());
    }
}
    }
}
