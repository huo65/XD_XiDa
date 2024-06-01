package net.eight.model.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * 团队封面更新请求
 *
 * @author  
 * @date 2024/01/25
 */
@Data
public class TeamCoverUpdateRequest {
    private Long id;
    private MultipartFile file;
}
