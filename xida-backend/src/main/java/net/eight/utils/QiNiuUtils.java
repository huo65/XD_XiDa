package net.eight.utils;


import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import net.eight.common.ErrorCode;
import net.eight.exception.BusinessException;
import net.eight.properties.QiNiuProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.ByteArrayInputStream;

/**
 * 七牛云工具
 *
 * @author
 * @date 2024/04/22
 */

@Component
public class QiNiuUtils {

    private static QiNiuProperties qiNiuProperties;

    @Resource
    private QiNiuProperties tempProperties;

    /**
     * 上载
     *
     * @param uploadBytes 上载字节
     * @return {@link String}
     */
    public static String upload(byte[] uploadBytes) {
        Configuration cfg = new Configuration(Region.autoRegion());
        UploadManager uploadManager = new UploadManager(cfg);

        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(uploadBytes);
        Auth auth = Auth.create(qiNiuProperties.getAccessKey(), qiNiuProperties.getSecretKey());
        String upToken = auth.uploadToken(qiNiuProperties.getBucket());
        try {
            Response response = uploadManager.put(byteInputStream, null, upToken, null, null);
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            return putRet.key;
        } catch (QiniuException ex) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "头像上传失败");
        }
    }

    /**
     * init属性
     */
    @PostConstruct
    public void initProperties() {
        qiNiuProperties = tempProperties;
    }
}
