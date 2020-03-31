
package org.meklu.patkis.domain;

import java.util.HashSet;
import java.util.Set;

public class Snippet {
    private final int id;
    private String title = "";
    private String description = "";
    private boolean isPublic = false;
    private String snippet = "";
    private User owner;

    private final Set<Tag> tags = new HashSet<>();

    public Snippet(int id, User owner) {
        this.id = id;
        this.owner = owner;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isIsPublic() {
        return isPublic;
    }

    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Set<Tag> getTags() {
        return tags;
    }
}
