package org.temkarus0070.efmsocialmedia.security.persistence.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "Users")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserAccount {

    @Id
    @Column(unique = true)
    @NotNull(message = "имя пользователя не может быть null")
    @NotBlank(message = "имя пользователя не может быть пустым")
    private String username;

    @Column(unique = true)
    @NotNull(message = "email не может быть null")
    @NotBlank(message = "email не может быть пустым")
    @Email(message = "неверный формат email")
    private String email;

    @NotNull(message = "пароль не может быть null")
    @NotBlank(message = "пароль не может быть пустым")
    private String password;

    @JsonIgnore
    private boolean enabled = true;

    public UserAccount(String username) {
        this.username = username;
    }
}
