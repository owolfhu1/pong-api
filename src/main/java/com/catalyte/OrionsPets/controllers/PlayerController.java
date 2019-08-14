package com.catalyte.OrionsPets.controllers;

import com.catalyte.OrionsPets.models.Player;
import com.catalyte.OrionsPets.repositories.PlayerRepository;
import com.catalyte.OrionsPets.services.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Orion Wolf_Hubbard on 8/5/19.
 */
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(value = "players")
public class PlayerController {
    @Autowired
    private PlayerRepository playerRepository;

    @RequestMapping(value = "test", method = RequestMethod.GET)
    public String test() {
        return "The API has booted up.";
    }

    @RequestMapping(value = "register", method = RequestMethod.GET)
    public String register(@RequestParam String username) {
        if (playerRepository.existsByUsername(username)) {
            return String.format("Player %s is already registered", username);
        } else {
            playerRepository.insert(new Player(username));
            return String.format("You have registered player %s.", username);
        }
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public ArrayList<String> list() {
        ArrayList<String> names = new ArrayList<>();
        List<Player> players = playerRepository.findAll();
        players.forEach(player -> names.add(player.getUsername()));
        return names;
    }

    @RequestMapping(value = "scores", method = RequestMethod.GET)
    public List<Player> scores(@RequestParam String type) {
        List<Player> players = playerRepository.findAll();
        switch (type) {
            case "rating": players.sort((a,b) -> (int) (b.getRating() - a.getRating())); break;
            case "zachRating": players.sort((a,b) -> (int) (b.getZachRating() - a.getZachRating())); break;
            case "games": players.sort((a,b) -> ((b. getWins() + b.getLosses()) - (a.getWins() + a.getLosses()))); break;
            case "wins": players.sort((a,b) -> (b.getWins() - a.getWins())); break;
            case "losses": players.sort((a,b) -> (b.getLosses() - a.getLosses())); break;
            case "winPercent": players.sort((a, b) -> winPercent(b) - winPercent(a)); break;
        }
        return players;
    }

    private int winPercent(Player player) {
        double wins = player.getWins();
        double total = player.getWins() + player.getLosses();
        return (int)(wins/total*10000);
    }

    @RequestMapping(value = "expect", method = RequestMethod.GET)
    public double[] scores(@RequestParam String playerOne, @RequestParam String playerTwo) {
        if (
            !playerOne.equals(playerTwo) &&
            playerRepository.existsByUsername(playerOne) &&
            playerRepository.existsByUsername(playerTwo)

        ) {
            return RatingService.expectations(
                playerRepository.findOneByUsername(playerOne).getRating(),
                playerRepository.findOneByUsername(playerTwo).getRating()
            );
        } else {
            double[] array = new double[2];
            array[0] = 0;
            array[1] = 0;
            return array;
        }
    }
}
