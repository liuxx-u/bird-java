package com.bird.web.api.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bird.service.zero.DicTypeService;
import com.bird.service.zero.OrganizationService;
import com.bird.service.zero.dto.OrganizationDTO;
import com.bird.web.file.upload.ServletUploader;
import com.bird.web.file.upload.UploadResult;
import com.bird.web.file.upload.storage.IFileStorage;
import com.bird.web.file.upload.storage.SimpleDiskFileStorage;
import com.bird.web.sso.SsoAuthorizeManager;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * @author liuxx
 * @date 2018/3/19
 */
@RestController
@RequestMapping("/")
public class HomeController {

    @Inject
    private SsoAuthorizeManager authorizeManager;

    @Reference
    private DicTypeService dicTypeService;

    @Reference
    private OrganizationService organizationService;

    @GetMapping("/test")
    public OrganizationDTO test(){
        return organizationService.getOrganization(1L);
    }

    @PostMapping("/upload")
    public UploadResult upload(HttpServletRequest request) throws IOException{

        SimpleDiskFileStorage storage = new SimpleDiskFileStorage();
        storage.setPath("D:/upload");
        storage.setUrlPrefix("http://localhost:8080/");

        ServletUploader uploader = new ServletUploader();
        uploader.setStorage(storage);

        return uploader.upload(request);
    }
}
