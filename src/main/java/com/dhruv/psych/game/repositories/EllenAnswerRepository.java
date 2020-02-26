package com.dhruv.psych.game.repositories;

import com.dhruv.psych.game.model.EllenAnswer;
import com.dhruv.psych.game.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EllenAnswerRepository  extends JpaRepository<EllenAnswer,Long> {
    @Query(value = "select * from ellenanswers where question =:question order by RAND() LIMIT 1 ",nativeQuery = true)
    EllenAnswer getRandomAnswer(Question question);
}
