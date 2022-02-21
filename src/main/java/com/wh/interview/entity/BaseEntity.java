package com.wh.interview.entity;

import javax.persistence.Version;

public class BaseEntity {

    @Version
    private Long version;

}
