package com.catalyte.OrionsPets.repositories;

import com.catalyte.OrionsPets.models.Season;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SeasonRepository extends MongoRepository<Season, String> {
    Season findDistinctByEnd(long end);
}
