
package org.meklu.patkis.domain;

import java.util.Objects;

/** Utility class for generating three-item tuples of varying types
 *
 * @param <A> The data type for Triple.a
 * @param <B> The data type for Triple.b
 * @param <C> The data type for Triple.c
 */
public class Triple<A, B, C> {
    private A a;
    private B b;
    private C c;

    /** Constructs a Triple
     *
     * @param a The first member of this Triple
     * @param b The second member of this Triple
     * @param c The third member of this Triple
     */
    public Triple(A a, B b, C c) {
        this.a = a;
        this.b = b;
        this.c = c;
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

    public C getC() {
        return c;
    }

    public void setC(C c) {
        this.c = c;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.a);
        hash = 23 * hash + Objects.hashCode(this.b);
        hash = 23 * hash + Objects.hashCode(this.c);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Triple<?, ?, ?> other = (Triple<?, ?, ?>) obj;
        if (!Objects.equals(this.a, other.a)) {
            return false;
        }
        if (!Objects.equals(this.b, other.b)) {
            return false;
        }
        return Objects.equals(this.c, other.c);
    }

    @Override
    public String toString() {
        return "(" + a + "," + b + "," + c + ")";
    }
}
