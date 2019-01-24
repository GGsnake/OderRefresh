package com.neo.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
@ToString
@Setter
@Getter
public class ScanLog {
    private Integer id;
    private Date lastTime ;
    private String devName;
    private Integer src;
}
