package com.bird.web.file.upload;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件上传结果
 *
 * @author liuxx
 * @date 2018/4/25
 */
@Getter
@Setter
public class UploadResult implements Serializable {
    private boolean success;
    private String message;
    private String path;
    private List<String> paths;

    public UploadResult() {
        paths = new ArrayList<>();
    }

    public UploadResult(boolean success, String message, String path) {
        this();

        this.success = success;
        this.message = message;
        this.path = path;
        if (StringUtils.hasText(path)) {
            this.paths.add(path);
        }
    }

    public UploadResult(boolean success, String message, List<String> paths) {
        this();

        this.success = success;
        this.message = message;
        this.paths = paths;

        if (!CollectionUtils.isEmpty(paths)) {
            this.path = paths.get(0);
        }
    }

    public static UploadResult success(String path) {
        return new UploadResult(true, "上传成功", path);
    }

    public static UploadResult success(List<String> paths) {
        return new UploadResult(true, "上传成功", paths);
    }

    public static UploadResult fail(String message) {
        return new UploadResult(false, message, "");
    }
}
