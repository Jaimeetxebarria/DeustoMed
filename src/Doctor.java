import java.util.ArrayList;
import java.util.Random;

public class Doctor {
    private String name;
    private String surname;
    private int id=00000;
    private String email;
    private String password;
    private String speciality;

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

    public int getId() {
        return id;
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

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public Doctor(String name, String surname, String email, String password, String speciality) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.speciality = speciality;
        this.id = createId();
    }
    private int createId(){
        ArrayList<Integer> ids = new ArrayList<Integer>();
        int newId = (int) (Math.random()*99999);
        while(ids.contains(newId)){
            newId = (int) (Math.random()*99999);
        }
        ids.add(newId);
        return newId;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", speciality='" + speciality + '\'' +
                '}';
    }
}
