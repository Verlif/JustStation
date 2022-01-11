package idea.verlif.juststation.global.file.parser;

import idea.verlif.juststation.global.file.FileType;
import idea.verlif.juststation.global.util.PrintUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * @author Verlif
 * @version 1.0
 * @date 2022/1/11 14:16
 */
@Service
public class FileParserService {

    /**
     * 文件解析表
     */
    private static final HashMap<FileType, FileParser4List> LIST_PARSER_HASH_MAP;
    private static final HashMap<FileType, FileParser4Single> SINGLE_PARSER_HASH_MAP;
    static {
        LIST_PARSER_HASH_MAP = new HashMap<>();
        SINGLE_PARSER_HASH_MAP = new HashMap<>();
    }

    public FileParserService(@Autowired ApplicationContext appContext) {
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
