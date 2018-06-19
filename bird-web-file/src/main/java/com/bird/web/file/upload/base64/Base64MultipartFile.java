package com.bird.web.file.upload.base64;

import com.bird.core.utils.DateHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.Base64Utils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Date;
import java.util.Random;

/**
 * 适用于base64文件流的MultipartFile
 *
 * @author liuxx
 * @date 2018/6/15
 */
public class Base64MultipartFile implements MultipartFile {

    /**
     * base64文件流分隔符
     */
    private static final String DELIMITER = ",";

    /**
     * 文件类型分隔符
     */
    private static final String SUFFIX_DELIMITER = "/";
    /**
     * Content-Type分隔符
     */
    private static final String CONTENT_TYPE_DELIMITER = ":";

    /**
     * 默认保存的文件类型
     */
    private static final String DEFAULT_SUFFIX = "png";
    /**
     * 日志记录器
     */
    private static Logger LOGGER = LoggerFactory.getLogger(Base64MultipartFile.class);

    /**
     * base64文件头
     */
    private String header;
    /**
     * 请求的Content-Type
     */
    private String contentType;
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 文件流内容
     */
    private byte[] content;

    private Base64MultipartFile(String header, byte[] imgContent) {
        this.header = header;
        this.content = imgContent;

        String suffix = DEFAULT_SUFFIX;
        if (StringUtils.isNotBlank(this.header)) {
            if (this.header.indexOf(SUFFIX_DELIMITER) > 0) {
                suffix = this.header.split(SUFFIX_DELIMITER)[1].split(";")[0];
            }
            if (this.header.indexOf(CONTENT_TYPE_DELIMITER) > 0) {
                this.contentType = this.header.split(CONTENT_TYPE_DELIMITER)[1].split(";")[0];
            }
        }
        this.fileName = String.format("%s_%d.%s"
                , DateHelper.format(new Date(), "yyyyMMddHHmmss")
                , new Random().nextInt(1000)
                , suffix);
    }

    /**
     * 根据
     *
     * @param base64 base64文件流
     * @return
     */
    public static Base64MultipartFile init(String base64) {
        if (StringUtils.isBlank(base64) || base64.indexOf(DELIMITER) < 0) {
            LOGGER.warn("base64字符串格式错误");
            return null;
        }

        String[] arr = base64.split(",");
        if (arr.length != 2) {
            LOGGER.warn("base64字符串格式错误");
            return null;
        }

        String header = arr[0];
        byte[] content = Base64Utils.decodeFromString(arr[1]);

        return new Base64MultipartFile(header, content);
    }

    @Override
    public String getName() {
        return this.fileName;
    }

    @Nullable
    @Override
    public String getOriginalFilename() {
        return this.fileName;
    }

    @Nullable
    @Override
    public String getContentType() {
        return this.contentType;
    }

    @Override
    public boolean isEmpty() {
        return content == null || content.length == 0;
    }

    @Override
    public long getSize() {
        return this.content.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return this.content;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(this.content);
    }

    @Override
    public void transferTo(File file) throws IOException, IllegalStateException {
        new FileOutputStream(file).write(content);
    }
}
