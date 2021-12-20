package idea.verlif.juststation.global.kit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import idea.verlif.juststation.global.util.PrintUtils;
import reactor.util.annotation.NonNull;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 文件工具集
 *
 * @author Verlif
 * @version 1.0
 * @date 2021/12/16 9:20
 */
public class FileHolder {

    private static final String SUFFIX_SPLIT = ".";

    private final File file;

    private final ObjectMapper objectMapper;

    public FileHolder(File file) {
        this.file = file;
        objectMapper = new ObjectMapper();
    }

    public FileHolder(String path) {
        this(new File(path));
    }

    /**
     * 获取文件或文件夹对象
     *
     * @return 路径对应的文件或文件夹；当文件或文件夹不存在时，创建并返回
     */
    public File getOrCreate() {
        if (file.exists()) {
            return file;
        } else {
            // 判定是文件还是文件夹
            try {
                if (file.getName().contains(SUFFIX_SPLIT) && file.createNewFile()) {
                    return file;
                } else if (file.mkdirs()) {
                    return file;
                }
            } catch (IOException e) {
                PrintUtils.print(e);
            }
        }
        return null;
    }

    /**
     * 获取对象列表 <br/>
     * 仅能获取以{@linkplain #putList(List)}方式存入的数据
     *
     * @param cl  对象类
     * @param <T> 对象泛型
     * @return 对象列表
     */
    public <T> List<T> getList(Class<T> cl) {
        String c = getString();
        List<T> list = new ArrayList<>();
        try {
            JsonNode node = objectMapper.readTree(c);
            if (node.isArray()) {
                Iterator<JsonNode> elements = node.elements();
                while (elements.hasNext()) {
                    list.add(objectMapper.treeToValue(elements.next(), cl));
                }
            }
        } catch (JsonProcessingException e) {
            PrintUtils.print(e);
        }
        return list;
    }

    /**
     * 存入列表 <br/>
     * 仅能通过{@linkplain #getList(Class)}方式获取列表
     *
     * @param list 列表数据
     * @param <T>  对象泛型
     * @return 是否存入成功
     */
    public <T> boolean putList(List<T> list) {
        return putString(objectMapper.valueToTree(list).toString());
    }

    /**
     * 从列表中读取字符串
     *
     * @return 文件中存入的字符串
     */
    public @NonNull
    String getString() {
        char[] chars = new char[1024];
        try (FileReader reader = new FileReader(file)) {
            StringBuilder sb = new StringBuilder();
            int size = reader.read(chars);
            while (size > -1) {
                sb.append(chars, 0, size);
                chars = new char[1024];
                size = reader.read(chars);
            }
            return sb.toString();
        } catch (IOException e) {
            PrintUtils.print(e);
        }
        return "";
    }

    /**
     * 向文件中写入字符串
     *
     * @param content 字符串
     * @return 是否写入成功
     */
    public boolean putString(String content) {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
            writer.flush();
            return true;
        } catch (IOException e) {
            PrintUtils.print(e);
        }
        return false;
    }

}
