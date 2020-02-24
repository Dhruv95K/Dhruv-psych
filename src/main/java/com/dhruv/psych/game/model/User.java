package com.dhruv.psych.game.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class User extends Auditable{

    @NotBlank
    @Email
    @Column(unique = true)
    @Getter
    @Setter
    private String email;

    @NotBlank
    @Getter
    private String saltedHashedPassword;

    public void setSaltedHashedPassword(String saltedHashedPassword) {
        this.saltedHashedPassword = new BCryptPasswordEncoder(6).encode(saltedHashedPassword);
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @Getter
    @Setter
    Set<Role> roles = new HashSet<>();

    public User(){
    }

    public User(User user) { // copy constructor
        this.email = user.getEmail();
        this.saltedHashedPassword = user.getSaltedHashedPassword();
        this.roles = user.getRoles();
    }
}
