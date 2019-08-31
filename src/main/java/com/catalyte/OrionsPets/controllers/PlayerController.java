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
        RatingService.sort(players, type);
        return players;
    }

    @RequestMapping(value = "expect", method = RequestMethod.GET)
    public ArrayList<Double> scores(@RequestParam String playerOne, @RequestParam String playerTwo) {
        ArrayList<Double> list = new ArrayList<>();
        if (
            !playerOne.equals(playerTwo) &&
            playerRepository.existsByUsername(playerOne) &&
            playerRepository.existsByUsername(playerTwo)

        ) {
            double[] array = RatingService.expectations(
                playerRepository.findOneByUsername(playerOne).getRating(),
                playerRepository.findOneByUsername(playerTwo).getRating()
            );
            list.add(array[0]);
            list.add(array[1]);
        } else {
            list.add(0.0);
            list.add(0.0);
        }
        return list;
    }
}
