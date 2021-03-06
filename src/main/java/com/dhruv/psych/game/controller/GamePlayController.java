package com.dhruv.psych.game.controller;

import com.dhruv.psych.game.exceptions.InvalidGameActionException;
import com.dhruv.psych.game.model.Game;
import com.dhruv.psych.game.model.Player;
import com.dhruv.psych.game.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/play")
public class GamePlayController {

    @Autowired
    private PlayerRepository playerRepository;

    @GetMapping("/")
    public String play(Authentication authentication){
        return authentication.getName();
    }

    @GetMapping("submit-answer/{answer}")
    public void submitAnswer(Authentication authentication,@PathVariable(name = "answer") String answer) throws InvalidGameActionException {
        Optional<Player> player = playerRepository.findByEmail(authentication.getName());
        Player p = player.get();
        if(p == null)
            throw new InvalidGameActionException("player is null");
        p.getCurrentGame().submitAnswer(p,answer);
    }

}
