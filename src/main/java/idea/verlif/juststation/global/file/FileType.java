package idea.verlif.juststation.global.file;

/**
 * 文件类型
 *
 * @author Verlif
 * @version 1.0
 * @date 2021/11/23 14:28
 */
public enum FileType {

    /**
     * EXCEL文件类型
     */
    EXCEL("xls", "xlsx");

    /**
     * 文件后缀
     */
    private final String[] suffix;

    FileType(String... suffix) {
        this.suffix = suffix;
    }

    public String[] getSuffix() {
        return suffix;
    }

    public static FileType fromSuffix(String suffix) {
        for (FileType value : values()) {
            for (String s : value.suffix) {
                if (s.equals(suffix)) {
                    return value;
                }
            }
        }
        return null;
    }

    public static FileType fromFilename(String filename) {
        if (filename == null) {
            return null;
        }
        String[] filenameS = filename.split("\\.");
        String suffix = filenameS[filenameS.length - 1];
        return fromSuffix(suffix);
    }
}
