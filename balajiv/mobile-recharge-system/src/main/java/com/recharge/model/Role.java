package com.recharge.model;

public enum Role {
    ADMIN(1),
    USER(2);

    private final int id;

    Role(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}

