
package org.meklu.patkis.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/** Serves as the data model for code snippets
 *
 * @see User
 */
public class Snippet {
    private int id = -1;
    private String title = "";
    private String description = "";
    private boolean isPublic = false;
    private String snippet = "";
    private User owner;

    // Should probably refactor this into some collection of Tag/User combos
    // and generate a sanitized tag set automagically on updates
    private final Set<Tag> tags = new HashSet<>();
    // This contains tags that were set by our current user
    private final Set<Tag> userTags = new HashSet<>();
    // This contains tags that should be unlinked next
    private final Set<Tag> unlinkTags = new HashSet<>();

    /** Instantiates a Snippet object
     *
     * @param owner The User to set as the owner of this Snippet
     */
    public Snippet(User owner) {
        this.owner = owner;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
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

    public void addTag(Tag t) {
        if (this.unlinkTags.contains(t)) {
            this.unlinkTags.remove(t);
        }
        if (this.userTags.contains(t)) {
            return;
        }
        this.userTags.add(t);
        this.tags.add(t);
    }

    public void removeTag(Tag t) {
        if (!this.userTags.contains(t)) {
            return;
        }
        this.unlinkTags.add(t);
        this.userTags.remove(t);
        this.tags.remove(t);
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Collection<Tag> tags) {
        this.tags.clear();
        this.tags.addAll(tags);
    }

    public Set<Tag> getUserTags() {
        return userTags;
    }

    public void setUserTags(Collection<Tag> userTags) {
        this.userTags.clear();
        this.userTags.addAll(userTags);
    }

    public Set<Tag> getUnlinkTags() {
        return unlinkTags;
    }

    public void setUnlinkTags(Collection<Tag> unlinkTags) {
        this.unlinkTags.clear();
        this.unlinkTags.addAll(unlinkTags);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + this.id;
        hash = 13 * hash + Objects.hashCode(this.title);
        hash = 13 * hash + Objects.hashCode(this.description);
        hash = 13 * hash + (this.isPublic ? 1 : 0);
        hash = 13 * hash + Objects.hashCode(this.snippet);
        hash = 13 * hash + Objects.hashCode(this.owner);
        hash = 13 * hash + Objects.hashCode(this.tags);
        return hash;
    }

    // This auto-generated method is just a tad too long
    @SuppressWarnings("checkstyle:MethodLength")
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Snippet other = (Snippet) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.isPublic != other.isPublic) {
            return false;
        }
        if (!Objects.equals(this.title, other.title)) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        if (!Objects.equals(this.snippet, other.snippet)) {
            return false;
        }
        if (!Objects.equals(this.owner, other.owner)) {
            return false;
        }
        return Objects.equals(this.tags, other.tags);
    }
}
