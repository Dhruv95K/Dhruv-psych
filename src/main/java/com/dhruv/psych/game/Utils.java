package com.dhruv.psych.game;

import com.dhruv.psych.game.config.ApplicationContextProvider;
import com.dhruv.psych.game.config.SpringConfiguration;
import com.dhruv.psych.game.model.EllenAnswer;
import com.dhruv.psych.game.model.GameMode;
import com.dhruv.psych.game.model.Question;
import com.dhruv.psych.game.repositories.EllenAnswerRepository;
import com.dhruv.psych.game.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.aspectj.SpringConfiguredConfiguration;

public class Utils {
    private static QuestionRepository questionRepository;
    private static EllenAnswerRepository ellenAnswerRepository;

    static {
        questionRepository = (QuestionRepository) ApplicationContextProvider
                .getApplicationContext()
                .getBean("questionRepository");
        ellenAnswerRepository = (EllenAnswerRepository) ApplicationContextProvider
                .getApplicationContext()
                .getBean("ellenAnswerRepository");
    }

    public static Question getRandomQuestion(GameMode gameMode){
        return questionRepository.getRandomQuestion(gameMode);
    }

    public static EllenAnswer getRandomEllenAnswer(Question question) {
        return ellenAnswerRepository.getRandomAnswer(question);
    }
}
