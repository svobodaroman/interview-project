package com.wh.interview.controller.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Value;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static com.wh.interview.GeneralAppConfig.ISO_LOCAL_DATE_TIME;

@Value
@Builder
public class MatchScoreResponse {

    String matchName;
    int scoreA;
    int scoreB;
    @JsonFormat(pattern = ISO_LOCAL_DATE_TIME)
    @DateTimeFormat(pattern = ISO_LOCAL_DATE_TIME)
    LocalDateTime scoreTime;
}
