package com.backend.model.dto.flowable;

import com.backend.model.dto.CommonDto;
import lombok.Data;

/**
 * @author DELL
 */
@Data
public class ProcessDefinitionDto extends CommonDto {
    private static final long serialVersionUID = 1L;

    private String id;
    private String deploymentId;
    private String category;
    private String name;
    private String key;
    private String description;
    private Integer version;
    private String resourceName;
    private String diagramResourceName;
    private Boolean hasStartFormKey;
    private Boolean hasGraphicalNotation;
    private Boolean isSuspended;
    private String tenantId;
    private String derivedFrom;
    private String derivedFromRoot;
    private Integer derivedVersion;
    private String engineVersion;

}
