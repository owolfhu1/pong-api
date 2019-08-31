package com.catalyte.OrionsPets.services;

import com.catalyte.OrionsPets.models.Player;

import java.util.List;

public class RatingService {
    private static int K = 50;
    private static int zachK = 20;

    public static void rate(Player player1, Player player2, int score1, int score2) {
        double transform1 = Math.pow(10, player1.getRating() / 400) / (Math.pow(10, player1.getRating() / 400) + Math.pow(10, player2.getRating() / 400));
        double transform2 = Math.pow(10, player2.getRating() / 400) / (Math.pow(10, player1.getRating() / 400) + Math.pow(10, player2.getRating() / 400));

        double zachform1 = Math.pow(10, player1.getZachRating() / 400) / (Math.pow(10, player1.getZachRating() / 400) + Math.pow(10, player2.getZachRating() / 400));
        double zachform2 = Math.pow(10, player2.getZachRating() / 400) / (Math.pow(10, player1.getZachRating() / 400) + Math.pow(10, player2.getZachRating() / 400));

        double scoreOne = (double)score1;
        double scoreTwo = (double)score2;

        double fraction1 = scoreOne / (scoreOne + scoreTwo);
        double fraction2 = scoreTwo / (scoreOne + scoreTwo);

        double newRating1 = player1.getRating() + K * (fraction1 - transform1);
        double newRating2 = player2.getRating() + K * (fraction2 - transform2);

        double newZachting1 = player1.getZachRating() + zachK * ((score1 > score2 ? 1 : 0) - zachform1);
        double newZachting2 = player2.getZachRating() + zachK * ((score2 > score1 ? 1 : 0) - zachform2);

        player1.setRating(newRating1);
        player2.setRating(newRating2);

        player1.setZachRating(newZachting1);
        player2.setZachRating(newZachting2);

        if (score1 > score2) {
            player1.win();
            player2.lose();
        } else {
            player1.lose();
            player2.win();
        }
    }

    public static double[] expectations(double one, double two) {
        double[] array = new double[2];
        array[0] = Math.pow(10, one / 400) / (Math.pow(10, one / 400) + Math.pow(10, two / 400));
        array[1] = Math.pow(10, two / 400) / (Math.pow(10, one / 400) + Math.pow(10, two / 400));
        return array;
    }

    public static void sort(List<Player> players, String type) {
        switch (type) {
            case "rating": players.sort((a,b) -> (int) (b.getRating() - a.getRating())); break;
            case "zachRating": players.sort((a,b) -> (int) (b.getZachRating() - a.getZachRating())); break;
            case "games": players.sort((a,b) -> ((b. getWins() + b.getLosses()) - (a.getWins() + a.getLosses()))); break;
            case "wins": players.sort((a,b) -> (b.getWins() - a.getWins())); break;
            case "losses": players.sort((a,b) -> (b.getLosses() - a.getLosses())); break;
            case "winPercent": players.sort((a, b) -> winPercent(b) - winPercent(a)); break;
        }
    }

    private static int winPercent(Player player) {
        double wins = player.getWins();
        double total = player.getWins() + player.getLosses();
        return (int)(wins/total*10000);
    }
}
