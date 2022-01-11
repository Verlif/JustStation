package idea.verlif.justdemo.core.file;

import com.baomidou.mybatisplus.core.metadata.IPage;
import idea.verlif.justdemo.core.base.biz.UserBiz;
import idea.verlif.justdemo.core.base.domain.User;
import idea.verlif.juststation.global.base.result.BaseResult;
import idea.verlif.juststation.global.base.result.ResultCode;
import idea.verlif.juststation.global.base.result.ext.FailResult;
import idea.verlif.juststation.global.base.result.ext.OkResult;
import idea.verlif.juststation.global.file.FileService;
import idea.verlif.juststation.global.file.FileType;
import idea.verlif.juststation.global.file.domain.FileCart;
import idea.verlif.juststation.global.file.parser.FileParser4List;
import idea.verlif.juststation.global.file.parser.FileParserService;
import idea.verlif.juststation.global.util.PrintUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;

/**
 * 文件导入导出测试接口
 *
 * @author Verlif
 * @version 1.0
 * @date 2021/12/10 11:33
 */
@RestController
@RequestMapping("/public/excel")
@Api(tags = "导入导出测试")
public class ExcelController {

    @Autowired
    private FileService fileService;

    @Autowired
    private FileParserService parserService;

    @Autowired
    private UserBiz userBiz;

    @GetMapping
    public BaseResult<?> exportUser(HttpServletResponse response) {
        // 获取所有用户信息
        BaseResult<IPage<User>> page = userBiz.getPage(null);
        List<User> list = page.getData().getRecords();
        FileParser4List parser = parserService.getListParser(FileType.EXCEL);
        File file = new File(fileService.getLocalFile(FileCart.TEST, null), "test.xlsx");
        try {
            if (parser.saveObjectList(list, file, User.class) == list.size()) {
                return fileService.downloadFile(response, FileCart.TEST, null, file.getName());
            } else {
                return new FailResult<>("导出数据有误");
            }
        } catch (Exception e) {
            PrintUtils.print(e);
            return FailResult.empty();
        } finally {
            if (file.exists()) {
                file.delete();
            }
        }
    }

    @PostMapping
    public BaseResult<?> importUser(MultipartFile file) throws Exception {
        if (file == null) {
            return new BaseResult<>(ResultCode.FAILURE_PARAMETER_LACK).withParam("file");
        }
        FileParser4List parser = parserService.getListParser(FileType.fromFilename(file.getOriginalFilename()));
        if (parser == null) {
            return new FailResult<>("不支持的文件格式");
        }
        List<User> list = parser.getObjectList(file.getInputStream(), User.class);
        return new OkResult<>().appendMsg("，已解析文件 - " + file.getResource().getFilename()).data(list);
    }
}
