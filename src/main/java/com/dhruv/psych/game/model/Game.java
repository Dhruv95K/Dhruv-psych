package com.dhruv.psych.game.model;

import com.dhruv.psych.game.Utils;
import com.dhruv.psych.game.exceptions.InvalidGameActionException;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @JsonIdentityReference
    @Getter
    @Setter
    private Set<Player> players = new HashSet<>();

    @Getter @Setter
    @Enumerated(EnumType.STRING)
    @NotNull
    private GameMode gameMode;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    @JsonManagedReference
    @Getter @Setter
    private List<Round> rounds = new ArrayList<>();

    @NotNull
    @Getter @Setter
    private int numRounds = 10;

    @Getter @Setter
    private Boolean hasEllen = false;

    @NotNull
    @ManyToOne
    @JsonIdentityReference
    @Getter @Setter
    private Player leader;

    @ManyToMany(cascade = CascadeType.ALL)
    @JsonManagedReference
    @Getter @Setter
    private Map<Player,Stat> playerStats = new HashMap<>();

    @Enumerated(EnumType.STRING)
    @Getter @Setter
    private GameStatus gameStatus;

    @ManyToMany
    @JsonIdentityReference
    @Getter @Setter
    private Set<Player> readyPlayers = new HashSet<>();

    public Game() {
    }

    public Game(@NotNull GameMode gameMode,int numRounds,Boolean hasEllen,@NotNull Player leader){
        this.gameMode = gameMode;
        this.numRounds = numRounds;
        this.hasEllen = hasEllen;
        this.leader = leader;
        this.players.add(leader);
    }

    public void addPlayer(Player player) throws InvalidGameActionException {
        if(!this.gameStatus.equals(GameStatus.PLAYERS_JOINING))
            throw new InvalidGameActionException("Cannot join after the game has started");
        this.players.add(player);
    }

    public void removePlayer(Player player) throws InvalidGameActionException {
        if(!this.players.contains(player))
            throw new InvalidGameActionException("No Such player in the game");
        this.players.remove(player);
        if(this.players.size() == 0 || (this.players.size() == 1 && !gameStatus.equals(GameStatus.PLAYERS_JOINING)))
            endGame();

    }

    public void startGame(Player player) throws InvalidGameActionException {
        if(!player.equals(this.leader))
            throw new InvalidGameActionException("Only the leader can start the game");
        startNewRound();
    }

    private void startNewRound() {
        this.gameStatus = GameStatus.SUBMITTING_ANSWERS;
        Question question = Utils.getRandomQuestion(this.gameMode);
        Round round = new Round(this,question,this.rounds.size());
        if(hasEllen) {
            round.setEllenAnswer(Utils.getRandomEllenAnswer(question));
        }
        this.rounds.add(round);
    }

    public void submitAnswer(Player player, String answer) throws InvalidGameActionException {
        if(answer.length() == 0)
            throw new InvalidGameActionException("Answers cannot be empty");
        if(!this.players.contains(player))
            throw new InvalidGameActionException("No such player in the game");
        //if player has already submitted then reject
        //if duplicate reject
        if(!this.gameStatus.equals(GameStatus.SUBMITTING_ANSWERS))
            throw new InvalidGameActionException("Game is not accepting answers at the moment");
        Round currentRound = getCurrentRound();
        currentRound.submitAnswer(player,answer);
        if(currentRound.allAnswersSubmitted(this.players.size()))
            this.gameStatus = GameStatus.SELECTING_ANSWERS;

    }

    public void selectAnswer(Player player,PlayerAnswer playerAnswer) throws InvalidGameActionException {
        if(!this.players.contains(player))
            throw new InvalidGameActionException("No such player in the game");
        if(!this.gameStatus.equals(GameStatus.SELECTING_ANSWERS))
            throw new InvalidGameActionException("Game is not selecting answers at the moment");

        Round currentRound = getCurrentRound();
        currentRound.selectAnswer(player,playerAnswer);
        if(currentRound.allAnswersSelected(this.players.size())) {
            if(rounds.size() < numRounds)
                this.gameStatus = GameStatus.WAITING_FOR_READY;
            else
                endGame();
        }
    }

    public void playerIsReady(Player player) throws InvalidGameActionException {
        if(!this.players.contains(player))
            throw new InvalidGameActionException("No such player in the game");
        if(!this.gameStatus.equals(GameStatus.WAITING_FOR_READY))
            throw new InvalidGameActionException("Game is not waiting for players to be ready");

        this.readyPlayers.add(player);
        if(this.readyPlayers.size() == this.players.size())
            startNewRound();
    }

    public void playerIsNotReady(Player player) throws InvalidGameActionException {
        if(!players.contains(player))
            throw new InvalidGameActionException("No such player in the game");
        if(!this.gameStatus.equals(GameStatus.WAITING_FOR_READY))
            throw new InvalidGameActionException("Game is not waiting for players to be ready");

        this.readyPlayers.remove(player);
    }

    private Round getCurrentRound() throws InvalidGameActionException {
        if(this.rounds.size() == 0)
            throw new InvalidGameActionException("The Game has not started");
        return this.rounds.get(this.rounds.size()-1);
    }

    private void endGame() {
        this.gameStatus = GameStatus.ENDED;
    }

    public String getGameState(){
        return ""; //toDo
    }
}
