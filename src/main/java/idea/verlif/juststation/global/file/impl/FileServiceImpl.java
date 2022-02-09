package idea.verlif.juststation.global.file.impl;

import idea.verlif.spring.file.FileConfig;
import idea.verlif.spring.file.domain.FileCart;
import idea.verlif.spring.file.domain.FileInfo;
import idea.verlif.spring.file.impl.DefaultFileService;
import idea.verlif.juststation.global.file.FileData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/9/13 10:27
 */
@Component
public class FileServiceImpl extends DefaultFileService {

    public FileServiceImpl(@Autowired FileConfig config) {
        super(config);
    }

    @Override
    protected FileInfo buildInfo(FileCart cart, String type, File file) {
        FileData data = new FileData(file);
        // 添加预览路径
        String path = cart.getArea();
        String url = FileData.TAG + path + (type == null ? "" : type + FileConfig.DIR_SPLIT) + data.getFileName();
        data.setUrl(url);
        return data;
    }
}
