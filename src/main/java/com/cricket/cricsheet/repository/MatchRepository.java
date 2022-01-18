package com.cricket.cricsheet.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.cricket.cricsheet.model.Match;

public interface MatchRepository extends MongoRepository<Match, String> {

	@Query(value="{'info.teams':{\r\n" + 
			"                   $all:[?0]\r\n" + 
			"               }}")
	List<Match> findMatchesByTeam(String team);
	
	@Query(value="{'info.teams':{\r\n" + 
			"                   $all:[?0,?1]\r\n" + 
			"               }}")
	List<Match> findMatchesByTeamAndOpponent(String team, String opponent);
}
