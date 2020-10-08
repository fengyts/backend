package com.backend.model.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * @author fengyts
 */
@Data
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String createUserId;
    private LocalDateTime createTime;
    private String modifyUserId;
    private LocalDateTime modifyTime;

}
