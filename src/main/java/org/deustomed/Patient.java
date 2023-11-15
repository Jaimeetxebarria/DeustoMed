package org.deustomed;

public class Patient extends User {
    private int age;

    public Patient(int id, String name, String surname1, String surname2, String email, String password, int age) {
        super(id, name, surname1, surname2, email, password);
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
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
