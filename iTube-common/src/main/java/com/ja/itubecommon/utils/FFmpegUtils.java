package com.ja.itubecommon.utils;

import com.ja.itubecommon.entity.config.AppConfig;
import com.ja.itubecommon.entity.constants.Constants;
import com.ja.itubecommon.exception.BusinessException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class FFmpegUtils {
    @Resource
    private AppConfig appConfig;

    public void createImageThumbnail(String filePath) throws BusinessException {
        String command = "ffmpeg -i \"%s\" -vf scale=200:-1 \"%s\"";
        command = String.format(command, filePath, filePath + Constants.IMAGE_THUMBNAIL_SUFFIX);
        ProcessUtils.executeCommand(command, appConfig.getShowFFmpegLog());
    }
}
