package com.cricket.cricsheet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cricket.cricsheet.model.Match;
import com.cricket.cricsheet.repository.MatchRepository;

@Service
public class MatchService {
	
	@Autowired
	private MatchRepository matchRepository;
	
	public Match findMatchById(String id) {
		try {
			return matchRepository.findById(id).get();
		}catch (Exception e) {
			return null;
		}
	}

	public List<Match> getAllMatches() {
		return matchRepository.findAll();
	}

	public List<Match> findMatchesByTeam(String team, String opponent) {
		List<Match> matchList=null;
		if(opponent == null) {
			matchList = matchRepository.findMatchesByTeam(team);
		}else {
			matchList = matchRepository.findMatchesByTeamAndOpponent(team, opponent);
		}
		return matchList;
	}

}
