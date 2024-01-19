package org.deustomed;

import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import org.deustomed.postgrest.PostgrestClient;
import org.deustomed.postgrest.PostgrestQuery;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

@Getter
@Setter
public abstract class User {
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
    protected String dni;
    protected String email;
    protected String phoneNumber;
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

    /**
     * Used to obtain the object from the database.
     *
     * @param postgrestClient
     * @param id              The user id.
     */
    public User(@NotNull String id, @NotNull PostgrestClient postgrestClient) {
        PostgrestQuery query = postgrestClient.from("person").select().eq("id", id).getQuery();
        JsonObject responseJson = postgrestClient.sendQuery(query).getAsJsonArray().get(0).getAsJsonObject();
        this.id = responseJson.get("id").getAsString();
        this.name = responseJson.get("name").getAsString();
        this.surname1 = responseJson.get("surname1").getAsString();
        this.surname2 = responseJson.get("surname2").getAsString();
        this.birthDate = LocalDate.parse(responseJson.get("birthdate").getAsString());
        this.sex = Sex.valueOf(responseJson.get("sex").getAsString().toUpperCase());
        this.email = responseJson.get("email").getAsString();
        this.dni = responseJson.get("dni").getAsString();
        this.phoneNumber = responseJson.get("phone").getAsString();
        this.address = responseJson.get("address").getAsString();
    }

    public int getAgeInYears() {
        return Period.between(birthDate, LocalDate.now()).getYears();
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

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", surname1='" + surname1 + '\'' +
                ", surname2='" + surname2 + '\'' +
                ", birthDate=" + birthDate +
                ", sex=" + sex +
                ", dni='" + dni + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
