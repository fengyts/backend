package com.backend.converter.example;

import java.io.Serializable;
import lombok.Data;

@Data
public class UserDto implements Serializable {

    private static final long serialVersionUID = 2325932966244404528L;

    private String id;
    private String name;
    private Integer age;
    private String birthday;

    private String endDay;


}
