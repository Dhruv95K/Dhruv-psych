package com.dhruv.psych.game.model;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletionService;

@Entity
@Table(name = "players")
public class Player extends User {

    @NotBlank
    @Getter
    @Setter
    private String alias;

    @Getter
    @Setter
    private String psychFaceURL;

    @Getter
    @Setter
    private String picURL;

    @OneToOne(cascade = CascadeType.ALL)
    @JsonManagedReference
    @Getter
    @Setter
    private Stat stats = new Stat();

    @ManyToMany(mappedBy = "players")
    @JsonIdentityReference
    @Getter
    @Setter
    private Set<Game> games = new HashSet<>();

    public Player() { }

    private Player(Builder builder) {
        setEmail(builder.email);
        setSaltedHashedPassword(builder.saltedHashedPassword);
        setAlias(builder.alias);
        setPsychFaceURL(builder.psychFaceURL);
        setPicURL(builder.picURL);
    }

    public Game getCurrentGame() {
        //toDo
        return null;
    }

    public static final class Builder {
        private @Email @NotBlank String email;
        private @NotBlank String saltedHashedPassword;
        private @NotBlank String alias;
        private String psychFaceURL;
        private String picURL;

        public Builder() {
        }

        public Builder email(@Email @NotBlank String val) {
            email = val;
            return this;
        }

        public Builder saltedHashedPassword(@NotBlank String val) {
            saltedHashedPassword = val;
            return this;
        }

        public Builder alias(@NotBlank String val) {
            alias = val;
            return this;
        }

        public Builder psychFaceURL(String val) {
            psychFaceURL = val;
            return this;
        }

        public Builder picURL(String val) {
            picURL = val;
            return this;
        }

        public Player build() {
            return new Player(this);
        }
    }
}
