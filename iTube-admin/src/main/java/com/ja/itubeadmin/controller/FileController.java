package com.ja.itubeadmin.controller;

import com.ja.itubecommon.entity.config.AppConfig;
import com.ja.itubecommon.entity.constants.Constants;
import com.ja.itubecommon.entity.enums.DateTimePatternEnum;
import com.ja.itubecommon.entity.enums.ResponseCodeEnum;
import com.ja.itubecommon.entity.vo.ResponseVO;
import com.ja.itubecommon.exception.BusinessException;
import com.ja.itubecommon.utils.DateUtils;
import com.ja.itubecommon.utils.FFmpegUtils;
import com.ja.itubecommon.utils.StringTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

@RestController
@RequestMapping("file")
@Validated
@Slf4j
public class FileController extends BaseController{
    @Resource
    private AppConfig appConfig;

    @Resource
    private FFmpegUtils ffmpegUtils;

    /**
     * 上传图片
     * @param file 图片文件
     * @param createThumbnail 是否生成缩略图
     */
    @RequestMapping("/uploadImage")
    public ResponseVO uploadImage(@NotNull MultipartFile file,
                                  @NotNull Boolean createThumbnail) throws Exception {
        String month = DateUtils.format(new Date(), DateTimePatternEnum.YYYYMM.getPattern());   // 当前月份作为文件路径
        String folder = appConfig.getProjectFolder() + Constants.FILE_FOLDER + Constants.FILE_FOLDER_COVER + month;
        File folderFile = new File(folder);
        if(!folderFile.exists()) {
            folderFile.mkdirs();
        }
        String fileName = file.getOriginalFilename();
//        String fileNameSuffix = fileName.substring(fileName.lastIndexOf("."));
        String processedFileName = StringTools.getRandomString(30) + fileName;
        String filePath = folder + "/" + processedFileName;
        file.transferTo(new File(filePath));    // 将文件保存到指定路径
        if(createThumbnail) {
            // 生成缩略图
            ffmpegUtils.createImageThumbnail(filePath);
        }
        return getSuccessResponseVO(Constants.FILE_FOLDER_COVER + month + "/" + processedFileName);
    }

    /**
     * 获取图片
     * @param fileName 文件名 示例：cover/2026/0410/xxxx.jpeg
     */
    @RequestMapping("/getImage")
    public void getImage(HttpServletResponse response, @NotNull String fileName) throws Exception {
        if(!StringTools.pathValidate(fileName)) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        response.setContentType("image/" + suffix.replace(".", ""));
        response.setHeader("Cache-Control", "max-age=2592000");
        readFile(response, fileName);
    }

    protected void readFile(HttpServletResponse response, String filePath) {
        File file = new File(appConfig.getProjectFolder() + Constants.FILE_FOLDER + filePath);
        if(file.exists()) return;
        try (OutputStream out = response.getOutputStream();
             FileInputStream in = new FileInputStream(file)) {
            byte[] byteData = new byte[1024];
            int len = 0;
            while((len = in.read(byteData)) != -1) {
                out.write(byteData, 0, len);
            }
            out.flush();
        } catch (IOException e) {
            log.error("读取文件失败", e);
        }
    }
}
