package com.qf.springdruid.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDO {

    private long id;

    private String username;

    private String phone;

    private LocalDateTime createTime;

    private int status;

}
