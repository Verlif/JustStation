package idea.verlif.juststation.global.file.handler;

import idea.verlif.juststation.global.base.domain.SimPage;
import idea.verlif.juststation.global.base.result.BaseResult;
import idea.verlif.juststation.global.base.result.ResultCode;
import idea.verlif.juststation.global.base.result.ext.FailResult;
import idea.verlif.juststation.global.base.result.ext.OkResult;
import idea.verlif.juststation.global.file.FileConfig;
import idea.verlif.juststation.global.util.PageUtils;
import idea.verlif.juststation.global.util.PrintUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/9/13 10:27
 */
public class DefaultFileHandler implements FileHandler {

    private final FileConfig pathConfig;

    public DefaultFileHandler(FileConfig config) {
        this.pathConfig = config;
    }

    /**
     * 获取本地文件夹地址
     *
     * @param fileCart 文件域
     * @param type     文件子目录；可为空
     * @return 本地文件地址
     */
    @Override
    public File getLocalFile(FileCart fileCart, String type) {
        String path = fileCart.getArea();
        if (!path.endsWith(FileConfig.DIR_SPLIT)) {
            path = path + FileConfig.DIR_SPLIT;
        }
        return new File(pathConfig.getMain() + path + (type == null ? "" : type));
    }

    /**
     * 获取外网可访问的文件地址
     *
     * @param fileCart 文件域
     * @param type     文件子路径；可为空
     * @param fileName 文件名
     * @return 文件地址
     */
    @Override
    public String getAccessiblePath(FileCart fileCart, String type, String fileName) {
        String path = fileCart.getArea();
        if (!path.endsWith(FileConfig.DIR_SPLIT)) {
            path += FileConfig.DIR_SPLIT;
        }
        return FileConfig.TAG + path + (type == null ? "" : type) + FileConfig.DIR_SPLIT + fileName;
    }

    /**
     * 获取文件列表
     *
     * @param query    文件查询条件
     * @param fileCart 文件域
     * @param type     文件自目录；可为空
     * @return 文件列表信息
     */
    @Override
    public BaseResult<SimPage<FileInfo>> getFileList(FileCart fileCart, String type, FileQuery query) {
        return new OkResult<>(getInfoList(fileCart, type, query));
    }

