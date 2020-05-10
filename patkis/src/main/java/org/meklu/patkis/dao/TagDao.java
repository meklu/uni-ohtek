
package org.meklu.patkis.dao;

import org.meklu.patkis.domain.Tag;

public interface TagDao extends Dao<Tag> {
    default Tag findByTag(String tag) {
        return this.find("tag", tag);
    }
}
