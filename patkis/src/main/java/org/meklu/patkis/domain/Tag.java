
package org.meklu.patkis.domain;

import java.util.Objects;

/** Serves as the data model for tags to apply to snippets
 *
 * @see Snippet
 */
public class Tag implements Comparable {
    private int id;
    private String tag;

    public Tag(String tag) {
        this(-1, tag);
    }

    public Tag(int id, String tag) {
        this.id = id;
        this.tag = tag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + this.id;
        hash = 29 * hash + Objects.hashCode(this.tag);
        return hash;
    }

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
        final Tag other = (Tag) obj;
        if (this.id != other.id) {
            return false;
        }
        return Objects.equals(this.tag, other.tag);
    }

    @Override
    public int compareTo(Object o) {
        if (o.getClass() != this.getClass()) {
            return Integer.MAX_VALUE;
        }
        Tag t = (Tag) o;
        return this.tag.compareTo(t.tag);
    }
}
