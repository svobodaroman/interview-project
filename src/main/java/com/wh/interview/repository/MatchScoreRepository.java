package com.wh.interview.repository;

import com.wh.interview.entity.MatchScoreEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchScoreRepository extends JpaRepository<MatchScoreEntity, Long> {

    Sort DEFAULT_SORTING = Sort.by(Sort.Order.desc("scoreTime"));

    MatchScoreEntity findFirstByMatchUuid(String matchUuid, Sort sort);


}
