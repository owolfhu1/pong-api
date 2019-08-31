package com.catalyte.OrionsPets.repositories;

import com.catalyte.OrionsPets.models.Game;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GameRepository extends MongoRepository<Game, String> {
    Game findDistinctByTime(long time);
    List<Game> findByTimeGreaterThan(long time);
    List<Game> findByTimeBetween(long start, long end);
}
