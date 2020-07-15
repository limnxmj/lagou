/**
 * FileName: FileUpLoadController.java
 * Author:   limn_xmj@163.com
 * Date:     2020/7/15 12:13
 * Description:
 */
package cn.xmj.controller;

import cn.xmj.bean.UpLoadResult;
import cn.xmj.service.FileUpLoadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/oss")
public class FileUpLoadController {

    @Autowired
    private FileUpLoadService fileUpLoadService;

    @PostMapping("/upload")
    @ResponseBody
    public UpLoadResult upload(MultipartFile file) {
        return fileUpLoadService.upload(file);
    }

    @RequestMapping("/download")
    public void download(HttpServletResponse response, String objectName) {
        try {
            fileUpLoadService.download(response.getOutputStream(), objectName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/delete")
    @ResponseBody
    public UpLoadResult delete(String objectName) {
        return fileUpLoadService.delete(objectName);
    }
}
