package com.dhruv.psych.game.controller;

import com.dhruv.psych.game.model.*;
import com.dhruv.psych.game.repositories.GameRepository;
import com.dhruv.psych.game.repositories.PlayerRepository;
import com.dhruv.psych.game.repositories.QuestionRepository;
import com.dhruv.psych.game.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/dev-test")
public class DevTestController {
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String hello(){

        return "Hello World";
    }

    @GetMapping("/questions")
    public List<Question> getQuestions(){
        return questionRepository.findAll();
    }

    @GetMapping("/question/{id}")
    public Optional<Question> getQuestionById(@PathVariable(name = "id") Long id){
        return questionRepository.findById(id);
    }

    @GetMapping("/players")
    public List<Player> getPlayers(){
        return playerRepository.findAll();
    }

    @GetMapping("/player/{id}")
    public Optional<Player> getPlayerById(@PathVariable(name = "id") Long id){
        return playerRepository.findById(id);
    }

    @GetMapping("/users")
    public List<User> getUsers(){
        return userRepository.findAll();
    }

    @GetMapping("/user/{id}")
    public Optional<User> getUserById(@PathVariable(name = "id") Long id){
        return userRepository.findById(id);
    }

    @GetMapping("/games")
    public List<Game> getGames(){
        return gameRepository.findAll();
    }

    @GetMapping("/game/{id}")
    public Optional<Game> getGameById(@PathVariable(name = "id") Long id){
        return gameRepository.findById(id);
    }

    @GetMapping("/populate")
    public String populateDB() {
        for(Player player: playerRepository.findAll()) {
            player.getGames().clear();
            playerRepository.save(player);
        }
        gameRepository.deleteAll();
        playerRepository.deleteAll();
        questionRepository.deleteAll();

        Player luffy = new Player.Builder()
                .alias("Monkey D. Luffy")
                .email("luffy@interviewbit.com")
                .saltedHashedPassword("strawhat")
                .build();
        playerRepository.save(luffy);
        Player robin = new Player.Builder()
                .alias("Nico Robin")
                .email("robin@interviewbit.com")
                .saltedHashedPassword("poneglyph")
                .build();
        playerRepository.save(robin);

        Game game = new Game();
        game.setGameMode(GameMode.IS_THIS_A_FACT);
        game.setLeader(luffy);
        game.getPlayers().add(luffy);
        gameRepository.save(game);

        questionRepository.save(new Question(
                "What is the most important Poneglyph",
                "Rio Poneglyph",
                GameMode.IS_THIS_A_FACT
        ));

        questionRepository.save(new Question(
                "How far can Luffy stretch?",
                "56 Gomu Gomus",
                GameMode.IS_THIS_A_FACT
        ));

        return "populated";
    }
}
