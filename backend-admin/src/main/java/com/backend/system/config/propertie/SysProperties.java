package com.backend.system.config.propertie;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "sys")
public class SysProperties {

    private boolean kaptchaOpen = false;
    private boolean smsOpen = false;


}