    private SimPage<FileInfo> getInfoList(FileCart fileCart, String type, FileQuery query) {
        List<FileInfo> list = new ArrayList<>();
        // 获取文件夹对象
        File file = getLocalFile(fileCart, type);
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (files != null) {
                    // 遍历文件夹内的文件对象，载入列表
                    for (File f : files) {
                        FileInfo info = new FileInfo();
                        info.setFileName(f.getName());
                        info.setUpdateTime(new Date(f.lastModified()));
                        info.setUrl(getAccessiblePath(fileCart, type, f.getName()));
                        list.add(info);
                    }
                }
            }
        }
        // 开始过滤操作
        List<FileInfo> filterList;
        if (query.getName() != null) {
            filterList = list.stream()
                    .filter(fileInfo -> (fileInfo.getFileName().contains(query.getName())))
                    .sorted((o1, o2) -> (int) (o2.getUpdateTime().getTime() - o1.getUpdateTime().getTime()))
                    .collect(Collectors.toList());
        } else {
            filterList = list.stream()
                    .sorted((o1, o2) -> (int) (o2.getUpdateTime().getTime() - o1.getUpdateTime().getTime()))
                    .collect(Collectors.toList());
        }
        list.clear();
        list.addAll(filterList);
        // 创建分页对象
        return PageUtils.page(list, query);
    }

    @Override
    public BaseResult<?> uploadFile(FileCart fileCart, String type, MultipartFile... files) {
        if (files == null || files.length == 0) {
            return new BaseResult<>(ResultCode.FAILURE_FILE_MISSING);
        }
        File dirFile = getLocalFile(fileCart, type);
        // 创建目标文件域
        if (!dirFile.exists()) {
            if (!dirFile.mkdirs()) {
                return new BaseResult<>(ResultCode.FAILURE_FILE).withParam(fileCart.name());
            }
        }
        try {
            for (MultipartFile file : files) {
                String name = file.getOriginalFilename();
                if (name == null) {
                    return new BaseResult<>(ResultCode.FAILURE_FILE_MISSING);
                }
                File dir = new File(dirFile, name);
                file.transferTo(dir);
            }
            return OkResult.empty();
        } catch (IOException e) {
            e.printStackTrace();
            return new BaseResult<>(ResultCode.FAILURE_FILE_UPLOAD);
        }
    }

    /**
     * 下载文件
     *
     * @param response 服务器响应对象
     * @param fileCart 目标文件所在文件域
     * @param type     文件子目录；可为空
     * @param fileName 目标文件名
     * @return 下载结果
     */
    @Override
    public BaseResult<?> downloadFile(HttpServletResponse response, FileCart fileCart, String type, String fileName) {
        response.setCharacterEncoding("UTF-8");
        response.setHeader("content-disposition", "attachment; fileName=" + fileName);
        File file = new File(getLocalFile(fileCart, type), fileName);
        if (file.exists()) {
            try (
                    FileInputStream fis = new FileInputStream(file);
                    OutputStream os = response.getOutputStream()
            ) {
                byte[] b = new byte[1024];
                int length;
                while ((length = fis.read(b)) > 0) {
                    os.write(b, 0, length);
                }
                os.flush();
                return OkResult.empty();
            } catch (IOException e) {
                PrintUtils.print(e);
                return new BaseResult<>(ResultCode.FAILURE_FILE_DOWNLOAD);
            }
        } else {
            return new BaseResult<>(ResultCode.FAILURE_FILE_MISSING).withParam(fileName);
        }
    }

    /**
     * 删除文件
     *
     * @param fileCart 目标文件所在文件域
     * @param type     文件子目录；可为空
     * @param fileName 目标文件名
     * @return 删除结果
     */
    @Override
    public BaseResult<?> deleteFile(FileCart fileCart, String type, String fileName) {
        File file = new File(getLocalFile(fileCart, type), fileName);
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                return OkResult.empty();
            } else {
                return FailResult.empty();
            }
        } else {
            return new BaseResult<>(ResultCode.FAILURE_FILE_MISSING).withParam(fileName);
        }
    }

    /**
     * 删除文件
     *
     * @param file 目标文件
     */
    private void deleteFiles(File file) {
        if (file.isFile()) {
            file.delete();
        } else {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    deleteFiles(f);
                }
            }
        }
    }

    /**
     * 备份文件夹下所有文件
     *
     * @param dirPath 文件夹路径
     * @return 是否备份成功
     */
    private boolean moveToBack(String dirPath) {
        return moveToBack(new File(dirPath));
    }

    /**
     * 备份文件夹下所有文件（只会备份文件，不会备份文件夹）
     *
     * @param dirFile 文件夹对象
     * @return 是否备份成功
     */
    private boolean moveToBack(File dirFile) {
        if (!dirFile.isDirectory()) {
            return false;
        }
        // 创建备份文件夹
        File backup = getBackFile(dirFile);
        if (!backup.exists()) {
            if (!backup.mkdirs()) {
                return false;
            }
        }
        // 备份文件夹存在则开始备份
        if (backup.exists()) {
            // 获取目标文件夹下的所有文件
            File[] oldFiles = dirFile.listFiles();
            if (oldFiles != null) {
                // 遍历目标文件
                for (File file : oldFiles) {
                    // 对可用的文件对象进行操作
                    if (file.exists() && file.isFile()) {
                        // 生成目标文件的备份文件对象
                        File backFile = new File(backup, file.getName());
                        // 备份已存在时，删除旧备份
                        if (backFile.exists()) {
                            if (backFile.delete()) {
                                if (!file.renameTo(backFile)) {
                                    return false;
                                }
                            } else {
                                return false;
                            }
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    private File getBackFile(String dirPath) {
        return new File(dirPath, ".back");
    }

    private File getBackFile(File dirFile) {
        return new File(dirFile, ".back");
    }
}
