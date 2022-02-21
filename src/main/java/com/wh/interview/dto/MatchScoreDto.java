package com.wh.interview.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Value;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.wh.interview.GeneralAppConfig.ISO_LOCAL_DATE_TIME;

@Value
@Builder
public class MatchScoreDto {
    String matchName;
    UUID matchUuid;
    int scoreA;
    int scoreB;
    @JsonFormat(pattern = ISO_LOCAL_DATE_TIME)
    @DateTimeFormat(pattern = ISO_LOCAL_DATE_TIME)
    LocalDateTime scoreTime;
}
