package com.dhruv.psych.game.model;

import com.dhruv.psych.game.exceptions.InvalidGameActionException;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "rounds")
public class Round extends Auditable{

    @ManyToOne
    @JsonBackReference
    @Getter @Setter
    @NotNull
    private Game game;

    @NotNull
    @ManyToOne
    @JsonIdentityReference
    @Getter @Setter
    private Question question;

    @ManyToMany(cascade = CascadeType.ALL)
    @JsonManagedReference
    @Getter @Setter
    private Map<Player,PlayerAnswer> submittedAnswers = new HashMap<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JsonManagedReference
    @Getter @Setter
    private Map<Player,PlayerAnswer> selectedAnswers = new HashMap<>();

    @NotNull
    @Getter @Setter
    private int roundNumber;

    @ManyToOne
    @JsonIdentityReference
    @Getter @Setter
    private EllenAnswer ellenAnswer;

    public Round() {
    }

    public Round(@NotNull  Game game, @NotNull Question question, @NotNull int roundNumber) {
        this.game = game;
        this.question = question;
        this.roundNumber = roundNumber;
    }

    public void submitAnswer(Player player, String answer) throws InvalidGameActionException {
        if(submittedAnswers.containsKey(player))
            throw new InvalidGameActionException("Player has already submitted answer");

        for(PlayerAnswer existingAnswer : submittedAnswers.values()){
            if(answer.equals(existingAnswer.getAnswer()))
                throw new InvalidGameActionException("Duplicate Answer");
        }
        submittedAnswers.put(player,new PlayerAnswer(this,player,answer));
    }

    public boolean allAnswersSubmitted(int numPlayers) {
        return this.submittedAnswers.size() == numPlayers ? true : false;
    }

    public void selectAnswer(Player player, PlayerAnswer selectedAnswer) throws InvalidGameActionException {
        if(this.selectedAnswers.containsKey(player))
            throw new InvalidGameActionException("Player has already selected an answer for this round");
        if(selectedAnswer.getPlayer().equals(player))
            throw new InvalidGameActionException("Player cannot select their own answer");

        this.selectedAnswers.put(player,selectedAnswer);
    }

    public boolean allAnswersSelected(int numPlayers) {
        return this.selectedAnswers.size() == numPlayers;
    }
}
