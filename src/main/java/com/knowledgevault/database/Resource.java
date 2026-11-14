package com.knowledgevault.database;

public class Resource {

    private int resourceId;
    private int userId;
    private String title;
    private String description;
    private String url;
    private String filePath;
    private boolean favorite;

    public Resource(int resourceId, int userId,
                    String title, String description,
                    String url, String filePath,
                    boolean favorite) {

        this.resourceId = resourceId;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.url = url;
        this.filePath = filePath;
        this.favorite = favorite;
    }

    public int getResourceId() {
        return resourceId;
    }

    public int getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getFilePath() {
        return filePath;
    }

    public boolean isFavorite() {
        return favorite;
    }
}