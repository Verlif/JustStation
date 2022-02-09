package idea.verlif.juststation.global.file;

import idea.verlif.spring.file.domain.FileInfo;

import java.io.File;

/**
 * @author Verlif
 */
public class FileData extends FileInfo {

    /**
     * 文件访问URL前缀
     */
    public static final String TAG = "/file/";

    /**
     * 文件路径分隔符
     */
    public static final String DIR_SPLIT = File.separator;

    /**
     * 文件预览路径
     */
    private String url;

    public FileData(File file) {
        super(file);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url.replaceAll("\\\\", "/");
    }
}
