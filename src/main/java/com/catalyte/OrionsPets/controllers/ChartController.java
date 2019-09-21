package com.catalyte.OrionsPets.controllers;

import com.catalyte.OrionsPets.models.DataSet;
import com.catalyte.OrionsPets.models.Game;
import com.catalyte.OrionsPets.models.Player;
import com.catalyte.OrionsPets.models.PlotPoint;
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
@RequestMapping(value = "charts")
public class ChartController {
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @RequestMapping(value = "all", method = RequestMethod.GET)
    public ArrayList<DataSet> add() {
        ArrayList<DataSet> returnList = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();
        ArrayList<Player> players = new ArrayList<>();
        List<Game> games = gameRepository.findAll();
        games.sort(Comparator.comparingLong(Game::getTime));

        games.forEach(game -> {
            if (!names.contains(game.getPlayerOne())) {
                names.add(game.getPlayerOne());
                returnList.add(new DataSet(game.getPlayerOne()));
                players.add(new Player(game.getPlayerOne()));
            }
            if (!names.contains(game.getPlayerTwo())) {
                names.add(game.getPlayerTwo());
                returnList.add(new DataSet(game.getPlayerTwo()));
                players.add(new Player(game.getPlayerTwo()));
            }
        });

        games.forEach(game -> {
            Player one = this.getPlayer(players, game.getPlayerOne());
            Player two = this.getPlayer(players, game.getPlayerTwo());
            DataSet oneSet = this.getSet(returnList, game.getPlayerOne());
            DataSet twoSet = this.getSet(returnList, game.getPlayerTwo());
            RatingService.rate(one, two, game.getScoreOne(), game.getScoreTwo());
            oneSet.addPoint(new PlotPoint(game.getTime(), (int)one.getRating()));
            twoSet.addPoint(new PlotPoint(game.getTime(), (int)two.getRating()));
        });

        return returnList;
    }

    private Player getPlayer(List<Player> players, String player) {
        for (Player result : players) {
            if (result.getUsername().equals(player))
                return result;
        }
        return new Player("error");
    }

    private DataSet getSet(List<DataSet> dataSets, String player) {
        for (DataSet dataSet : dataSets) {
            if (dataSet.name.equals(player))
                return dataSet;
        }
        return new DataSet("error");
    }
}
