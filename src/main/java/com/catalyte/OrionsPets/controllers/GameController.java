package com.catalyte.OrionsPets.controllers;

import com.catalyte.OrionsPets.models.Game;
import com.catalyte.OrionsPets.models.Player;
import com.catalyte.OrionsPets.repositories.GameRepository;
import com.catalyte.OrionsPets.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Orion Wolf_Hubbard on 8/5/19.
 */
@RestController
@RequestMapping(value = "games")
public class GameController {
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add(
        @RequestParam String playerOne,
        @RequestParam String playerTwo,
        @RequestParam int scoreOne,
        @RequestParam int scoreTwo,
        @RequestParam String date
    ) {
        if(
            !playerRepository.existsByUsername(playerOne) ||
            !playerRepository.existsByUsername(playerTwo) ||
            playerOne.equals(playerTwo)
        ) {
            return "Please enter valid players";
        } else {
            Game game = new Game(playerOne, playerTwo, scoreOne, scoreTwo, date);
            gameRepository.insert(game);
            return "You have added a new game";
        }
    }

}
