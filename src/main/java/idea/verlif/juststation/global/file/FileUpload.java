package idea.verlif.juststation.global.file;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/11 16:44
 */
@Data
@Schema(name = "文件上传信息")
public class FileUpload {

    /**
     * 文件子路径
     */
    @Schema(name = "文件子路径")
    private String type;

    /**
     * Base64文件集合
     */
    @Schema(name = "Base64文件集合")
    private Base64File[] uploads;

    @Data
    @Schema(name = "Base64文件信息")
    public static final class Base64File {

        @Schema(name = "文件名")
        private String fileName;

        @Schema(name = "Base64文件内容")
        private String file;
    }
}
