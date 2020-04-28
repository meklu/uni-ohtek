
package org.meklu.patkis.domain;

import java.util.Objects;

/** Utility class for generating two-item tuples of varying types
 *
 * @param <A> The data type for Triple.a
 * @param <B> The data type for Triple.b
 */
public class Pair<A, B> {
    private A a;
    private B b;

    /** Constructs a Pair
     *
     * @param a The first member of this Pair
     * @param b The second member of this Pair
     */
    public Pair(A a, B b) {
        this.a = a;
        this.b = b;
    }

    public A getA() {
        return a;
    }

    public void setA(A a) {
        this.a = a;
    }

    public B getB() {
        return b;
    }

    public void setB(B b) {
        this.b = b;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.a);
        hash = 97 * hash + Objects.hashCode(this.b);
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
        final Pair<?, ?> other = (Pair<?, ?>) obj;
        if (!Objects.equals(this.a, other.a)) {
            return false;
        }
        return Objects.equals(this.b, other.b);
    }

    @Override
    public String toString() {
        return "(" + a + "," + b + ")";
    }
}
