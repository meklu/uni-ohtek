
package org.meklu.patkis.dao;

import java.util.Set;
import org.meklu.patkis.domain.Snippet;
import org.meklu.patkis.domain.Tag;
import org.meklu.patkis.domain.User;

public interface TagDao extends Dao<Tag> {
    default Tag findByTag(String tag) {
        return this.find("tag", tag);
    }

    /** Finds all Tags for a given Snippet
     *
     * @param s The snippet to look for
     * @return A list of all the Tags for the provided Snippet
     */
    Set<Tag> findBySnippet(Snippet s);

    /** Finds all Tags for a given Snippet/User combination
     *
     * @param s The snippet to look for
     * @param u The user to have tagged it
     * @return A list of all the Tags for the provided Snippet tagged by User u
     */
    Set<Tag> findBySnippet(Snippet s, User u);

    /** Links a given Tag to a given Snippet
     *
     * @param t The tag to link to s
     * @param s The snippet to link to t
     * @param u The tagger to link this for
     * @return Whether we succeeded or not
     */
    boolean linkToSnippet(Tag t, Snippet s, User u);

    /** Unlinks a given Tag from a given Snippet
     *
     * @param t The tag to unlink from s
     * @param s The snippet to unlink from t
     * @param u The tagger to unlink this for
     * @return Whether we succeeded or not
     */
    boolean unlinkFromSnippet(Tag t, Snippet s, User u);
}
