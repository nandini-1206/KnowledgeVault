package com.knowledgevault.database;

public class ResourceDAOUpdateTest {

    public static void main(String[] args) {

        ResourceDAO resourceDAO = new ResourceDAO();

        boolean updated = resourceDAO.updateResource(
            104,
            3,
            "Java JDBC Complete Guide",
            "Updated description for JDBC testing",
            "https://example.com/jdbc-guide",
            null
        );

        if (updated) {
            System.out.println("Resource updated successfully!");
        } else {
            System.out.println("Resource update failed!");
        }
    }
}