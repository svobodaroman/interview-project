package com.wh.interview.controller;

import com.wh.interview.controller.api.CreateMatchRequest;
import com.wh.interview.controller.api.CreateMatchResponse;
import com.wh.interview.controller.api.MatchScoreResponse;
import com.wh.interview.controller.api.UpdateScoreRequest;
import com.wh.interview.dto.MatchScoreDto;
import com.wh.interview.service.ScoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = "/score", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class ScoreController {

    private final ScoreService scoreService;

    @Autowired
    public ScoreController(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    @Operation(summary = "Creates initial match with score", description = "Creates initial match with score", tags = {"score"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(schema = @Schema(implementation = CreateMatchResponse.class)))})
    @PostMapping("/v1")
    public CreateMatchResponse createScore(@RequestBody CreateMatchRequest createMatchRequest) {

        log.info("Creating new match score {}", createMatchRequest);
        String matchUuid = scoreService.createNewMatch(createMatchRequest);
        return new CreateMatchResponse(matchUuid);
    }

    @Operation(summary = "Updates score of match", description = "Updates score of match", tags = {"score"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation")})
    @PutMapping("/v1/{matchUuid}")
    public MatchScoreResponse setScore(@PathVariable UUID matchUuid, @RequestBody UpdateScoreRequest updateScoreRequest) {

        MatchScoreDto matchScoreDto = scoreService.updateScore(matchUuid, updateScoreRequest);
        return MatchScoreResponse.builder()
                .matchName(matchScoreDto.getMatchName())
                .scoreA(matchScoreDto.getScoreA())
                .scoreB(matchScoreDto.getScoreB())
                .scoreTime(matchScoreDto.getScoreTime())
                .build();
    }

    @Operation(summary = "Returns latest known score of match", description = "Returns latest known score of match", tags = {"score"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(schema = @Schema(implementation = MatchScoreResponse.class)))})
    @GetMapping("/v1/{matchUuid}")
    public MatchScoreResponse getScore(@PathVariable UUID matchUuid) {

        MatchScoreDto lastMatchScore = scoreService.getLastMatchScore(matchUuid);

        return MatchScoreResponse.builder()
                .matchName(lastMatchScore.getMatchName())
                .scoreA(lastMatchScore.getScoreA())
                .scoreB(lastMatchScore.getScoreB())
                .scoreTime(lastMatchScore.getScoreTime())
                .build();
    }
}
