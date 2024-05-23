package net.eight.controller;

import io.swagger.annotations.Api;
import net.eight.common.BaseResponse;
import net.eight.common.ErrorCode;
import net.eight.common.ResultUtils;
import net.eight.exception.BusinessException;
import net.eight.model.domain.User;
import net.eight.service.ConfigService;
import net.eight.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static net.eight.constants.UserConstants.ADMIN_ROLE;

@RestController
@RequestMapping("/config")
@Api(tags = "配置管理模块")
public class ConfigController {

    @Resource
    private UserService userService;

    @Resource
    private ConfigService configService;

    @GetMapping("/notice")
    public BaseResponse<String> getNoticeTest() {
        String noticeTest = configService.getNoticeTest();
        return ResultUtils.success(noticeTest);
    }

    @GetMapping("/swiper")
    public BaseResponse<List<String>> getSwiperImgs() {
        List<String> swiperImgs = configService.getSwiperImgs();
        return ResultUtils.success(swiperImgs);
    }

    @PostMapping("/notice")
    public BaseResponse<Boolean> updateNoticeText(HttpServletRequest request, @RequestBody String text) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null || !loginUser.getRole().equals(ADMIN_ROLE)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "无权限");
        }
        configService.updateNoticeText(text);
        return ResultUtils.success(true);
    }

    @PostMapping("/upload")
    public BaseResponse<Boolean> uploadImage(HttpServletRequest request, @RequestBody MultipartFile file) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null || !loginUser.getRole().equals(ADMIN_ROLE)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "无权限");
        }
        configService.uploadImages(file);
        return ResultUtils.success(true);
    }

    @PostMapping("/remove")
    public BaseResponse<Boolean> removeImage(HttpServletRequest request, @RequestBody String url) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null || !loginUser.getRole().equals(ADMIN_ROLE)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "无权限");
        }
        configService.removeUrl(url);
        return ResultUtils.success(true);
    }
}
