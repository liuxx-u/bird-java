package com.bird.web.file.upload.storage.disk;

import com.bird.core.utils.DateHelper;
import com.bird.web.file.upload.IUploadContext;
import com.bird.web.file.upload.UploadException;
import com.bird.web.file.upload.storage.IFileStorage;
import com.bird.web.file.utils.FileHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author liuxx
 * @date 2018/4/25
 *
 * 简单的磁盘存储器
 * 公共文件夹示例：/public/image/20180502/
 * 私有文件夹示例：/private/2/image/20180502/
 */
public class DiskFileStorage implements IFileStorage {

    private static final String SEPARATOR = "/";
    private static final String DIR_PUBLIC = "/public";
    private static final String DIR_PRIVATE = "/private";

    private final DiskFileStorageProperties storageProperties;
    private final IFileSuffixDirectoryMapping fileSuffixDirectoryMapping;

    public DiskFileStorage(DiskFileStorageProperties storageProperties,IFileSuffixDirectoryMapping fileSuffixDirectoryMapping) {
        this.storageProperties = storageProperties;
        this.fileSuffixDirectoryMapping = fileSuffixDirectoryMapping;
    }

    /**
     * 文件保存
     *
     * @param file    上传的文件
     * @param bytes   处理后的文件数据
     * @param context 上下文信息
     * @return 上传成功后的URL地址
     */
    @Override
    public String save(MultipartFile file, byte[] bytes, IUploadContext context) throws UploadException, IOException {
        String uploadDir = this.storageProperties.getUploadDir();
        if (StringUtils.isBlank(uploadDir)) {
            throw new UploadException("文件保存路径不存在");
        }

        String urlPrefix = this.storageProperties.getUrlPrefix();
        if (StringUtils.isBlank(urlPrefix)) {
            urlPrefix = String.format("%s://%s/", context.getSchema(), context.getHeader(HttpHeaders.HOST));
        }

        String newFileName = getNewFileName(file);
        String additionalPath = getSafePath(StringUtils.stripStart(getAdditionalPath(file, context), SEPARATOR));
        String saveDir = getSafePath(uploadDir) + additionalPath;
        File dir = new File(saveDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        if (!dir.canWrite()) {
            throw new IOException("上传目录没有写权限");
        }

        try (BufferedOutputStream buffStream = new BufferedOutputStream(new FileOutputStream(saveDir + newFileName))) {
            buffStream.write(bytes);
        }

        return getSafePath(urlPrefix) + additionalPath + newFileName;
    }

    /**
     * 获取附加的路径
     *
     * @param file    文件
     * @param context 上下文信息
     * @return 自定义路径
     */
    private String getAdditionalPath(MultipartFile file, IUploadContext context) {
        String suffix = FileHelper.getSuffix(file.getOriginalFilename());
        String privateDirName = context.getHeader(this.storageProperties.getUploadDirHeader());
        String dir = StringUtils.isBlank(privateDirName) ? DIR_PUBLIC : String.format("%s/%s", DIR_PRIVATE, privateDirName);

        return String.format("%s/%s/%s/", dir, this.fileSuffixDirectoryMapping.getSuffixDirectory(suffix), DateHelper.format(new Date(), "yyyyMMdd"));
    }

    /**
     * 获取保存的文件的名称
     *
     * @param file    文件
     * @return 存储的文件名
     */
    private String getNewFileName(MultipartFile file) {

        String fileName = file.getOriginalFilename();
        return String.format("%s_%d.%s"
                , DateHelper.format(new Date(), "yyyyMMddHHmmss")
                , ThreadLocalRandom.current().nextInt(1000)
                , FileHelper.getSuffix(fileName));
    }

    /**
     * 获取格式化的的路径
     *
     * @param path 路径
     * @return 格式化后的路径
     */
    private String getSafePath(String path) {
        if (StringUtils.isBlank(path)) {
            return "";
        }

        if (!StringUtils.endsWith(path, SEPARATOR)) {
            return path + "/";
        }
        return path;
    }
}
