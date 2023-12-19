package org.deustomed.chat;

public class ChatUser {

    protected String name;
    protected String surname1;
    protected String surname2;
    protected String id;

    public ChatUser(String name, String surname1, String surname2, String id) {
        this.name = name;
        this.surname1 = surname1;
        this.surname2 = surname2;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname1() {
        return surname1;
    }

    public void setSurname1(String surname1) {
        this.surname1 = surname1;
    }

    public String getSurname2() {
        return surname2;
    }

    public void setSurname2(String surname2) {
        this.surname2 = surname2;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return name + " " + surname1 + " " + surname2;
    }
}
