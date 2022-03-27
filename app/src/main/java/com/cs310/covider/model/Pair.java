package com.cs310.covider.model;

import java.io.Serializable;

public class Pair implements Serializable {
    private String key;
    private Serializable value;

    public Pair(String key, Serializable value) {
        this.key = key;
        this.value = value;
    }

    public Pair() {

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Serializable getValue() {
        return value;
    }

    public void setValue(Serializable value) {
        this.value = value;
    }
}
