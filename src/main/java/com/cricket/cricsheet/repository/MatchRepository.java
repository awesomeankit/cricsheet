package com.cricket.cricsheet.repository;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.cricket.cricsheet.common.CricContants;
import com.cricket.cricsheet.model.Match;

public interface MatchRepository extends MongoRepository<Match, String> {

	@Query(value="{'info.teams':{\r\n" + 
			"                   $all:[?0]\r\n" + 
			"               }}")
	@Cacheable(value=CricContants.CACHE_NAME_GET_MATCHES_BY_TEAM, key = "#a0", unless = "#result == null")
	List<Match> findMatchesByTeam(String team);
	
	@Query(value="{'info.teams':{\r\n" + 
			"                   $all:[?0,?1]\r\n" + 
			"               }}")
	@Cacheable(value=CricContants.CACHE_NAME_GET_MATCHES_BY_TEAM_OPPONENT, key = "#a0 + '_' + #a1", unless = "#result == null")
	List<Match> findMatchesByTeamAndOpponent(String team, String opponent);
}
