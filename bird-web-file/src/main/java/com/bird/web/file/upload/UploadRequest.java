package com.bird.web.file.upload;

import com.bird.web.file.FileItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liuxx
 * @date 2018/4/26
 */
public class UploadRequest implements Serializable {

    private long size;
    private List<FileItem> files;
    private Map<String,Object> fields;

    public UploadRequest(){
        files = new ArrayList<>();
        fields = new HashMap<>();
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public List<FileItem> getFiles() {
        return files;
    }

    public void setFiles(List<FileItem> files) {
        this.files = files;
    }

    public Map<String, Object> getFields() {
        return fields;
    }

    public void setFields(Map<String, Object> fields) {
        this.fields = fields;
    }
}
