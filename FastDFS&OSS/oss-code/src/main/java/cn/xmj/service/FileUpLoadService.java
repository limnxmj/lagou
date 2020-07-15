/**
 * FileName: FileUpLoadService.java
 * Author:   limn_xmj@163.com
 * Date:     2020/7/15 11:52
 * Description:
 */
package cn.xmj.service;

import cn.xmj.bean.UpLoadResult;
import cn.xmj.config.AliyunConfig;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.UUID;

@Service
public class FileUpLoadService {

    @Autowired
    private AliyunConfig aliyunConfig;

    @Autowired
    private OSSClient ossClient;

    private static final String[] IMAGE_TYPE = new String[]{"jpg", "png", "jpeg"};

    public UpLoadResult upload(MultipartFile multipartFile) {


        //类型检查：必须为 jpg、png 或者 jpeg类型，其它图片类型返回错误提示信息
        String fileName = multipartFile.getOriginalFilename();
        if (!isLegalImageType(fileName)) {
            return errorUpLoadResult("图片格式必须为jpg、png或者jpeg类型");
        }

        //大小检查：必须小于5M，大于5M时返回错误提示信息
        if (multipartFile.getSize() > 5 * 1024 * 1024) {
            return errorUpLoadResult("图片必须小于5M");
        }

        //图片名称：生成的文件名，必须保持唯一
        String filePath = getFilePath(fileName);

        //上传
        try {
            ossClient.putObject(aliyunConfig.getBucketName(), filePath, new ByteArrayInputStream(multipartFile.getBytes()));
        } catch (IOException e) {
            return errorUpLoadResult("上传失败");
        }

        UpLoadResult upLoadResult = new UpLoadResult();
        upLoadResult.setStatus("success");
        upLoadResult.setName(aliyunConfig.getUrlPrefix() + filePath);
        upLoadResult.setUid(filePath);
        upLoadResult.setResponse("上传成功");
        return upLoadResult;
    }

    /**
     * 生成不重复的文件路径和文件名
     */
    private String getFilePath(String sourceFileName) {
        DateTime dateTime = new DateTime();
        return "images/" + dateTime.toString("yyyy")
                + "/" + dateTime.toString("MM") + "/"
                + dateTime.toString("dd") + "/" + UUID.randomUUID().toString() + "." +
                StringUtils.substringAfterLast(sourceFileName, ".");
    }

    private UpLoadResult errorUpLoadResult(String msg) {
        UpLoadResult upLoadResult = new UpLoadResult();
        upLoadResult.setStatus("error");
        upLoadResult.setResponse(msg);
        return upLoadResult;
    }

    private boolean isLegalImageType(String originalFilename) {

        for (String type : IMAGE_TYPE) {
            if (StringUtils.endsWithIgnoreCase(originalFilename, type)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 发送下载请求 /oss/download，实现图片下载
     */
    public void download(OutputStream os, String objectName) {
        OSSObject ossObject = ossClient.getObject(aliyunConfig.getBucketName(), objectName);
        // 读取文件内容。
        BufferedInputStream in = new BufferedInputStream(ossObject.getObjectContent());
        BufferedOutputStream out = new BufferedOutputStream(os);
        byte[] buffer = new byte[1024];
        int lenght = 0;
        try {
            while ((lenght = in.read(buffer)) != -1) {
                out.write(buffer, 0, lenght);
            }
            if (out != null) {
                out.flush();
                out.close();
            }
            if (in != null) {
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送删除请求/oss/delete，实现图片删除
     */
    public UpLoadResult delete(String objectName) {

        // 根据BucketName,objectName删除文件
        ossClient.deleteObject(aliyunConfig.getBucketName(), objectName);
        UpLoadResult upLoadResult = new UpLoadResult();
        upLoadResult.setName(objectName);
        upLoadResult.setStatus("success");
        upLoadResult.setResponse("删除成功");
        return upLoadResult;
    }
}
