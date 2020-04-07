
package org.meklu.patkis.domain;

import java.util.Objects;

public class Triple<A, B, C> {
    private A a;
    private B b;
    private C c;

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
