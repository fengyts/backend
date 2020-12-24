package com.backend.mybatis.entity;

import java.io.Serializable;
import lombok.Data;

@Data
public class MulitiDb implements Serializable {

    private static final long serialVersionUID = -2603278344266455860L;

    private Long id;

    private String name;


}
