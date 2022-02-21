package com.wh.interview.it;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.wh.interview.controller.ScoreController;
import com.wh.interview.controller.api.ErrorResponse;
import com.wh.interview.controller.api.MatchScoreResponse;
import com.wh.interview.dto.MatchScoreDto;
import com.wh.interview.exception.MatchNotFoundException;
import com.wh.interview.service.ScoreService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static com.wh.interview.GeneralAppConfig.ISO_LOCAL_DATE_TIME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(ScoreController.class)
public class ScoreControllerIntTest {

    private final Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class,
                    (JsonDeserializer<LocalDateTime>) (json, typeOfT, context) -> LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ofPattern(ISO_LOCAL_DATE_TIME)))
            .create();

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ScoreService scoreService;


    @Test
    public void getScoreWhenDoesNotExist_errorResponse() throws Exception {
        // given
        UUID uuid = UUID.randomUUID();
        String urlUnderTest = "/score/v1/" + uuid;
        String errorMessage = "Some message";
        given(scoreService.getLastMatchScore(uuid)).willThrow(new MatchNotFoundException(errorMessage));

        // when
        MockHttpServletResponse response = mvc.perform(get(urlUnderTest).contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        ErrorResponse errorResponse = new Gson().fromJson(response.getContentAsString(), ErrorResponse.class);
        assertThat(errorResponse.getMessage()).isEqualTo(errorMessage);
        assertThat(errorResponse.getUri()).isEqualTo(urlUnderTest);
    }

    @Test
    public void getScore_happy() throws Exception {
        // given
        UUID uuid = UUID.randomUUID();
        String urlUnderTest = "/score/v1/" + uuid;
        String matchName = "A vs B";
        LocalDateTime mockTime = LocalDateTime.now();
        MatchScoreDto scoreResult = MatchScoreDto.builder().matchUuid(uuid).matchName(matchName).scoreTime(mockTime).scoreA(1).scoreB(2).build();
        given(scoreService.getLastMatchScore(uuid)).willReturn(scoreResult);

        // when
        MockHttpServletResponse response = mvc.perform(get(urlUnderTest).contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        MatchScoreResponse responseObject = gson.fromJson(response.getContentAsString(), MatchScoreResponse.class);
        assertThat(responseObject.getMatchName()).isEqualTo(matchName);
        assertThat(responseObject.getScoreA()).isEqualTo(1);
        assertThat(responseObject.getScoreB()).isEqualTo(2);
        assertThat(responseObject.getScoreTime()).isEqualTo(mockTime.format(DateTimeFormatter.ofPattern(ISO_LOCAL_DATE_TIME)));
    }

}
