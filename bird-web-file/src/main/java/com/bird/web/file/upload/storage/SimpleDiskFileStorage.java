package com.bird.web.file.upload.storage;

import com.bird.core.utils.DateHelper;
import com.bird.core.utils.FileHelper;
import com.bird.web.file.upload.IUploadContext;
import com.bird.web.file.upload.UploadException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Random;

/**
 * @author liuxx
 * @date 2018/4/25
 *
 * 简单的磁盘存储器
 */
public class SimpleDiskFileStorage implements IFileStorage {

    private static final String SEPARATOR = "/";

    /**
     * 文件保存路径
     */
    private String dir;

    /**
     * URL前缀
     */
    private String urlPrefix;

    /**
     * 文件保存
     *
     * @param file    上传的文件
     * @param bytes   处理后的文件数据
     * @param context 上下文信息
     * @return 上传成功后的URL地址
     */
    @Override
    public String save(MultipartFile file,byte[] bytes, IUploadContext context) throws UploadException, IOException {
        if (StringUtils.isBlank(this.dir)) {
            throw new UploadException("文件保存路径不存在");
        }
        if (StringUtils.isBlank(urlPrefix)) {
            urlPrefix = String.format("http://%s/", context.getHeader(HttpHeaders.HOST));
        }

        String newFileName = getNewFileName(file, context);
        String additionalPath = getSafePath(StringUtils.stripStart(getAdditionalPath(file, context), SEPARATOR));
        String saveDir = getSafePath(this.dir) + additionalPath;
        File dir = new File(saveDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        if (!dir.canWrite()) {
            throw new IOException("上传目录没有写权限");
        }

        BufferedOutputStream buffStream = null;
        try {
            buffStream = new BufferedOutputStream(new FileOutputStream(saveDir + newFileName));
            buffStream.write(bytes);
        } finally {
            if (buffStream != null) {
                buffStream.close();
            }
        }

        return getSafePath(this.urlPrefix) + additionalPath + newFileName;
    }

    /**
     * 获取附加的路径
     *
     * @param file 文件
     * @param context 上下文信息
     * @return
     */
    protected String getAdditionalPath(MultipartFile file, IUploadContext context) {
        return null;
    }

    /**
     * 获取保存的文件的名称
     * @param file 文件
     * @param context 上下文信息
     * @return
     */
    protected String getNewFileName(MultipartFile file,IUploadContext context){

        String fileName = file.getOriginalFilename();
        return String.format("%s_%d.%s"
                , DateHelper.format(new Date(), "yyyyMMddHHmmss")
                , new Random().nextInt(1000)
                , FileHelper.getSuffix(fileName));
    }

    /**
     * 获取正确的路径
     *
     * @param path 路径
     * @return
     */
    private String getSafePath(String path) {
        if (StringUtils.isBlank(path)) return "";

        if (!StringUtils.endsWith(path, SEPARATOR)) {
            return path + "/";
        }
        return path;
    }


    public String getDir() {
        return this.dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getUrlPrefix() {
        return urlPrefix;
    }

    public void setUrlPrefix(String urlPrefix) {
        this.urlPrefix = urlPrefix;
    }
}
