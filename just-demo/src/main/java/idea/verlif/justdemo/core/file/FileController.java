package idea.verlif.justdemo.core.file;

import idea.verlif.spring.file.FileService;
import idea.verlif.spring.file.domain.FileCart;
import idea.verlif.spring.file.domain.FilePage;
import idea.verlif.spring.file.domain.FileQuery;
import idea.verlif.juststation.global.base.result.BaseResult;
import idea.verlif.juststation.global.base.result.ext.FailResult;
import idea.verlif.juststation.global.base.result.ext.OkResult;
import idea.verlif.juststation.global.util.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 文件上传下载测试接口
 *
 * @author Verlif
 * @version 1.0
 * @date 2021/11/11 17:31
 */
@RestController
@RequestMapping("/file")
@Api(tags = "文件上传下载测试")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping
    @Operation(summary = "上传文件")
    public BaseResult<?> uploadFile(MultipartFile[] files) {
        try {
            if (fileService.uploadFile(new FileCart("test"), SecurityUtils.getUsername(), files) > 0) {
                return OkResult.empty();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return FailResult.empty();
    }

    @GetMapping
    @Operation(summary = "下载文件")
    public BaseResult<?> downloadFile(
            @RequestParam String filename,
            HttpServletResponse response) {
        try {
            if (fileService.downloadFile(response, new FileCart("test"), SecurityUtils.getUsername(), filename)) {
                return OkResult.empty();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return FailResult.empty();
    }

    @GetMapping("/list")
    @Operation(summary = "文件列表")
    public BaseResult<FilePage> getFileList(FileQuery fileQuery) {
        return new OkResult<FilePage>().data(fileService.getFileList(new FileCart("test"), SecurityUtils.getUsername(), fileQuery));
    }

    @DeleteMapping
    @Operation(summary = "删除文件")
    public BaseResult<?> deleteFile(@RequestParam String filename) {
        if (fileService.deleteFile(new FileCart("test"), SecurityUtils.getUsername(), filename)) {
            return OkResult.empty();
        } else {
            return FailResult.empty();
        }
    }
}
