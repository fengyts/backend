package com.backend.config;

import java.io.Serializable;
import lombok.Data;

@Data
public abstract class BaseConfiguration implements Serializable {

    protected String basePackage;

    protected String controllerPackage;
    protected String entityPackage;
    protected String servicePackage;
    protected String serviceImplPackage;
    protected String daoPackage;
    protected String mybatisMapperPackage;

    protected String baseControllerFullClass;
    protected String baseEntityFullClass;
    protected String baseServiceFullClass;
    protected String baseServiceImplFullClass;
    protected String baseDaoClass;


}
