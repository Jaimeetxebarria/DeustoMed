package org.deustomed;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

@Getter
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull
    protected final String id;
    @NotNull
    protected String name;
    @NotNull
    protected String surname1;
    @NotNull
    protected String surname2;
    @NotNull
    protected LocalDate birthDate;
    @NotNull
    protected Sex sex;

    //Optional fields
    @Setter
    protected String dni;
    @Setter
    protected String email;
    @Setter
    protected String phoneNumber;
    @Setter
    protected String address;

    public User(@NotNull String id, @NotNull String name, @NotNull String surname1, @NotNull String surname2,
                @NotNull LocalDate birthDate, @NotNull Sex sex, String dni, String email, String phoneNumber,
                String address) {
        this.id = id;
        this.name = name;
        this.surname1 = surname1;
        this.surname2 = surname2;
        this.birthDate = birthDate;
        this.sex = sex;
        this.dni = dni;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public User(@NotNull String id, @NotNull String name, @NotNull String surname1, @NotNull String surname2,
                @NotNull LocalDate birthDate, @NotNull Sex sex) {
        this(id, name, surname1, surname2, birthDate, sex, null, null, null, null);
    }

    public int getAgeInYears() {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public void setSurname1(@NotNull String surname1) {
        this.surname1 = surname1;
    }

    public void setSurname2(@NotNull String surname2) {
        this.surname2 = surname2;
    }

    public void setBirthDate(@NotNull LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void setSex(@NotNull Sex sex) {
        this.sex = sex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(getId(), user.getId())
                && Objects.equals(getName(), user.getName())
                && Objects.equals(getSurname1(), user.getSurname1())
                && Objects.equals(getSurname2(), user.getSurname2())
                && Objects.equals(getBirthDate(), user.getBirthDate())
                && getSex() == user.getSex() && Objects.equals(getDni(), user.getDni())
                && Objects.equals(getEmail(), user.getEmail())
                && Objects.equals(getPhoneNumber(), user.getPhoneNumber())
                && Objects.equals(getAddress(), user.getAddress());
    }

    public static void main(String[] args) {
        User user = new User("00AAA", "Jaime", "Etxebarria", "Ugarte", LocalDate.now(), Sex.MALE, "12345678A", "a", "123", "asd");
    }

}
