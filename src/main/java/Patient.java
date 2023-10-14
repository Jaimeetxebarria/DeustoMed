package main.java;

import java.util.ArrayList;

public class Patient {
    private int id=00000;
    private String name;
    private String surname;
    private String email;
    private String password;
    private int age;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Patient(String name, String surname, String email, String password, int age) {
        this.name=name;
        this.surname=surname;
        this.email=email;
        this.password=password;
        this.age=age;
        this.id = createId();

    }
    private int createId(){
        ArrayList<Integer> ids = new ArrayList<Integer>();
        int newId = (int) (Math.random()*99999);
        while(ids.contains(newId)){
            newId = (int) (Math.random()*99999);
            if (ids.size()>=99999){
                System.out.println("Error: No more ids available");
                return -1;
            }
        }
        ids.add(newId);
        return newId;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", age=" + age +
                '}';
    }
}
