package com.catalyte.OrionsPets.services;

import com.catalyte.OrionsPets.models.Player;

public class RatingService {
    private static int K = 20;

    public static void rate(Player player1, Player player2, int score1, int score2) {
        double transform1 = Math.pow(10, player1.getRating() / 400) /
                (Math.pow(10, player1.getRating() / 400) + Math.pow(10, player2.getRating() / 400));
        double transform2 = Math.pow(10, player2.getRating() / 400) /
                (Math.pow(10, player2.getRating() / 400) + Math.pow(10, player1.getRating() / 400));

        double fraction1 = score1 / (score1 + score2);
        double fraction2 = score2 / (score1 + score2);

        double newRating1 = player1.getRating() + K * (fraction1 - transform1);
        double newRating2 = player2.getRating() + K * (fraction2 - transform2);

        player1.setRating(newRating1);
        player2.setRating(newRating2);
    }
}
