package idea.verlif.juststation.global.file;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import idea.verlif.juststation.core.base.result.BaseResult;
import idea.verlif.juststation.core.base.result.ext.FailResult;
import idea.verlif.juststation.core.base.result.ext.OkResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
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
@Service
public class SystemFileService {

    @Resource
    private FilePathConfig pathConfig;

    /**
     * 获取本地文件夹地址
     *
     * @param fileCart 文件域
     * @param type     文件子目录；可为空
     * @return 本地文件地址
     */
    public File getLocalFile(FileCart fileCart, String type) {
        String path = fileCart.getArea();
        if (!path.endsWith(FilePathConfig.DIR_SPLIT)) {
            path = path + FilePathConfig.DIR_SPLIT;
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
    public String getAccessiblePath(FileCart fileCart, String type, String fileName) {
        String path = fileCart.getArea();
        if (!path.endsWith(FilePathConfig.DIR_SPLIT)) {
            path += FilePathConfig.DIR_SPLIT;
        }
        return FilePathConfig.TAG + path + (type == null ? "" : type) + FilePathConfig.DIR_SPLIT + fileName;
    }

    /**
     * 获取文件列表
     *
     * @param query    文件查询条件
     * @param fileCart 文件域
     * @param type     文件自目录；可为空
     * @return 文件列表信息
     */
    public BaseResult<IPage<FileInfo>> getFileList(FileCart fileCart, String type, FileQuery query) {
        return new OkResult<>(getInfoList(fileCart, type, query));
    }

    private IPage<FileInfo> getInfoList(FileCart fileCart, String type, FileQuery query) {
        // 创建分页对象
        IPage<FileInfo> page = new Page<>();
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
        page.setTotal(list.size());
        page.setSize(query.getPageSize());
        page.setPages(page.getTotal() / page.getSize());
        if (page.getTotal() % page.getSize() > 0) {
            page.setPages(page.getPages() + 1);
        }
        page.setCurrent(query.getPageNum());
        if (query.getPageHead() > list.size()) {
            page.setRecords(new ArrayList<>());
        } else {
            int end = query.getPageHead() + query.getPageSize();
            page.setRecords(list.subList(query.getPageHead(), Math.min(end, list.size())));
        }
        return page;
    }

    /**
     * 上传文件
     *
     * @param fileCart 文件域
     * @param type     文件子路径；可为空
     * @param file     目标文件
     * @return 是否上传成功
     */
    public BaseResult<?> uploadFile(FileCart fileCart, String type, MultipartFile file) {
        if (file == null) {
            return new FailResult<>("不允许上传空文件");
        }
        File dirFile = getLocalFile(fileCart, type);
        // 创建目标文件域
        if (!dirFile.exists()) {
            if (!dirFile.mkdirs()) {
                return new FailResult<>("无法创建对应文件区域 - " + fileCart.name());
            }
        }
        try {
            String name = file.getOriginalFilename();
            if (name == null) {
                return new FailResult<>("上传的文件需要文件名");
            }
            File dir = new File(dirFile, name);
            file.transferTo(dir);
            return new OkResult<>().msg("上传成功");
        } catch (IOException e) {
            e.printStackTrace();
            return new FailResult<>("文件上传失败");
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
    public BaseResult<?> downloadFile(HttpServletResponse response, FileCart fileCart, String type, String fileName) {
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
                return new OkResult<>("文件下载请求成功");
            } catch (IOException e) {
                e.printStackTrace();
                return new FailResult<>("文件下载失败");
            }
        } else {
            return new FailResult<>("文件不存在");
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
    public BaseResult<?> deleteFile(FileCart fileCart, String type, String fileName) {
        File file = new File(getLocalFile(fileCart, type), fileName);
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                return new OkResult<>().msg("删除成功");
            } else {
                return new FailResult<>("删除失败");
            }
        } else {
            return new FailResult<>("文件不存在");
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
        backup.mkdirs();
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
