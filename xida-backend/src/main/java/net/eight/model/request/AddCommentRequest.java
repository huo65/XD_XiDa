package net.eight.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 添加评论请求
 *
 * @author  
 * @date 2024/04/22
 */
@Data
@ApiModel(value = "添加博文评论请求")
public class AddCommentRequest implements Serializable {
    /**
     * 串行版本uid
     */
    private static final long serialVersionUID = 5733549433004941655L;
    /**
     * 博客id
     */
    @ApiModelProperty(value = "博文id")
    private Long blogId;
    /**
     * 内容
     */
    @ApiModelProperty(value = "评论")
    private String content;

}
