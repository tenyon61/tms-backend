package com.cqbo.web.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.RandomUtil;
import com.cqbo.web.common.BaseResponse;
import com.cqbo.web.common.ResultUtils;
import com.cqbo.web.config.MinioClientConfig;
import com.cqbo.web.constant.FileConstant;
import com.cqbo.web.exception.BusinessException;
import com.cqbo.web.exception.ErrorCode;
import com.cqbo.web.manager.MinioManager;
import com.cqbo.web.model.dto.file.UploadFileRequest;
import com.cqbo.web.model.entity.User;
import com.cqbo.web.model.enums.FileUploadBizEnum;
import com.cqbo.web.service.UserService;
import io.minio.StatObjectResponse;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * 文件接口
 */
//@RestController
//@RequestMapping("/file")
public class FileController {
    Logger logger = LoggerFactory.getLogger(FileController.class);

    @Resource
    private MinioManager minioManager;

    @Resource
    private MinioClientConfig minioClientConfig;

    @Resource
    private UserService userService;

    /**
     * 文件上传
     *
     * @param multipartFile
     * @param uploadFileRequest
     * @return
     */
    @PostMapping("/upload")
    public BaseResponse<String> uploadFile(@RequestPart("file") MultipartFile multipartFile, UploadFileRequest uploadFileRequest) {
        String biz = uploadFileRequest.getBiz();
        FileUploadBizEnum fileUploadBizEnum = FileUploadBizEnum.getEnumByValue(biz);
        if (fileUploadBizEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        validFile(multipartFile, fileUploadBizEnum);
        User loginUser = userService.getLoginUser();
        // 文件目录：根据业务、用户来划分
        String uuid = RandomUtil.randomString(8);
        String filename = uuid + "-" + multipartFile.getOriginalFilename();
        String filepath = String.format("/%s/%s/%s", fileUploadBizEnum.getValue(), loginUser.getId(), filename);
        File file = null;
        try {
            // 上传文件
            file = File.createTempFile(filepath, null);
            multipartFile.transferTo(file);
            minioManager.uploadObject(filepath, file);
            // 返回可访问地址
            return ResultUtils.success(FileConstant.MINIO_HOST + "/" + minioClientConfig.getBucket() + filepath);
        } catch (Exception e) {
            logger.error("file upload error, filepath = " + filepath, e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "minio:上传失败");
        } finally {
            if (file != null) {
                // 删除临时文件
                boolean delete = file.delete();
                if (!delete) {
                    logger.error("file delete error, filepath = {}", filepath);
                }
            }
        }
    }

    /**
     * 列表
     *
     * @return
     */
    @GetMapping("/listObjs")
    public BaseResponse<List<String>> listObjs() {
        try {
            List<String> strings = minioManager.listObjects();
            return ResultUtils.success(strings);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "minio:获取列表失败");
        }
    }

    /**
     * 删除
     *
     * @param filename
     * @return
     */
    @PutMapping("/delete")
    public BaseResponse<Boolean> delete(@RequestParam String filename) {
        try {
            minioManager.deleteObject(filename);
            return ResultUtils.success(true);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "minio:删除对象失败");
        }
    }

    /**
     * 下载minio服务的文件
     *
     * @param filename
     * @param response
     */
    @GetMapping("/download")
    public void download(@RequestParam String filename, HttpServletResponse response) {
        try {
            InputStream fileInputStream = minioManager.getObject(filename);
            // todo 完善文件命名逻辑
            String newFileName = System.currentTimeMillis() + "." + FileUtil.getSuffix(filename);
            response.setHeader("Content-Disposition", "attachment;filename=" + newFileName);
            response.setContentType("application/force-download");
            response.setCharacterEncoding("UTF-8");
            IoUtil.copy(fileInputStream, response.getOutputStream());
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "minio:下载失败");
        }
    }

    /**
     * 获取文件信息
     *
     * @param filename
     * @param response
     */
    @GetMapping("/statObj")
    public BaseResponse<String> statObj(@RequestParam String filename, HttpServletResponse response) {
        try {
            StatObjectResponse statObjectResponse = minioManager.statObject(filename);
            return ResultUtils.success(statObjectResponse.toString());
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "minio:获取信息失败");
        }
    }

    /**
     * 获取minio文件的下载地址
     *
     * @param filename
     * @return
     */
    @GetMapping("/getHttpUrl")
    public BaseResponse<String> getHttpUrl(@RequestParam String filename) {
        try {
            String url = minioManager.getObjectUrl(filename);
            return ResultUtils.success(url);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "minio:文件的下载地址获取");
        }
    }


    /**
     * 校验文件
     *
     * @param multipartFile
     * @param fileUploadBizEnum 业务类型
     */
    private void validFile(MultipartFile multipartFile, FileUploadBizEnum fileUploadBizEnum) {
        // 文件大小
        long fileSize = multipartFile.getSize();
        // 文件后缀
        String fileSuffix = FileUtil.getSuffix(multipartFile.getOriginalFilename());
        final long ONE_M = 1024 * 1024L;
        if (FileUploadBizEnum.USER_AVATAR.equals(fileUploadBizEnum)) {
            if (fileSize > ONE_M) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件大小不能超过 1M");
            }
            if (!Arrays.asList("jpeg", "jpg", "svg", "png", "webp").contains(fileSuffix)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件类型错误");
            }
        }
    }
}
