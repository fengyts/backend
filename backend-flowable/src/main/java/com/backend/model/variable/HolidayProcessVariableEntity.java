package com.backend.model.variable;

import com.backend.enums.StatusAuditEnum;
import com.backend.model.entity.SysUserEntity;
import java.io.Serializable;
import java.util.Map;
import lombok.Data;

/**
 * 请假流程 变量封装
 * @author fengyts
 */
@Data
public class HolidayProcessVariableEntity implements Serializable {
    private static final long serialVersionUID = 1L;

//    private Map<String, Object> applyVariables;

    private SysUserEntity reqUser;
    private SysUserEntity deptManagerUser;
    private SysUserEntity componyManagerUser;
    private Integer day;
    private String approve;
    private String rejectReason;
    private SysUserEntity rejectUser;
    private StatusAuditEnum statusAudit;


}
