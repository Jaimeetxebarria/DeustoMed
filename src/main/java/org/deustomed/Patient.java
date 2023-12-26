package org.deustomed;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class Patient extends User {
    private int age;
    private String phoneNumer;
    private String address;
    private Date birthDate;
    private String NSS;
    private ArrayList<Appointment> medicalRecord;

    public Patient(String id, String name, String surname1, String surname2, String email, String password, String dni, int age, String phoneNumer, String address, Date birthDate, String NSS) {
        super(id, name, surname1, surname2, email, password, dni);
        this.age = age;
        this.phoneNumer = phoneNumer;
        this.address = address;
        this.birthDate = birthDate;
        this.NSS = NSS;
    }

    public Patient(String id, String name, String surname1, String surname2, String email, String password, String dni, int age, String phoneNumer, String address, Date birthDate) {
        super(id, name, surname1, surname2, email, password, dni);
        this.age = age;
        this.phoneNumer = phoneNumer;
        this.address = address;
        this.birthDate = birthDate;
    }
    public Patient(String id, String name, String surname1, String surname2, String email, String password, String dni, Sex sex, int age, String phoneNumer, String address, Date birthDate) {
        super(id, name, surname1, surname2, email, password, dni, sex);
        this.age = age;
        this.phoneNumer = phoneNumer;
        this.address = address;
        this.birthDate = birthDate;
    }

    public Patient(String id, String name, String surname1, String surname2, String email, String password, int age) {
        super(id, name, surname1, surname2, email, password, "");
        this.age = age;
    }

    public Patient(){
        super("-1", "", "", "", "", "", "");
        this.age = 0;
        this.phoneNumer = "";
        this.address = "";
        this.birthDate = new Date();
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Patient patient)) return false;
        if (!super.equals(o)) return false;
        return getAge() == patient.getAge() &&
                Objects.equals(getPhoneNumer(), patient.getPhoneNumer()) &&
                Objects.equals(getAddress(), patient.getAddress()) &&
                Objects.equals(getBirthDate(), patient.getBirthDate()) &&
                Objects.equals(getNSS(), patient.getNSS()) &&
                Objects.equals(medicalRecord, patient.medicalRecord);
    }

    @Override
    public String toString() {
        return "Patient{" +
                "name='" + name + '\'' +
                ", surname1='" + surname1 + '\'' +
                ", surname2='" + surname2 + '\'' +
                ", email='" + email + '\'' +
                ", dni='" + dni + '\'' +
                ", age=" + age +
                ", phoneNumer='" + phoneNumer + '\'' +
                ", address='" + address + '\'' +
                ", birthDate=" + birthDate +
                ", Sex='" + sex + '\'' +
                '}';
    }
}
