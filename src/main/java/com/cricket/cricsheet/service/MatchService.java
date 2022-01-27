package com.cricket.cricsheet.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cricket.cricsheet.common.CricContants;
import com.cricket.cricsheet.exception.MatchException;
import com.cricket.cricsheet.model.Match;
import com.cricket.cricsheet.model.Record;
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

	public List<Match> findMatchesByTeam(String team, String opponent, String batting) {
		List<Match> matchList=null;
		if(opponent == null) {
			matchList = matchRepository.findMatchesByTeam(team);
		}else {
			matchList = matchRepository.findMatchesByTeamAndOpponent(team, opponent);
		}
		if (matchList == null || matchList.size() == 0 || team.equalsIgnoreCase(opponent)) {
			MatchException exp = new MatchException();
			exp.setErrorCode("100");
			exp.setErrorMessage("Match information not found for team=" + team + ", opponent=" + opponent);
			exp.setStatusCode(HttpStatus.NOT_FOUND);
			throw exp;
		} else if (matchList.size() > 0) {

			if ("first".equals(batting)) {
				matchList = matchList.stream()
						.filter(m -> (CricContants.DECISION_BAT.equals(m.getInfo().getToss().getDecision())
								&& team.equals(m.getInfo().getToss().getWinner()))
								|| CricContants.DECISION_FIELD.equals(m.getInfo().getToss().getDecision())
										&& !team.equals(m.getInfo().getToss().getWinner()))
						.collect(Collectors.toList());

			} else if ("second".equals(batting)) {
				matchList = matchList.stream()
						.filter(m -> (CricContants.DECISION_FIELD.equals(m.getInfo().getToss().getDecision())
								&& team.equals(m.getInfo().getToss().getWinner()))
								|| CricContants.DECISION_BAT.equals(m.getInfo().getToss().getDecision())
										&& !team.equals(m.getInfo().getToss().getWinner()))
						.collect(Collectors.toList());
			}

			if (matchList == null || matchList.size() == 0) {
				MatchException exp = new MatchException();
				exp.setErrorCode("100");
				exp.setErrorMessage("Match information not found for team=" + team + ", opponent=" + opponent +", batting="+batting);
				exp.setStatusCode(HttpStatus.NOT_FOUND);
				throw exp;
			}

		}
		return matchList;
	}

	public Record getWinLossRecord(List<Match> matches, String team) {
		Record winLossRecord = matches.stream()
				.filter(m -> !"no result".equalsIgnoreCase(m.getInfo().getOutcome().getResult()))
				.reduce(new Record(team), // this object will accumulate the wins and losses.
						(r, m) -> { // this BiFunction takes the accumulator and the current Match in the pipe
							if (team.equalsIgnoreCase(m.getInfo().getOutcome().getWinner())) {
								r.setWins(r.getWins() + 1);
							} else if ("tie".equalsIgnoreCase(m.getInfo().getOutcome().getResult())) {
								r.setTies(r.getTies() + 1);
							} else {
								r.setLosses(r.getLosses() + 1);
							}
							return r;
						}, (a, b) -> { // This is a combiner used if Parallel streams are used
							return new Record(a.getTeam(), a.getWins() + b.getWins(), a.getLosses() + b.getLosses(),
									a.getTies() + b.getTies());
						});
		
		return winLossRecord;
	}

}
