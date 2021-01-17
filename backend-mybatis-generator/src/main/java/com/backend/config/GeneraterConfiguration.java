package com.backend.config;

import lombok.Data;

@Data
public class GeneraterConfiguration extends BaseConfiguration  {

    private static final long serialVersionUID = 7269284763961591955L;

    private static final String CUSTOMER_CONFIG_LOCATION = "E:/temp/mybatis-generater/mybatis-generater.properties";

    private String entityPrefix;
    private String entitySuffix;





}
