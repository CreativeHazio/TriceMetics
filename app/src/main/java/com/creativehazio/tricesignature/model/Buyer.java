package com.creativehazio.tricesignature.model;

public class Buyer {
    private String name;
    private String email;
    private String password;

    public Buyer() {
    }

    public Buyer(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
