package com.backend.model.form;

import java.io.Serializable;
import lombok.Data;

@Data
public class LoginForm implements Serializable {
    private static final long serialVersionUID = 1L;

    private String loginName;
    private String passwd;
    private String kaptcha;
    private Boolean rememberMe;

}
