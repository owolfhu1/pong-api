package com.catalyte.OrionsPets.repositories;

import com.catalyte.OrionsPets.models.Player;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PlayerRepository extends MongoRepository<Player, String> {

  boolean existsByUsername(String username);

  Player findOneByUsername(String username);
}
