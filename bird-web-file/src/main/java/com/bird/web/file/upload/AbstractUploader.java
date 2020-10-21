package com.bird.web.file.upload;

import com.bird.web.file.upload.handler.IFileHandler;
import com.bird.web.file.upload.storage.IFileStorage;
import com.bird.web.file.upload.validator.IFileValidator;
import com.bird.web.file.upload.validator.ValidateResult;
import com.bird.web.file.utils.FileHelper;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 定义抽象的文件上传器基类
 *
 * @author liuxx
 * @date 2018/4/25
 */
public abstract class AbstractUploader {

    /**
     * 文件上传监听器
     */
    private IUploadListener uploadListener;

    /**
     * 文件验证器
     */
    private IFileValidator validator;

    /**
     * 文件处理器
     */
    private Map<String, List<IFileHandler>> fileHandlerMap = new HashMap<>(16);

    /**
     * 文件存储器
     */
    private IFileStorage storage;

    /**
     * 文件上传
     *
     * @param context context
     * @return upload result
     */
    protected UploadResult upload(IUploadContext context) throws IOException {
        if (storage == null) {
            throw new UploadException("IFileStorage组件为空，不能存储文件");
        }
        boolean listenerEnable = uploadListener != null;

        List<String> urls = new ArrayList<>();
        for (MultipartFile file : context.getFileMap().values()) {

            //文件验证
            if (validator != null) {
                if (listenerEnable) {
                    uploadListener.beforeValidate(file, context);
                }
                ValidateResult validateResult = validator.validate(file);
                if (validateResult != null && !validateResult.isSuccess()) {
                    continue;
                }
                if (listenerEnable) {
                    uploadListener.afterValidate(file, context, validateResult);
                }
            }
            byte[] bytes = file.getBytes();


            //文件处理
            String suffix = FileHelper.getSuffix(file.getOriginalFilename());
            List<IFileHandler> handlers = fileHandlerMap.get(suffix);
            if (handlers != null) {
                if (listenerEnable) {
                    uploadListener.beforeHandle(file, context);
                }
                for (IFileHandler handler : handlers) {
                    bytes = handler.handle(bytes);
                }
                if (listenerEnable) {
                    uploadListener.afterHandle(file, context);
                }
            }

            //文件存储
            if (listenerEnable) {
                uploadListener.beforeStorage(file, context);
            }
            String url = storage.save(file, bytes, context);
            urls.add(url);
            if (listenerEnable) {
                uploadListener.afterStorage(file, context, url);
            }
        }
        if (CollectionUtils.isEmpty(urls)) {
            return UploadResult.fail("上传文件为空");
        } else {
            return UploadResult.success(urls);
        }
    }

    public void setUploadListener(IUploadListener uploadListener) {
        this.uploadListener = uploadListener;
    }

    public void setValidator(IFileValidator validator) {
        this.validator = validator;
    }

    public void setStorage(IFileStorage storage) {
        this.storage = storage;
    }

    /**
     * 设置文件处理器
     *
     * @param handlers 处理器集合
     */
    public synchronized void setFileHandlers(List<IFileHandler> handlers) {
        if (CollectionUtils.isEmpty(handlers)) {
            return;
        }
        for (IFileHandler handler : handlers) {
            for (String suffix : handler.suffixes()) {
                List<IFileHandler> suffixHandlers = fileHandlerMap.getOrDefault(suffix, new ArrayList<>());
                suffixHandlers.add(handler);
                fileHandlerMap.put(suffix, suffixHandlers);
            }
        }
    }
}
