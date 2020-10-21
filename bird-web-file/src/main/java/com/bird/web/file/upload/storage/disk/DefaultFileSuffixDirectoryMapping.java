package com.bird.web.file.upload.storage.disk;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liuxx
 * @since 2020/10/21
 */
public class DefaultFileSuffixDirectoryMapping implements IFileSuffixDirectoryMapping {

    /**
     * 定义允许上传的文件扩展名与对应的上传目录。key：文件后缀名;value:存储路径
     */
    private static final Map<String, String> EXT_DIR_MAP = new HashMap<>();

    static {
        String dirImg = "images";
        String dirFlash = "flashs";
        String dirMedia = "medias";
        String dirFile = "files";

        //图片
        EXT_DIR_MAP.put("gif", dirImg);
        EXT_DIR_MAP.put("jpg", dirImg);
        EXT_DIR_MAP.put("jpeg", dirImg);
        EXT_DIR_MAP.put("png", dirImg);
        EXT_DIR_MAP.put("bmp", dirImg);
        EXT_DIR_MAP.put("tif", dirImg);

        //flash
        EXT_DIR_MAP.put("swf", dirFlash);
        EXT_DIR_MAP.put("flv", dirFlash);

        //视频
        EXT_DIR_MAP.put("swf", dirMedia);
        EXT_DIR_MAP.put("flv", dirMedia);
        EXT_DIR_MAP.put("mp3", dirMedia);
        EXT_DIR_MAP.put("wav", dirMedia);
        EXT_DIR_MAP.put("wma", dirMedia);
        EXT_DIR_MAP.put("wmv", dirMedia);
        EXT_DIR_MAP.put("mid", dirMedia);
        EXT_DIR_MAP.put("avi", dirMedia);
        EXT_DIR_MAP.put("mpg", dirMedia);
        EXT_DIR_MAP.put("asf", dirMedia);
        EXT_DIR_MAP.put("rm", dirMedia);
        EXT_DIR_MAP.put("rmvb", dirMedia);

        //文档
        EXT_DIR_MAP.put("doc", dirFile);
        EXT_DIR_MAP.put("docx", dirFile);
        EXT_DIR_MAP.put("xls", dirFile);
        EXT_DIR_MAP.put("xlsx", dirFile);
        EXT_DIR_MAP.put("ppt", dirFile);
        EXT_DIR_MAP.put("pptx", dirFile);
        EXT_DIR_MAP.put("csv", dirFile);
        EXT_DIR_MAP.put("htm", dirFile);
        EXT_DIR_MAP.put("html", dirFile);
        EXT_DIR_MAP.put("txt", dirFile);
        EXT_DIR_MAP.put("zip", dirFile);
        EXT_DIR_MAP.put("rar", dirFile);
        EXT_DIR_MAP.put("gz", dirFile);
        EXT_DIR_MAP.put("bz2", dirFile);
        EXT_DIR_MAP.put("7z", dirFile);
        EXT_DIR_MAP.put("pdf", dirFile);
    }

    /**
     * 获取文件格式对应的存储目录
     *
     * @param suffix 文件格式
     * @return 存储目录
     */
    @Override
    public String getSuffixDirectory(String suffix) {
        return EXT_DIR_MAP.getOrDefault(suffix == null ? StringUtils.EMPTY : suffix.toLowerCase(), "none");
    }
}
