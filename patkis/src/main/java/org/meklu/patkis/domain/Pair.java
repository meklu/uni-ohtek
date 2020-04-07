
package org.meklu.patkis.domain;

import java.util.Objects;

public class Pair<TA, TB> {
    private TA a;
    private TB b;

    public Pair(TA a, TB b) {
        this.a = a;
        this.b = b;
    }

    public TA getA() {
        return a;
    }

    public void setA(TA a) {
        this.a = a;
    }

    public TB getB() {
        return b;
    }

    public void setB(TB b) {
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
        if (!Objects.equals(this.b, other.b)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "(" + a + "," + b + ")";
    }
}
