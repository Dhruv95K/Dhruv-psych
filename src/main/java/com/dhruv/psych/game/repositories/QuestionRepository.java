package com.dhruv.psych.game.repositories;

import com.dhruv.psych.game.model.GameMode;
import com.dhruv.psych.game.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question,Long> {
    Question getByQuestion(String question);

    @Query(value = "select * from questions where gameMode =:gameMode order by RAND() LIMIT 1 ",nativeQuery = true)
    Question getRandomQuestion(GameMode gameMode);
}
