package com.dhruv.psych.game.model;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
@Table(name = "games")
public class Game extends Auditable{

    @ManyToMany
    @Getter
    @Setter
    private Set<Player> players;

    @Getter @Setter
    @Enumerated(EnumType.STRING)
    @NotNull
    private GameMode gameMode;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    @Getter @Setter
    private List<Round> rounds = new ArrayList<>();

    @NotNull
    @Getter @Setter
    private int numRounds = 10;

    @Getter @Setter
    private Boolean hasEllen = false;

    @NotNull
    @ManyToOne
    @Getter @Setter
    private Player leader;

    @ManyToMany(cascade = CascadeType.ALL)
    @Getter @Setter
    private Map<Player,Stat> playerStats = new HashMap<>();

    @Enumerated(EnumType.STRING)
    @Getter @Setter
    private GameStatus gameStatus;

    @ManyToMany
    @Getter @Setter
    private Set<Player> readyPlayers = new HashSet<>();


}
