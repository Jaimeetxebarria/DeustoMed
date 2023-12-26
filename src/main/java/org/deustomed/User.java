package org.deustomed;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public abstract class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected String id;
    protected String name;
    protected String surname1;
    protected String surname2;
    protected String email;
    protected String dni;
    protected Sex sex;

    public User(String id, String name, String surname1, String surname2, String email, String password, String dni) {
        this.id = id;
        this.name = name;
        this.surname1 = surname1;
        this.surname2 = surname2;
        this.email = email;
        this.dni = dni;
    }

    public User(String id, String name, String surname1, String surname2, String email, String password, String dni, Sex sex) {
        this.id = id;
        this.name = name;
        this.surname1 = surname1;
        this.surname2 = surname2;
        this.email = email;
        this.dni = dni;
        this.sex = sex;
    }

    public String getId() {
        return id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSurname2(String surname2) {
        this.surname2 = surname2;
    }

    public String getSurname2() {
        return surname2;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(getId(), user.getId()) &&
                Objects.equals(getName(), user.getName()) &&
                Objects.equals(getSurname1(), user.getSurname1()) &&
                Objects.equals(getSurname2(), user.getSurname2()) &&
                Objects.equals(getEmail(), user.getEmail()) &&
                Objects.equals(getDni(), user.getDni()) && getSex() == user.getSex();
    }
}
