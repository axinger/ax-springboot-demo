package com.github.axinger.xml.model;

public class IdCard {
    private String id;
    private String name;

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "IdCard{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
