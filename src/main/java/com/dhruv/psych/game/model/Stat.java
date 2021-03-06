package com.dhruv.psych.game.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "stats")
public class Stat extends Auditable {

    @Getter @Setter
    private Long gotPsychedCount = 0L;

    @Getter @Setter
    private Long psychedOthersCount = 0L;

    @Getter @Setter
    private Long correctAnswerCount = 0L;
}
