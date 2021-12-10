package idea.verlif.juststation.core.test.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import idea.verlif.juststation.core.test.biz.UserBiz;
import idea.verlif.juststation.core.test.domain.User;
import idea.verlif.juststation.core.test.domain.query.UserQuery;
import idea.verlif.juststation.global.base.result.BaseResult;
import idea.verlif.juststation.global.base.result.ext.FailResult;
import idea.verlif.juststation.global.file.FileService;
import idea.verlif.juststation.global.file.FileType;
import idea.verlif.juststation.global.file.handler.FileCart;
import idea.verlif.juststation.global.file.parser.FileParser4List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/12/10 11:33
 */
@RestController
@RequestMapping("/public/excel")
public class ExcelController {

    @Autowired
    private FileService fileService;

    @Autowired
    private UserBiz userBiz;

    @GetMapping("/download")
    public BaseResult<?> download(HttpServletResponse response) {
        // 获取所有用户信息
        BaseResult<IPage<User>> page = userBiz.getPage(null);
        List<User> list = page.getData().getRecords();
        FileParser4List parser = fileService.getListParser(FileType.EXCEL);
        File file = new File(fileService.getLocalFile(FileCart.TEST, null), "test.xlsx");
        try {
            int count = parser.saveObjectList(list, file, User.class);
            BaseResult<?> baseResult = fileService.downloadFile(response, FileCart.TEST, null, file.getName());
            if (count > 0) {
                file.delete();
            }
            return baseResult;
        } catch (Exception e) {
            e.printStackTrace();
            return new FailResult<>();
        }
    }

}
