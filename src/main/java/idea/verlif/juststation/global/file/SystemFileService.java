package idea.verlif.juststation.global.file;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/9/13 10:27
 */
@Service
public class SystemFileService {

    @Resource
    private FilePathConfig pathConfig;

    public File getLocalFile(String path) {
        return getLocalFile(path, null);
    }

    /**
     * 获取本地文件地址
     *
     * @param path 相对路径
     * @param id   文件路径ID
     * @return 本地文件地址
     */
    public File getLocalFile(String path, String id) {
        if (!path.endsWith("/")) {
            path = path + "/";
        }
        return new File(pathConfig.getMain() + "/" + path + (id == null ? "" : id));
    }

    /**
     * 获取外网可访问的文件地址
     *
     * @param basePath 文件相对路径
     * @param id       文件从属ID
     * @param fileName 文件名
     * @return 文件地址
     */
    public String getAccessiblePath(String basePath, String id, String fileName) {
        return FilePathConfig.TAG + "/" + basePath + (id == null ? "" : id) + "/" + fileName;
    }

    /**
     * 获取ID路径
     *
     * @param basePath 相对路径
     * @param id       从属ID
     * @return ID路径
     */
    public String getIdPath(String basePath, String id) {
        return basePath + "/" + id;
    }

    /**
     * 上传文件到文件系统并覆盖原有文件
     *
     * @param basePath   文件存储相对路径，例如" system/file "
     * @param fileUpload 上传的文件数据。fileUpload中有ID时，会按照ID建立子文件夹
     * @return 上传结果
     */
//    public BaseResult<?> uploadAndCoverFiles(String basePath, FileUpload fileUpload) {
//        File pathFile = getLocalFile(basePath, fileUpload.getId());
//        // 原文件备份
//        File backup = new File(pathFile, ".back");
//        backup.mkdirs();
//        if (backup.exists()) {
//            File[] oldFiles = pathFile.listFiles();
//            if (oldFiles != null) {
//                for (File file : oldFiles) {
//                    if (file.exists() && file.isFile()) {
//                        if (!file.renameTo(new File(backup, file.getName()))) {
//                            return new BaseResult<>().code(ResultCode.FAILURE.getCode()).msg("备份文件错误");
//                        }
//                    }
//                }
//            }
//        }
//        try {
//            for (FileUpload.Base64File file : fileUpload.getFiles()) {
//                String fileS = file.getBase64();
//                if (fileS != null) {
//                    File newFile = new File(pathFile, file.getFileName());
//                    org.apache.commons.io.FileUtils.copyInputStreamToFile(Base64FileUtil.getFileInputStream(fileS), newFile);
//                }
//            }
//            deleteFiles(backup);
//            return BaseResult.success("上传成功");
//        } catch (IOException e) {
//            return new BaseResult<>().code(ResultCode.FAILURE.getCode()).msg(e.getMessage());
//        }
//    }

    /**
     * 上传文件
     *
     * @param path 存入地址
     * @param file 目标文件
     * @return 是否上传成功
     */
    public boolean uploadFile(String path, MultipartFile file) {
        File pathFile = new File(path);
        if (!pathFile.exists()) {
            if (!pathFile.mkdirs()) {
                return false;
            }
        }
        try {
            String name = file.getOriginalFilename();
            if (name == null) {
                return false;
            }
            File dir = new File(path, name);
            file.transferTo(dir);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
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
}
