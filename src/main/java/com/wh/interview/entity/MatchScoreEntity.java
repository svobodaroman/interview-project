package com.wh.interview.entity;

import com.wh.interview.dto.MatchScoreDto;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Accessors(fluent = true)
public class MatchScoreEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String matchUuid;

    @Column(nullable = false)
    private String matchName;

    @Column(nullable = false)
    private int scoreA = 0;

    @Column(nullable = false)
    private int scoreB = 0;

    @Column(nullable = false)
    private LocalDateTime scoreTime;

    public MatchScoreDto asDto() {
        return MatchScoreDto.builder()
                .matchUuid(UUID.fromString(matchUuid))
                .matchName(matchName)
                .scoreA(scoreA)
                .scoreB(scoreB)
                .scoreTime(scoreTime)
                .build();
    }
}
