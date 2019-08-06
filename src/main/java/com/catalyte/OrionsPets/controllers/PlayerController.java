package com.catalyte.OrionsPets.controllers;

import com.catalyte.OrionsPets.models.Player;
import com.catalyte.OrionsPets.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
}
