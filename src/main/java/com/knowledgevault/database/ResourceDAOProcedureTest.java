
package com.knowledgevault.database;

import java.util.List;
import java.util.Map;

public class ResourceDAOProcedureTest {
    public static void main(String[] args) {
        ResourceDAO dao = new ResourceDAO();

        System.out.println("\n========== GET USER RESOURCES ==========");
        List<Resource> resources = dao.getUserResourcesUsingProcedure(1);
        for (Resource r : resources) {
            System.out.println(r.getResourceId() + " - " + r.getTitle());
        }
        System.out.println("Total resources: " + resources.size());

        System.out.println("\n========== SEARCH RESOURCES ==========");
        List<Resource> searchResults = dao.searchResources(1, "Italian");
        for (Resource r : searchResults) {
            System.out.println(r.getResourceId() + " - " + r.getTitle());
        }
        System.out.println("Search results: " + searchResults.size());

        System.out.println("\n========== READING HISTORY ==========");
        List<Map<String, Object>> history = dao.getReadingHistory(1);
        for (Map<String, Object> row : history) {
            System.out.println("History ID: " + row.get("history_id"));
            System.out.println("Resource: " + row.get("title"));
            System.out.println("Duration: " + row.get("duration"));
            System.out.println("-------------------------");
        }
        System.out.println("History records: " + history.size());

        System.out.println("\n========== COLLECTION RESOURCES ==========");
        List<Resource> collectionResources = dao.getCollectionResources(1, 301);
        for (Resource r : collectionResources) {
            System.out.println(r.getResourceId() + " - " + r.getTitle());
        }
        System.out.println("Collection resources: " + collectionResources.size());

        System.out.println("\n========== RESOURCE NOTES ==========");
        List<Map<String, Object>> notes = dao.getResourceNotes(1, 101);
        for (Map<String, Object> row : notes) {
            System.out.println("Note ID: " + row.get("note_id"));
            System.out.println("Note: " + row.get("note_content"));
            System.out.println("-------------------------");
        }
        System.out.println("Total notes: " + notes.size());

        System.out.println("\n========== ALL PROCEDURE TESTS COMPLETED ==========");
    }
}
