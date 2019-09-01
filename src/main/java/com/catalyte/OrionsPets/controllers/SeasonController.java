package com.catalyte.OrionsPets.controllers;

import com.catalyte.OrionsPets.models.Game;
import com.catalyte.OrionsPets.models.Player;
import com.catalyte.OrionsPets.models.Season;
import com.catalyte.OrionsPets.repositories.GameRepository;
import com.catalyte.OrionsPets.repositories.SeasonRepository;
import com.catalyte.OrionsPets.services.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Orion Wolf_Hubbard on 8/5/19.
 */
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(value = "seasons")
public class SeasonController {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private SeasonRepository seasonRepository;

    @RequestMapping(value = "current", method = RequestMethod.GET)
    public List<Player> currentSeason() {
        List<Player> players = new ArrayList<>();
        Season current = seasonRepository.findDistinctByEnd(0);
        List<Game> games = gameRepository.findByTimeGreaterThan(current.getStart());
        this.makeAndRate(players, games);
        RatingService.sort(players, "rating");
        return players;
    }

    @RequestMapping(value = "scores", method = RequestMethod.GET)
    public List<Player> currentSeason(@RequestParam String type, @RequestParam String id) {
        List<Player> players = new ArrayList<>();
        Season season;
        if (seasonRepository.findById(id).isPresent())
            season = seasonRepository.findById(id).get();
        else
            season = seasonRepository.findDistinctByEnd(0);
        List<Game> games;
        if (season.getEnd() > 0)
            games = gameRepository.findByTimeBetween(season.getStart(), season.getEnd());
        else
            games = gameRepository.findByTimeGreaterThan(season.getStart());
        this.makeAndRate(players, games);
        RatingService.sort(players, type);
        return players;
    }

    @RequestMapping(value = "currentObj", method = RequestMethod.GET)
    public Season currentObj() {
        return seasonRepository.findDistinctByEnd(0);
    }

    @RequestMapping(value = "all", method = RequestMethod.GET)
    public List<Season> all() {
        return seasonRepository.findAll();
    }

    @RequestMapping(value = "end", method = RequestMethod.GET)
    public String end(@RequestParam String word, @RequestParam long time) {
        if (word.equals("1337")) {
            Season current = seasonRepository.findDistinctByEnd(0);
            current.setEnd(time);
            seasonRepository.save(current);
            Season newSeason = new Season(time + 1);
            seasonRepository.insert(newSeason);
            return "The season has ended and a new one has begun.";
        } else {
            return "";
        }
    }

    @RequestMapping(value = "expect", method = RequestMethod.GET)
    public ArrayList<Double> scores(@RequestParam String playerOne, @RequestParam String playerTwo) {
        ArrayList<Double> list = new ArrayList<>();
        List<Player> players = new ArrayList<>();
        Season current = seasonRepository.findDistinctByEnd(0);
        List<Game> games = gameRepository.findByTimeGreaterThan(current.getStart());
        this.makeAndRate(players, games);
        if (
            !playerOne.equals(playerTwo) &&
            this.hasPlayer(players, playerOne) &&
            this.hasPlayer(players, playerTwo)
        ) {
            double[] array = RatingService.expectations(
                this.getPlayer(players, playerOne).getRating(),
                this.getPlayer(players, playerTwo).getRating()
            );
            list.add(array[0]);
            list.add(array[1]);
        } else {
            list.add(0.0);
            list.add(0.0);
        }
        return list;
    }

    private boolean hasPlayer(List<Player> players, String player) {
        AtomicBoolean result = new AtomicBoolean(false);
        players.forEach(p -> {
            if (p.getUsername().equals(player)) {
                result.set(true);
            }
        });
        return result.get();
    }

    private Player getPlayer(List<Player> players, String player) {
        for (Player result : players) {
            if (result.getUsername().equals(player))
                return result;
        }
        return new Player("error");
    }

    private void makeAndRate(List<Player> players, List<Game> games) {
        games.forEach(game -> {
            if (!this.hasPlayer(players,game.getPlayerOne())) {
                players.add(new Player(game.getPlayerOne()));
            }
            if (!this.hasPlayer(players,game.getPlayerTwo())) {
                players.add(new Player(game.getPlayerTwo()));
            }
        });
        games.forEach(game -> {
            Player playerOne = this.getPlayer(players, game.getPlayerOne());
            Player playerTwo = this.getPlayer(players, game.getPlayerTwo());
            int scoreOne = game.getScoreOne();
            int scoreTwo = game.getScoreTwo();
            RatingService.rate(playerOne, playerTwo, scoreOne, scoreTwo);
        });
    }
}
