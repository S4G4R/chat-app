package com.example.chatapp.users;

// POJO for a User

public class Users {

    private String id;
    private String name;
    private String status;

    public Users() {}

    public Users(String name, String status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Users)) return false;
        Users o = (Users) obj;

        return this.name.equals(o.name);
    }
}
