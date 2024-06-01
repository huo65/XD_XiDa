package net.eight.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 团队加入请求
 *
 * @author  
 * @date 2024/04/22
 */
@Data
@ApiModel(value = "加入队伍请求")
public class TeamJoinRequest implements Serializable {
    private static final long serialVersionUID = -3755024144750907374L;
    /**
     * id
     */
    @ApiModelProperty(value = "队伍id", required = true)
    private Long teamId;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    private String password;
}
