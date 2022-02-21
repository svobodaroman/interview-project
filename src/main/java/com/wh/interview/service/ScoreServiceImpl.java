package com.wh.interview.service;

import com.wh.interview.controller.api.CreateMatchRequest;
import com.wh.interview.controller.api.UpdateScoreRequest;
import com.wh.interview.dto.MatchScoreDto;
import com.wh.interview.entity.MatchScoreEntity;
import com.wh.interview.exception.MatchNotFoundException;
import com.wh.interview.notification.NotificationDispatcher;
import com.wh.interview.repository.MatchScoreRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

import static com.wh.interview.repository.MatchScoreRepository.DEFAULT_SORTING;

@Slf4j
@Service
public class ScoreServiceImpl implements ScoreService {

    public static final String NO_MATCH_WITH_UUID_FOUND = "No match with UUID {} found.";
    private final MatchScoreRepository matchScoreRepository;
    private final NotificationDispatcher notificationDispatcher;

    @Autowired
    public ScoreServiceImpl(MatchScoreRepository matchScoreRepository, NotificationDispatcher notificationDispatcher) {
        this.matchScoreRepository = matchScoreRepository;
        this.notificationDispatcher = notificationDispatcher;
    }

    @Override
    public String createNewMatch(CreateMatchRequest createMatchRequest) {
        MatchScoreEntity matchScore = MatchScoreEntity.builder()
                .matchUuid(String.valueOf(UUID.randomUUID()))
                .scoreA(createMatchRequest.getScoreA())
                .scoreB(createMatchRequest.getScoreB())
                .matchName(createMatchRequest.getMatchName())
                .scoreTime(createMatchRequest.getScoreTime())
                .build();
        MatchScoreDto result = matchScoreRepository.save(matchScore).asDto();
        notificationDispatcher.dispatchMessage(result.toString());
        return result.getMatchUuid().toString();
    }

    @Override
    public MatchScoreDto getLastMatchScore(UUID matchUuid) {
        return Optional.ofNullable(matchScoreRepository.findFirstByMatchUuid(String.valueOf(matchUuid), DEFAULT_SORTING))
                .orElseThrow(() -> {
                    log.error(NO_MATCH_WITH_UUID_FOUND, matchUuid);
                    throw new MatchNotFoundException(NO_MATCH_WITH_UUID_FOUND, matchUuid);
                }).asDto();
    }

    @Transactional
    @Override
    public void updateScore(UUID matchUuid, UpdateScoreRequest updateScoreRequest) {
        MatchScoreEntity lastScore = Optional.ofNullable(matchScoreRepository.findFirstByMatchUuid(String.valueOf(matchUuid), DEFAULT_SORTING))
                .orElseThrow(() -> {
                    log.error(NO_MATCH_WITH_UUID_FOUND, matchUuid);
                    throw new MatchNotFoundException(NO_MATCH_WITH_UUID_FOUND, matchUuid);
                });

        if (scoreIsBiggerOrMoreRecent(lastScore, updateScoreRequest)) {
            MatchScoreEntity newMatchScore = lastScore
                    .scoreA(updateScoreRequest.getScoreA())
                    .scoreB(updateScoreRequest.getScoreB())
                    .scoreTime(updateScoreRequest.getScoreTime())
                    .matchName(lastScore.matchName())
                    .matchUuid(lastScore.matchUuid());
            MatchScoreDto result = matchScoreRepository.save(newMatchScore).asDto();
            notificationDispatcher.dispatchMessage(result.toString());
        }
    }

    private boolean scoreIsBiggerOrMoreRecent(MatchScoreEntity lastScore, UpdateScoreRequest updateScoreRequest) {
        return lastScore.scoreA() < updateScoreRequest.getScoreA()
                || lastScore.scoreB() < updateScoreRequest.getScoreB()
                || lastScore.scoreTime().isBefore(updateScoreRequest.getScoreTime());
    }

}
