package com.example.application.impl;

import com.example.domain.model.Ingredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Filter<FIELD_TYPE> {
    private FIELD_TYPE eq;
    private FIELD_TYPE neq;
    private List<FIELD_TYPE> in;
    private List<FIELD_TYPE> nin;
    private FIELD_TYPE contains;
    private FIELD_TYPE notContains;

    public Filter() {
    }

    public Filter(Filter<FIELD_TYPE> filter) {
        this.eq = filter.eq;
        this.neq = filter.neq;
        this.in = filter.in == null ? null : new ArrayList<>(filter.in);
        this.nin = filter.nin == null ? null : new ArrayList<>(filter.nin);
        this.contains = filter.contains;
        this.notContains = filter.notContains;
    }

    public Filter<FIELD_TYPE> copy() {
        return new Filter<>(this);
    }

    public FIELD_TYPE getEq() {
        return eq;
    }

    public void setEq(FIELD_TYPE eq) {
        this.eq = eq;
    }

    public FIELD_TYPE getNeq() {
        return neq;
    }

    public void setNeq(FIELD_TYPE neq) {
        this.neq = neq;
    }

    public List<FIELD_TYPE> getIn() {
        return in;
    }

    public void setIn(List<FIELD_TYPE> in) {
        this.in = in;
    }

    public List<FIELD_TYPE> getNin() {
        return nin;
    }

    public void setNin(List<FIELD_TYPE> nin) {
        this.nin = nin;
    }

    public FIELD_TYPE getContains() {
        return contains;
    }

    public void setContains(FIELD_TYPE contains) {
        this.contains = contains;
    }

    public FIELD_TYPE getNotContains() {
        return notContains;
    }

    public void setNotContains(FIELD_TYPE notContains) {
        this.notContains = notContains;
    }

    @Override
    public boolean equals(Object o) {
        return o == this || (o instanceof Filter other && Objects.equals(eq, other.eq) &&
                Objects.equals(neq, other.neq) &&
                Objects.equals(in, other.in) &&
                Objects.equals(nin, other.nin) &&
                Objects.equals(contains, other.contains) &&
                Objects.equals(notContains, other.notContains));
    }

    @Override
    public int hashCode() {
        return Objects.hash(eq, neq, in, nin, contains, notContains);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Filter{");
        sb.append(eq != null ? "eq=" + eq : "");
        sb.append(neq != null ?  ", neq=" + neq : "");
        sb.append(in != null ?  ", in=" + in : "");
        sb.append(nin != null ?  ", nin=" + nin : "");
        sb.append(contains != null ? ", contains=" + contains : "");
        sb.append(notContains != null ?  ", notContains=" + notContains : "");
        sb.append('}');
        return sb.toString();
    }
}
