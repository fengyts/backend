package com.backend.model.dto.flowable;

import com.backend.model.dto.CommonDto;
import lombok.Data;
import org.flowable.common.engine.api.repository.EngineResource;

import java.util.Date;
import java.util.Map;

/**
 * @author DELL
 */
@Data
public class DeploymentDto extends CommonDto {
    private static final long serialVersionUID = 1L;

    private String parentDeploymentId;
    private String id;
    private String name;
    private Date deploymentTime;
    private String category;
    private String key;
    private String derivedFrom;
    private String derivedFromRoot;
    private String tenantId;
    private String engineVersion;
    private Boolean isNew;
    private Map<String, EngineResource> resources;

}