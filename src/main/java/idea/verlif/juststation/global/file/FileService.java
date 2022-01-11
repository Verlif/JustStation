package idea.verlif.juststation.global.file;

import idea.verlif.juststation.global.base.domain.SimPage;
import idea.verlif.juststation.global.base.result.BaseResult;
import idea.verlif.juststation.global.file.domain.FileCart;
import idea.verlif.juststation.global.file.domain.FileInfo;
import idea.verlif.juststation.global.file.domain.FileQuery;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;

/**
 * 文件控制器，用于管理实体文件
 *
 * @author Verlif
 * @version 1.0
 * @date 2021/11/10 9:14
 */
public interface FileService {

    /**
     * 获取本地文件夹对象
     *
     * @param fileCart 文件域
     * @param type     子目录
     * @return 文件夹对象
     */
    File getLocalFile(FileCart fileCart, String type);

    /**
     * 获取外网可访问的文件地址
     *
     * @param fileCart 文件域
     * @param type     文件子路径；可为空
     * @param fileName 文件名
     * @return 文件地址
     */
    String getAccessiblePath(FileCart fileCart, String type, String fileName);

    /**
     * 获取文件列表
     *
     * @param query    文件查询条件
     * @param fileCart 文件域
     * @param type     文件自目录；可为空
     * @return 文件列表信息
     */
    BaseResult<SimPage<FileInfo>> getFileList(FileCart fileCart, String type, FileQuery query);

    /**
     * 上传文件
     *
     * @param fileCart 文件域
     * @param type     文件子路径；可为空
     * @param files    目标文件组
     * @return 是否上传成功
     */
    BaseResult<?> uploadFile(FileCart fileCart, String type, MultipartFile... files);

    /**
     * 下载文件
     *
     * @param response 服务器响应对象
     * @param fileCart 目标文件所在文件域
     * @param type     文件子目录；可为空
     * @param fileName 目标文件名
     * @return 下载结果
     */
    BaseResult<?> downloadFile(HttpServletResponse response, FileCart fileCart, String type, String fileName);

    /**
     * 删除文件
     *
     * @param fileCart 目标文件所在文件域
     * @param type     文件子目录；可为空
     * @param fileName 目标文件名
     * @return 删除结果
     */
    BaseResult<?> deleteFile(FileCart fileCart, String type, String fileName);
}
