package com.catalyte.OrionsPets.controllers;

import com.catalyte.OrionsPets.models.Game;
import com.catalyte.OrionsPets.repositories.GameRepository;
import com.catalyte.OrionsPets.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

/**
 * Created by Orion Wolf_Hubbard on 8/5/19.
 */
@CrossOrigin(origins = "*", allowedHeaders = "*")
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
        @RequestParam int scoreTwo
    ) {
        if(
            !playerRepository.existsByUsername(playerOne) ||
            !playerRepository.existsByUsername(playerTwo) ||
            playerOne.equals(playerTwo)
        ) {
            return "Please enter valid players";
        } else if (scoreOne < 0 || scoreTwo < 0 || scoreOne == scoreTwo) {
            return "Please enter valid scores";
        } else {
            int count = (int) gameRepository.count() + 1;
            Game game = new Game(playerOne, playerTwo, scoreOne, scoreTwo, count);
            gameRepository.insert(game);
            return "You have added a new game, game number " + count;
        }
    }

    @RequestMapping(value = "all", method = RequestMethod.GET)
    public List<Game> all() {
        List<Game> games = gameRepository.findAll();
        games.sort(Comparator.comparingInt(Game::getGameNumber));
        return games;
    }

}
