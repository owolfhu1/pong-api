package com.catalyte.OrionsPets.controllers;

import com.catalyte.OrionsPets.models.Game;
import com.catalyte.OrionsPets.models.Player;
import com.catalyte.OrionsPets.repositories.GameRepository;
import com.catalyte.OrionsPets.repositories.PlayerRepository;
import com.catalyte.OrionsPets.services.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
        @RequestParam int scoreTwo,
        @RequestParam long time
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
            Game game = new Game(playerOne, playerTwo, scoreOne, scoreTwo, time);
            gameRepository.insert(game);
            Player player1 = playerRepository.findOneByUsername(game.getPlayerOne());
            Player player2 = playerRepository.findOneByUsername(game.getPlayerTwo());
            double before = player1.getRating();
            RatingService.rate(player1, player2, game.getScoreOne(), game.getScoreTwo());
            double after = player1.getRating();
            playerRepository.save(player1);
            playerRepository.save(player2);
            String amount = Math.abs(before - after) + "";
            return String.format(
                "Game recorded: %s has exceeded my expectations and stolen %s rank from %s!",
                before > after ? player2.getUsername() : player1.getUsername(),
                amount.length() > 3 ? amount.substring(0, 4) : amount,
                before <= after ? player2.getUsername() : player1.getUsername()
            );
        }
    }

    @RequestMapping(value = "all", method = RequestMethod.GET)
    public List<Game> all() {
        List<Game> games = gameRepository.findAll();
        games.sort(Comparator.comparingLong(Game::getTime));
        return games;
    }

    @RequestMapping(value = "rate", method = RequestMethod.GET)
    public String rateAllGames() {
        List<Game> games = gameRepository.findAll();
        games.sort(Comparator.comparingLong(Game::getTime));
        playerRepository.findAll().forEach(player -> {
            player.setRating(1500);
            player.setZachRating(1500);
            player.setWins(0);
            player.setLosses(0);
            playerRepository.save(player);
        });
        games.forEach(game -> {
            Player player1 = playerRepository.findOneByUsername(game.getPlayerOne());
            Player player2 = playerRepository.findOneByUsername(game.getPlayerTwo());
            RatingService.rate(player1, player2, game.getScoreOne(), game.getScoreTwo());
            playerRepository.save(player1);
            playerRepository.save(player2);
        });
        return "all games have been rated";
    }

    @RequestMapping(value = "player", method = RequestMethod.GET)
    public List<Game> player(@RequestParam String player) {
        List<Game> games = new ArrayList<>();
        gameRepository.findAll().forEach(game -> {
            if (game.getPlayerOne().equals(player))
                games.add(game);
            else if (game.getPlayerTwo().equals(player)) {
                flipGame(game);
                games.add(game);
            }
        });
        games.sort(Comparator.comparingLong(Game::getTime));
        return games;
    }

    @RequestMapping(value = "vs", method = RequestMethod.GET)
    public List<Game> playerVsPlayer(@RequestParam String playerOne, @RequestParam String playerTwo) {
        List<Game> games = new ArrayList<>();
        gameRepository.findAll().forEach(game -> {
            if (game.getPlayerOne().equals(playerOne) && game.getPlayerTwo().equals(playerTwo))
                games.add(game);
            else if (game.getPlayerOne().equals(playerTwo) && game.getPlayerTwo().equals(playerOne)) {
                flipGame(game);
                games.add(game);
            }
        });
        games.sort(Comparator.comparingLong(Game::getTime));
        return games;
    }

    private void flipGame(Game game) {
        int score1 = game.getScoreOne();
        String player1 = game.getPlayerOne();
        game.setScoreOne(game.getScoreTwo());
        game.setPlayerOne(game.getPlayerTwo());
        game.setScoreTwo(score1);
        game.setPlayerTwo(player1);
    }

    @RequestMapping(value = "update", method = RequestMethod.GET)
    public String update(
        @RequestParam String playerOne,
        @RequestParam String playerTwo,
        @RequestParam int scoreOne,
        @RequestParam int scoreTwo,
        @RequestParam long time
    ) {
        Game original = gameRepository.findDistinctByTime(time);
        Game copy = new Game(
            original.getPlayerOne(),
            original.getPlayerTwo(),
            original.getScoreOne(),
            original.getScoreTwo(),
            original.getTime()
        );
        original.addHistory(copy);
        original.setPlayerOne(playerOne);
        original.setPlayerTwo(playerTwo);
        original.setScoreOne(scoreOne);
        original.setScoreTwo(scoreTwo);
        gameRepository.save(original);
        rateAllGames();
        return "game at " + original.getTime() + " and ratings updated";
    }
}
