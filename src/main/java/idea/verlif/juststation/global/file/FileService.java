package idea.verlif.juststation.global.file;

import com.baomidou.mybatisplus.core.metadata.IPage;
import idea.verlif.juststation.core.base.result.BaseResult;
import idea.verlif.juststation.global.file.handler.*;
import idea.verlif.juststation.global.file.parser.FileParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.HashMap;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/16 14:18
 */
@Service
public class FileService {

    /**
     * 文件管理器
     */
    private final FileHandler fileHandler;

    /**
     * 文件解析表
     */
    private static final HashMap<FileType, FileParser> PARSER_HASH_MAP;

    static {
        PARSER_HASH_MAP = new HashMap<>();
    }

    public FileService(
            @Autowired(required = false) FileHandler fileHandler,
            @Autowired FilePathConfig config) {
        if (fileHandler == null) {
            this.fileHandler = new DefaultFileHandler(config);
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

    public BaseResult<?> uploadFile(FileCart fileCart, String type, MultipartFile[] files) {
        return fileHandler.uploadFile(fileCart, type, files);
    }

    public BaseResult<?> downloadFile(HttpServletResponse response, FileCart fileCart, String type, String fileName) {
        return fileHandler.downloadFile(response, fileCart, type, fileName);
    }

    public BaseResult<?> deleteFile(FileCart fileCart, String type, String fileName) {
        return fileHandler.deleteFile(fileCart, type, fileName);
    }

    /**
     * 注册文件解析器
     *
     * @param type   文件标志
     * @param parser 文件解析器
     */
    public static <T extends FileParser> void register(FileType type, T parser) {
        PARSER_HASH_MAP.put(type, parser);
    }

    /**
     * 获取文件解析器
     *
     * @param type 文件标志
     * @return 文件解析器
     */
    public FileParser getParser(FileType type) {
        return PARSER_HASH_MAP.get(type);
    }
}
