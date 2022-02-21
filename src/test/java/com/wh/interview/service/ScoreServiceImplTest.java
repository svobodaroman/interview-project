package com.wh.interview.service;


import com.wh.interview.Callback;
import com.wh.interview.controller.api.CreateMatchRequest;
import com.wh.interview.controller.api.UpdateScoreRequest;
import com.wh.interview.dto.MatchScoreDto;
import com.wh.interview.entity.MatchScoreEntity;
import com.wh.interview.exception.MatchNotFoundException;
import com.wh.interview.notification.NotificationDispatcher;
import com.wh.interview.repository.MatchScoreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.wh.interview.repository.MatchScoreRepository.DEFAULT_SORTING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ScoreServiceImplTest {

    @Mock
    MatchScoreRepository repositoryMock;

    @Mock
    NotificationDispatcher dispatcherMock;

    @InjectMocks
    ScoreServiceImpl scoreService;

    @Test
    public void updateScore_missInDb_throwException() {
        when(repositoryMock.findFirstByMatchUuid(any(), any())).thenReturn(null);
        UpdateScoreRequest request = UpdateScoreRequest.builder().build();

        try {
            scoreService.updateScore(UUID.randomUUID(), request);
            fail("It should throw exception");
        } catch (Exception e) {
            assertThat(e)
                    .isInstanceOf(MatchNotFoundException.class);
        }
    }

    @Test
    public void updateScore_happy() {
        UUID uuid = UUID.randomUUID();
        String matchName = "A test B";
        MatchScoreEntity matchMock = MatchScoreEntity.builder().matchUuid(uuid.toString()).matchName(matchName).build();
        when(repositoryMock.findFirstByMatchUuid(any(), any())).thenReturn(matchMock);
        when(repositoryMock.save(any())).thenReturn(matchMock);
        LocalDateTime mockTime = LocalDateTime.now();
        UpdateScoreRequest request = UpdateScoreRequest.builder().scoreA(1).scoreB(2).scoreTime(mockTime).build();


        MatchScoreDto result = tryCatchWithoutException(() -> scoreService.updateScore(uuid, request));


        MatchScoreDto expected = MatchScoreDto.builder().matchUuid(uuid).matchName(matchName).scoreTime(mockTime).scoreA(1).scoreB(2).build();
        assertEquals(expected, result);
        verify(repositoryMock).findFirstByMatchUuid(String.valueOf(uuid), DEFAULT_SORTING);
        ArgumentCaptor<MatchScoreEntity> matchScoreArgumentCaptor = ArgumentCaptor.forClass(MatchScoreEntity.class);
        verify(repositoryMock).save(matchScoreArgumentCaptor.capture());
        MatchScoreEntity actual = matchScoreArgumentCaptor.getValue();
        assertThat(actual.matchName()).isEqualTo(matchMock.matchName());
        assertThat(actual.scoreA()).isEqualTo(matchMock.scoreA());
        assertThat(actual.scoreB()).isEqualTo(matchMock.scoreB());
        assertThat(actual.matchUuid()).isEqualTo(matchMock.matchUuid());
        assertThat(actual.scoreTime()).isEqualTo(matchMock.scoreTime());
        assertThat(actual.id()).isEqualTo(matchMock.id());
        verify(dispatcherMock).dispatchMessage(eq(matchMock.asDto().toString()));
    }

    @Test
    public void createScore_happy() {
        UUID uuid = UUID.randomUUID();
        String matchName = "A test B";
        LocalDateTime mockTime = LocalDateTime.now();
        MatchScoreEntity matchMock = MatchScoreEntity.builder().matchUuid(uuid.toString()).matchName(matchName).scoreTime(mockTime).scoreA(1).scoreB(2).build();
        when(repositoryMock.save(any())).thenReturn(matchMock);
        CreateMatchRequest request = CreateMatchRequest.builder().matchName(matchName).scoreA(1).scoreB(2).scoreTime(mockTime).build();


        String result = tryCatchWithoutException(() -> scoreService.createNewMatch(request));


        assertEquals(uuid.toString(), result);
        ArgumentCaptor<MatchScoreEntity> matchScoreArgumentCaptor = ArgumentCaptor.forClass(MatchScoreEntity.class);
        verify(repositoryMock).save(matchScoreArgumentCaptor.capture());
        assertEquals(matchName, matchScoreArgumentCaptor.getValue().matchName());
        assertEquals(1, matchScoreArgumentCaptor.getValue().scoreA());
        assertEquals(2, matchScoreArgumentCaptor.getValue().scoreB());
        assertEquals(mockTime, matchScoreArgumentCaptor.getValue().scoreTime());
        verify(dispatcherMock).dispatchMessage(eq(matchMock.asDto().toString()));
    }

    private <T> T tryCatchWithoutException(Callback callback) {
        try {
            return (T) callback.callback();
        } catch (Exception e) {
            fail("It should not throw exception");
            return null;
        }
    }

    @Test
    public void getLastMatchScore_happy() {
        UUID uuid = UUID.randomUUID();
        String matchName = "A test B";
        LocalDateTime mockTime = LocalDateTime.now();
        MatchScoreEntity matchMock = MatchScoreEntity.builder().matchUuid(uuid.toString()).matchName(matchName).scoreTime(mockTime).scoreA(1).scoreB(2).build();
        when(repositoryMock.findFirstByMatchUuid(uuid.toString(), DEFAULT_SORTING)).thenReturn(matchMock);


        MatchScoreDto result = tryCatchWithoutException(() -> scoreService.getLastMatchScore(uuid));


        MatchScoreDto expected = MatchScoreDto.builder().matchUuid(uuid).matchName(matchName).scoreTime(mockTime).scoreA(1).scoreB(2).build();
        assertEquals(expected, result);

    }
}
