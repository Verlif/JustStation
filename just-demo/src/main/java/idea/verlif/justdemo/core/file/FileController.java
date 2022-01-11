package idea.verlif.justdemo.core.file;

import idea.verlif.juststation.global.base.domain.SimPage;
import idea.verlif.juststation.global.base.result.BaseResult;
import idea.verlif.juststation.global.file.FileService;
import idea.verlif.juststation.global.file.domain.FileCart;
import idea.verlif.juststation.global.file.domain.FileInfo;
import idea.verlif.juststation.global.file.domain.FileQuery;
import idea.verlif.juststation.global.util.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

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
        return fileService.uploadFile(FileCart.TEST, SecurityUtils.getUsername(), files);
    }

    @GetMapping
    @Operation(summary = "下载文件")
    public BaseResult<?> downloadFile(
            @RequestParam String fileName,
            HttpServletResponse response) {
        return fileService.downloadFile(response, FileCart.TEST, SecurityUtils.getUsername(), fileName);
    }

    @GetMapping("/list")
    @Operation(summary = "文件列表")
    public BaseResult<SimPage<FileInfo>> getFileList(FileQuery fileQuery) {
        return fileService.getFileList(FileCart.TEST, SecurityUtils.getUsername(), fileQuery);
    }

    @DeleteMapping
    @Operation(summary = "删除文件")
    public BaseResult<?> deleteFile(@RequestParam String filename) {
        return fileService.deleteFile(FileCart.TEST, SecurityUtils.getUsername(), filename);
    }
}
