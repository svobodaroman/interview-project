package com.wh.interview.service;

import com.wh.interview.controller.api.CreateMatchRequest;
import com.wh.interview.controller.api.UpdateScoreRequest;
import com.wh.interview.dto.MatchScoreDto;

import java.util.UUID;

public interface ScoreService {
    String createNewMatch(CreateMatchRequest createMatchRequest);

    MatchScoreDto getLastMatchScore(UUID matchUuid);

    void updateScore(UUID matchUuid, UpdateScoreRequest updateScoreRequest);
}
