package idea.verlif.juststation.global.file.impl;

import idea.verlif.file.service.FileConfig;
import idea.verlif.file.service.impl.DefaultFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
}
