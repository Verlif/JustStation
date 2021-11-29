package idea.verlif.juststation.global.file.handler;

import idea.verlif.juststation.global.base.domain.Pageable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/9/13 10:25
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(name = "文件查询条件")
public class FileQuery extends Pageable<FileInfo> {

    @Schema(name = "文件名查询")
    private String name;
}
