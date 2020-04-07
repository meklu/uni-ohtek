
package org.meklu.patkis.domain;

import java.util.Objects;

public class Triple<TA, TB, TC> {
    private TA a;
    private TB b;
    private TC c;

    public Triple(TA a, TB b, TC c) {
        this.a = a;
        this.b = b;
        this.c = c;
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

    public TC getC() {
        return c;
    }

    public void setC(TC c) {
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
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Triple<?, ?, ?> other = (Triple<?, ?, ?>) obj;
        if (!Objects.equals(this.a, other.a)) {
            return false;
        }
        if (!Objects.equals(this.b, other.b)) {
            return false;
        }
        if (!Objects.equals(this.c, other.c)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "(" + a + "," + b + "," + c + ")";
    }
}
