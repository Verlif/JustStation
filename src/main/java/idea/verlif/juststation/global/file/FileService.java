package idea.verlif.juststation.global.file;

import idea.verlif.juststation.global.base.domain.SimPage;
import idea.verlif.juststation.global.base.result.BaseResult;
import idea.verlif.juststation.global.file.handler.FileCart;
import idea.verlif.juststation.global.file.handler.FileHandler;
import idea.verlif.juststation.global.file.handler.FileInfo;
import idea.verlif.juststation.global.file.handler.FileQuery;
import idea.verlif.juststation.global.file.parser.FileParser4List;
import idea.verlif.juststation.global.file.parser.FileParser4Single;
import idea.verlif.juststation.global.file.parser.Parser4List;
import idea.verlif.juststation.global.file.parser.Parser4Single;
import idea.verlif.juststation.global.util.PrintUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

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
    @Autowired
    private FileHandler fileHandler;

    /**
     * 文件解析表
     */
    private static final HashMap<FileType, FileParser4List> LIST_PARSER_HASH_MAP;
    private static final HashMap<FileType, FileParser4Single> SINGLE_PARSER_HASH_MAP;

    static {
        LIST_PARSER_HASH_MAP = new HashMap<>();
        SINGLE_PARSER_HASH_MAP = new HashMap<>();
    }

    public FileService(@Autowired ApplicationContext appContext) {
        Map<String, FileParser4List> listMap = appContext.getBeansOfType(FileParser4List.class);
        for (FileParser4List value : listMap.values()) {
            Parser4List list = value.getClass().getAnnotation(Parser4List.class);
            if (list != null) {
                for (FileType fileType : list.fileType()) {
                    LIST_PARSER_HASH_MAP.put(fileType, value);
                }
            } else {
                PrintUtils.print(Level.WARNING, value.getClass().getSimpleName() + " doesn't has @Parser4List");
            }
        }
        Map<String, FileParser4Single> singleMap = appContext.getBeansOfType(FileParser4Single.class);
        for (FileParser4Single value : singleMap.values()) {
            Parser4Single list = value.getClass().getAnnotation(Parser4Single.class);
            if (list != null) {
                for (FileType fileType : list.fileType()) {
                    SINGLE_PARSER_HASH_MAP.put(fileType, value);
                }
            } else {
                PrintUtils.print(Level.WARNING, value.getClass().getSimpleName() + " doesn't has @Parser4Single");
            }
        }
    }

    public File getLocalFile(FileCart fileCart, String type) {
        return fileHandler.getLocalFile(fileCart, type);
    }

    public String getAccessiblePath(FileCart fileCart, String type, String fileName) {
        return fileHandler.getAccessiblePath(fileCart, type, fileName);
    }

    public BaseResult<SimPage<FileInfo>> getFileList(FileCart fileCart, String type, FileQuery query) {
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
     * 获取文件解析器
     *
     * @param type 文件标志
     * @return 文件解析器
     */
    public FileParser4List getListParser(FileType type) {
        return LIST_PARSER_HASH_MAP.get(type);
    }

    /**
     * 获取文件解析器
     *
     * @param type 文件标志
     * @return 文件解析器
     */
    public FileParser4Single getSingleParser(FileType type) {
        return SINGLE_PARSER_HASH_MAP.get(type);
    }
}
