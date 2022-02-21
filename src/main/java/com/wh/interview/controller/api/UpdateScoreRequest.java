package com.wh.interview.controller.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Value;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Value
@Builder
public class UpdateScoreRequest {

    int scoreA;
    int scoreB;
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime scoreTime;
}
