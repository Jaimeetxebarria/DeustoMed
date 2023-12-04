package org.deustomed;

import java.util.Date;

public class Patient extends User {
    private int age;
    private String phoneNumer;
    private String address;
    private Date birthDate;
    private String NSS;

    public Patient(int id, String name, String surname1, String surname2, String email, String password, String dni, int age, String phoneNumer, String address, Date birthDate, String NSS) {
        super(id, name, surname1, surname2, email, password, dni);
        this.age = age;
        this.phoneNumer = phoneNumer;
        this.address = address;
        this.birthDate = birthDate;
        this.NSS = NSS;
    }

    public Patient(int id, String name, String surname1, String surname2, String email, String password, String dni, int age, String phoneNumer, String address, Date birthDate) {
        super(id, name, surname1, surname2, email, password, dni);
        this.age = age;
        this.phoneNumer = phoneNumer;
        this.address = address;
        this.birthDate = birthDate;
    }

    public Patient(int id, String name, String surname1, String surname2, String email, String password, int age) {
        super(id, name, surname1, surname2, email, password, "");
        this.age = age;
    }

    public String getNSS() {
        return NSS;
    }

    public void setNSS(String NSS) {
        this.NSS = NSS;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhoneNumer() {
        return phoneNumer;
    }

    public void setPhoneNumer(String phoneNumer) {
        this.phoneNumer = phoneNumer;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname1 + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", age=" + age +
                '}';
    }
}