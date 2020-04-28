
package org.meklu.patkis.dao;

import java.util.List;

import org.meklu.patkis.domain.Snippet;
import org.meklu.patkis.domain.User;

public interface SnippetDao extends Dao<Snippet> {
    /** Gets the Snippets available to the specified user
     *
     * @param u The User that's being checked against
     * @return A list of Snippets viewable by u
     */
    List<Snippet> getAvailableSnippets(User u);
}
