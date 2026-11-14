package com.knowledgevault.database;

import java.util.List;

public class ResourceDAOTest {

    public static void main(String[] args) {

        ResourceDAO resourceDAO = new ResourceDAO();

        // 1. INSERT
        int resourceId = resourceDAO.addResource(
            3,
            "CRUD Integration Testing",
            "Testing all four database operations",
            "https://example.com/crud-test",
            null
        );

        if (resourceId == -1) {
            System.out.println("INSERT failed!");
            return;
        }

        System.out.println("\nINSERT successful! ID: " + resourceId);


        // 2. SELECT
        System.out.println("\n--- SELECT ---");

        List<Resource> resources =
                resourceDAO.getResourcesByUser(3);

        for (Resource resource : resources) {
            if (resource.getResourceId() == resourceId) {
                System.out.println("Title: " + resource.getTitle());
                System.out.println("URL: " + resource.getUrl());
            }
        }


        // 3. UPDATE
        System.out.println("\n--- UPDATE ---");

        boolean updated = resourceDAO.updateResource(
            resourceId,
            3,
            "Updated CRUD Testing",
            "Resource successfully updated",
            "https://example.com/updated",
            null
        );

        System.out.println("UPDATE: " + updated);


        // 4. SELECT AGAIN
        System.out.println("\n--- VERIFY UPDATE ---");

        resources = resourceDAO.getResourcesByUser(3);

        for (Resource resource : resources) {
            if (resource.getResourceId() == resourceId) {
                System.out.println("Updated title: " +
                                   resource.getTitle());
            }
        }


        // 5. DELETE
        System.out.println("\n--- DELETE ---");

        boolean deleted = resourceDAO.deleteResource(
            resourceId,
            3
        );

        System.out.println("DELETE: " + deleted);


        // 6. FINAL SELECT
        System.out.println("\n--- VERIFY DELETE ---");

        resources = resourceDAO.getResourcesByUser(3);

        boolean exists = false;

        for (Resource resource : resources) {
            if (resource.getResourceId() == resourceId) {
                exists = true;
                break;
            }
        }

        System.out.println("Resource still exists: " + exists);
    }
}