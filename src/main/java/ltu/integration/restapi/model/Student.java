package ltu.integration.restapi.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Document(collection = "students")
public class Student {

    @EqualsAndHashCode.Exclude
    @Id
    private UUID id = UUID.randomUUID();

    @NotBlank(message = "firstName is required")
    private String firstName;

    @Indexed
    @NotBlank(message = "lastName is required")
    private String lastName;

    @Indexed(unique = true)
    @NotBlank(message = "personnummer is required")
    private String personnummer;

    @NotBlank(message = "email is required")
    private String email;

    @NotNull
    private Date createdAt = new Date();


    public Student (String firstName, String lastName, String personnummer) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.personnummer = personnummer;
        //autogenerating an email from the first three letters of the first and last name
        //todo check logic for names with less than 3 letters
        String first3 = this.firstName.substring(0, 3).toLowerCase();
        String last3 = this.lastName.substring(0, 3).toLowerCase();
        this.email = first3 + last3 + "-1";             //Todo generating a new number if number is already taken
    }

    // ********************** Model Methods ********************** //

    // ********************** Accessor Methods ********************** //

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    // ********************** Common Methods ********************** //
}