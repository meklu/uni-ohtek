
package org.meklu.patkis.domain;

import java.util.Objects;

/** Serves as the data model for tags to apply to snippets
 *
 * @see Snippet
 */
public class Tag implements Comparable {
    private final String tag;

    public Tag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass() != this.getClass()) {
            return false;
        }
        Tag t = (Tag) o;
        return this.tag.equals(t.tag);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.tag);
        return hash;
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
