package com.bird.web.api.controller;

import org.apache.dubbo.config.annotation.Reference;
import com.bird.core.OperationResult;
import com.bird.service.cms.CmsClassifyService;
import com.bird.service.cms.dto.CmsClassifyDTO;
import com.bird.service.common.service.query.PagedListQueryDTO;
import com.bird.service.common.service.query.PagedListResultDTO;
import com.bird.web.common.controller.AbstractController;
import com.bird.web.file.upload.ServletUploader;
import com.bird.web.file.upload.UploadResult;
import com.bird.web.file.upload.storage.SimpleDiskFileStorage;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author liuxx
 * @date 2018/3/19
 */
@RestController
@RequestMapping("/")
public class HomeController extends AbstractController {

    @Reference
    private CmsClassifyService classifyService;

    @GetMapping("/test")
    public CmsClassifyDTO test(){
        return classifyService.getClassify(1L);
    }

    @PostMapping("/upload")
    public UploadResult upload(HttpServletRequest request) throws IOException{

        SimpleDiskFileStorage storage = new SimpleDiskFileStorage();
        storage.setDir("D:/upload");
        storage.setUrlPrefix("http://localhost:8080/");

        ServletUploader uploader = new ServletUploader();
        uploader.setStorage(storage);

        return uploader.upload(request);
    }

    @PostMapping("/getPaged")
    @ApiOperation(value = "获取分页")
    public OperationResult<PagedListResultDTO> getPaged(@RequestBody PagedListQueryDTO query) {

        PagedListResultDTO result = classifyService.queryPagedList(query, CmsClassifyDTO.class);
        return OperationResult.Success("获取成功", result);
    }
}
