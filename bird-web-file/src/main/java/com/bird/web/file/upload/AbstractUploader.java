package com.bird.web.file.upload;

import com.bird.core.utils.FileHelper;
import com.bird.web.file.upload.handler.IFileHandler;
import com.bird.web.file.upload.storage.IFileStorage;
import com.bird.web.file.upload.validator.IFileValidator;
import com.bird.web.file.upload.validator.ValidateResult;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liuxx
 * @date 2018/4/25
 */
public abstract class AbstractUploader {

    /**
     * 文件上传监听器
     */
    @Autowired(required = false)
    private IUploadListener uploadListener;

    /**
     * 文件验证器
     */
    @Autowired(required = false)
    protected IFileValidator validator;

    /**
     * 文件处理器
     */
    @Autowired(required = false)
    protected Map<String, List<IFileHandler>> fileHandlerMap;

    /**
     * 文件存储器
     */
    @Autowired
    protected IFileStorage storage;

    /**
     * 设置文件处理器
     *
     * @param handler 处理器
     * @param suffixs 对应的文件后缀名集合
     */
    public synchronized void setFileHandler(IFileHandler handler, String... suffixs) {
        if (handler == null) return;
        if (ArrayUtils.isEmpty(suffixs)) return;

        if (fileHandlerMap == null) {
            fileHandlerMap = new HashMap<>(16);
        }
        for (String suffix : suffixs) {
            List<IFileHandler> handlers = fileHandlerMap.getOrDefault(suffix, new ArrayList<>());
            handlers.add(handler);
            fileHandlerMap.put(suffix, handlers);
        }
    }

    /**
     * 文件上传
     *
     * @param context
     * @return
     */
    protected UploadResult upload(IUploadContext context) throws IOException {
        if (storage == null) {
            throw new UploadException("IFileStorage组件为空，不能存储文件");
        }
        boolean listenerEnable = uploadListener != null;

        //TODO:请求验证

        for (MultipartFile file : context.getFileMap().values()) {

            //文件验证
            if (validator != null) {
                if (listenerEnable) uploadListener.beforeValidate(file, context);
                ValidateResult validateResult = validator.validate(file);
                if (validateResult != null && !validateResult.isSuccess()) {
                    return UploadResult.fail(validateResult.getErrorInfo());
                }
                if (listenerEnable) uploadListener.afterValidate(file, context, validateResult);
            }

            //文件处理
            String suffix = FileHelper.getSuffix(file.getOriginalFilename());
            Object handlers = MapUtils.getObject(fileHandlerMap, suffix);
            if (handlers != null) {
                if (listenerEnable) uploadListener.beforeHandle(file, context);
                ((List<IFileHandler>) handlers).forEach(handler -> handler.handle(file));
                if (listenerEnable) uploadListener.afterHandle(file, context);
            }

            //文件存储
            String url = storage.save(file, context);

            return UploadResult.success(url);
        }

        return null;
    }

    public void setUploadListener(IUploadListener uploadListener) {
        this.uploadListener = uploadListener;
    }

    public void setValidator(IFileValidator validator){
        this.validator = validator;
    }

    public void setStorage(IFileStorage storage){
        this.storage = storage;
    }
}
