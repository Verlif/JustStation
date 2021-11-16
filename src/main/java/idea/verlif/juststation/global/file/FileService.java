package idea.verlif.juststation.global.file;

import com.baomidou.mybatisplus.core.metadata.IPage;
import idea.verlif.juststation.core.base.result.BaseResult;
import idea.verlif.juststation.global.component.FileHandler;
import idea.verlif.juststation.global.file.impl.FileHandlerAto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/16 14:18
 */
@Service
public class FileService {

    private final FileHandler fileHandler;

    public FileService(@Autowired(required = false) FileHandler fileHandler, @Autowired FilePathConfig config) {
        if (fileHandler == null) {
            this.fileHandler = new FileHandlerAto(config);
        } else {
            this.fileHandler = fileHandler;
        }
    }

    public File getLocalFile(FileCart fileCart, String type) {
        return fileHandler.getLocalFile(fileCart, type);
    }

    public String getAccessiblePath(FileCart fileCart, String type, String fileName) {
        return fileHandler.getAccessiblePath(fileCart, type, fileName);
    }

    public BaseResult<IPage<FileInfo>> getFileList(FileCart fileCart, String type, FileQuery query) {
        return fileHandler.getFileList(fileCart, type, query);
    }

    public BaseResult<?> uploadFile(FileCart fileCart, String type, MultipartFile file) {
        return fileHandler.uploadFile(fileCart, type, file);
    }

    public BaseResult<?> downloadFile(HttpServletResponse response, FileCart fileCart, String type, String fileName) {
        return fileHandler.downloadFile(response, fileCart, type, fileName);
    }

    public BaseResult<?> deleteFile(FileCart fileCart, String type, String fileName) {
        return fileHandler.deleteFile(fileCart, type, fileName);
    }
}
