package com.backend.converter.example;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
public class UserDO implements Serializable {
    private static final long serialVersionUID = 4461790713574300294L;

    private String id;
    private String userName;
    private Integer age;
    private Date birthday;

    private Date endDay;

}
