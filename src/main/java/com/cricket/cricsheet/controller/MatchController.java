package com.cricket.cricsheet.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cricket.cricsheet.common.CricContants;
import com.cricket.cricsheet.exception.MatchException;
import com.cricket.cricsheet.model.ErrorModel;
import com.cricket.cricsheet.model.Match;
import com.cricket.cricsheet.model.Record;
import com.cricket.cricsheet.service.MatchService;

@RestController
@RequestMapping(value = "/cricsheet/api/matches")
public class MatchController {

	private static final Logger logger = LogManager.getLogger(MatchController.class);

	@Autowired
	private MatchService matchService;

	@GetMapping(path = "/matchid/{id}")
	public ResponseEntity<Match> getMatchById(@PathVariable(name = "id") String id) {
		logger.info("Finding Match information with id=[" + id + "]");
		Match match;
		try {
			match = matchService.findMatchById(id);

			if (match != null) {
				return ResponseEntity.ok().body(match);
			} else {
				MatchException exp = new MatchException();
				exp.setErrorCode("100");
				exp.setErrorMessage("Match information not found for matchid=" + id);
				exp.setStatusCode(HttpStatus.NOT_FOUND);
				throw exp;
			}
		} catch (MatchException mexp) {
			throw mexp;
		} catch (Exception e) {
			MatchException exp = new MatchException();
			exp.setErrorCode("101");
			exp.setErrorMessage("Internal Server Error");
			exp.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
			throw exp;
		}

	}

	@GetMapping
	public ResponseEntity<List<Match>> getAllMatches() {
		List<Match> allMatchList = null;
		try {
			allMatchList = matchService.getAllMatches();

			if (allMatchList != null && allMatchList.size() > 0)
				return ResponseEntity.ok().header(CricContants.HEADER_MATCH_COUNT, String.format("%d", allMatchList.size()))
						.body(allMatchList);
			else {
				MatchException exp = new MatchException();
				exp.setErrorCode("100");
				exp.setErrorMessage("Match information not found.");
				exp.setStatusCode(HttpStatus.NOT_FOUND);
				throw exp;
			}
		} catch (MatchException mexp) {
			throw mexp;
		} catch (Exception e) {
			MatchException exp = new MatchException();
			exp.setErrorCode("101");
			exp.setErrorMessage("Internal Server Error");
			exp.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
			throw exp;
		}
	}

	@GetMapping(path = "teams/{team}")
	public ResponseEntity<List<Match>> getMatchByTeam(@PathVariable(name = "team") String team,
			@RequestParam(name = "opponent", required = false) String opponent,
			@RequestParam(name = "batting", required = false) String batting) {
		List<Match> matches;
		try {
			matches = matchService.findMatchesByTeam(team, opponent, batting);
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.set(CricContants.HEADER_MATCH_COUNT, String.format("%d", matches.size()));
			return new ResponseEntity<>(matches, responseHeaders, HttpStatus.OK);

		} catch (MatchException mexp) {
			throw mexp;
		} catch (Exception e) {
			logger.error("Exception:",e);
			MatchException exp = new MatchException();
			exp.setErrorCode("101");
			exp.setErrorMessage("Internal Server Error");
			exp.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
			throw exp;
		}

	}

	@GetMapping(path = "/{team}/record")
	public Record getWinLossRecord(@PathVariable(name = "team") String team,
			@RequestParam(name = "opponent", required = false) String opponent,
			@RequestParam(name = "batting", required = false) String batting) {
		List<Match> matches = null;
		try {
			matches = matchService.findMatchesByTeam(team, opponent, batting);
			Record winLossRecord = matchService.getWinLossRecord(matches, team);
			return winLossRecord;
		} catch (MatchException mexp) {
			throw mexp;
		} catch (Exception e) {
			MatchException exp = new MatchException();
			exp.setErrorCode("101");
			exp.setErrorMessage("Internal Server Error");
			exp.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
			throw exp;
		}
	}

	@ExceptionHandler
	public ResponseEntity<ErrorModel> handleMatchException(MatchException exception) {
		ErrorModel errorModel = new ErrorModel(exception.getErrorCode(), exception.getErrorMessage(),
				exception.getErrorMessage());

		return new ResponseEntity<>(errorModel, exception.getStatusCode());
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorModel> handleValidationException(ConstraintViolationException exception) {
		ErrorModel errorModel = new ErrorModel("100", exception.getLocalizedMessage(),
				exception.getLocalizedMessage());

		return new ResponseEntity<>(errorModel, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorModel> handleNotFoundException(Exception exception) {
		ErrorModel errorModel = new ErrorModel("100", exception.getLocalizedMessage(),
				exception.getLocalizedMessage());
		logger.error(exception);
		return new ResponseEntity<>(errorModel, HttpStatus.BAD_REQUEST);
	}
}
