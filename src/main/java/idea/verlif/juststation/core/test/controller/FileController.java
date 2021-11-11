package idea.verlif.juststation.core.test.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import idea.verlif.juststation.core.base.result.BaseResult;
import idea.verlif.juststation.global.file.FileCart;
import idea.verlif.juststation.global.file.FileInfo;
import idea.verlif.juststation.global.file.FileQuery;
import idea.verlif.juststation.global.file.SystemFileService;
import idea.verlif.juststation.global.util.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/11 17:31
 */
@RestController
@RequestMapping("/file")
@Api(tags = "文件管理")
public class FileController {

    @Autowired
    private SystemFileService fileService;

    @PostMapping
    @Operation(summary = "上传文件")
    public BaseResult<?> uploadFile(MultipartFile file) {
        return fileService.uploadFile(FileCart.TEST, SecurityUtils.getUsername(), file);
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
    public BaseResult<IPage<FileInfo>> getFileList() {
        return fileService.getFileList(FileCart.TEST, SecurityUtils.getUsername(), new FileQuery());
    }
}
