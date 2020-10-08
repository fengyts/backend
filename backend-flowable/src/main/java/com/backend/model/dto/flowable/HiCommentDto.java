package com.backend.model.dto.flowable;

import java.util.Date;
import lombok.Data;
import org.flowable.engine.impl.persistence.entity.CommentEntityImpl;

@Data
public class HiCommentDto extends CommentEntityImpl {
    private static final long serialVersionUID = 1L;

    /**
     * 任务id
     */
    protected String taskId;
    /**
     * 添加人
     */
    protected String userId;
    /**
     * 用户的名称
     */
    protected String userName;
    /**
     * 用户的头像链接
     */
    protected String userUrl;
    /**
     * 流程实例id
     */
    protected String processInstanceId;
    /**
     * 意见信息
     */
    protected String message;
    /**
     * 时间
     */
    protected Date time;
    /**
     * @see com.backend.enums.CommentTypeEnum
     */
    private String type;
    /**
     * 类型名称
     */
    private String typeName;
    /**
     * 任务名称
     */
    private String taskName;
    /**
     * 评论全信息
     */
    private String fullMsg;

}
