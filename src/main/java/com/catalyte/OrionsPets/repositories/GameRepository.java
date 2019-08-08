package com.catalyte.OrionsPets.repositories;

import com.catalyte.OrionsPets.models.Game;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GameRepository extends MongoRepository<Game, String> {
    Game findDistinctByGameNumber(int gameNumber);
}
