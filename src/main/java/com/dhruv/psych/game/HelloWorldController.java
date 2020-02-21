package com.dhruv.psych.game;

import com.dhruv.psych.game.model.GameMode;
import com.dhruv.psych.game.model.Player;
import com.dhruv.psych.game.model.Question;
import com.dhruv.psych.game.repositories.PlayerRepository;
import com.dhruv.psych.game.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/dev-test")
public class HelloWorldController {
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private QuestionRepository questionRepository;

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

    @GetMapping("/populate")
    public String populateDB() {
        Player luffy = new Player.Builder()
                    .alias("Monkey D. Luffy")
                    .email("abc@gmail.com")
                    .saltedHashedPassword("strawhat")
                    .build();
        playerRepository.save(luffy);

        Player robin = new Player.Builder()
                .alias("Nico Robin")
                .email("robin@interviewbit.com")
                .saltedHashedPassword("poneglyph")
                .build();
        playerRepository.save(robin);

        questionRepository.save(new Question(
                "What is the most important Poneglyph",
                "Rio Poneglyph",
                GameMode.IS_THIS_A_FACT
        ));

        return "populated";
    }
}
