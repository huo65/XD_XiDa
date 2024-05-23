package net.eight.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.eight.model.domain.Config;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author  
 * @description 针对表【config】的数据库操作Service
 * @createDate 2024-04-08 11:53:41
 */
public interface ConfigService extends IService<Config> {

    String getNoticeTest();

    List<String> getSwiperImgs();

    void updateNoticeText(String text);

    void uploadImages(MultipartFile file);

    void removeUrl(String url);
}
