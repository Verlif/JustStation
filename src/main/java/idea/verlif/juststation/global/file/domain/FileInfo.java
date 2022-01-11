package idea.verlif.juststation.global.file.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/9/13 10:22
 */
@Data
@Schema(name = "文件信息")
public class FileInfo {

    @Schema(name = "文件名称")
    private String fileName;

    @Schema(name = "文件上次更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @Schema(name = "文件访问URL")
    private String url;
}
