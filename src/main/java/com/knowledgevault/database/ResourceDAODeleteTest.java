package com.knowledgevault.database;

public class ResourceDAODeleteTest {

    public static void main(String[] args) {

        ResourceDAO resourceDAO = new ResourceDAO();

        boolean deleted = resourceDAO.deleteResource(
            104,
            3
        );

        if (deleted) {
            System.out.println("Resource deleted successfully!");
        } else {
            System.out.println("Resource deletion failed!");
        }
    }
}