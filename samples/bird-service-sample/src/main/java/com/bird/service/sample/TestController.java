package com.bird.service.sample;

import com.bird.service.common.datasource.DataSourceContext;
import com.bird.service.sample.dto.TabLongDTO;
import com.bird.service.sample.dto.TabStringDTO;
import com.bird.service.sample.model.TabLong;
import com.bird.service.sample.model.TabString;
import com.bird.service.sample.service.TabLongService;
import com.bird.service.sample.service.TabStringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuxx
 * @date 2019/8/29
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private TabLongService tabLongService;

    @Autowired
    private TabStringService tabStringService;

    @GetMapping("/long/insert")
    public TabLong insertLong(String remark) {
        TabLong model = new TabLong();
        model.setRemark(remark);
        return tabLongService.save(model);
    }

    @GetMapping("/long/update")
    public TabLong updateLong(Long id,String remark) {
        TabLong model = new TabLong();
        model.setId(id);
        model.setRemark(remark);
        return tabLongService.save(model);
    }

    @GetMapping("/long/updateDto")
    public void updateLongDto(Long id,String remark) {
        TabLongDTO dto = new TabLongDTO();
        dto.setId(id);
        dto.setRemark(remark);
        tabLongService.save(dto);
    }

    @GetMapping("/long/insertDto")
    public Long insertLongDto(String remark) {
        TabLongDTO dto = new TabLongDTO();
        dto.setRemark(remark);
        return tabLongService.save(dto);
    }

    @GetMapping("/long/insertBatch")
    public List<TabLong> insertBatchLong() {
        List<TabLong> models = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            TabLong model = new TabLong();
            model.setRemark("remark" + i);
            models.add(model);
        }
        tabLongService.insertBatch(models);

        return models;
    }

    @GetMapping("/string/insert")
    public TabString insertString(String remark) {
        DataSourceContext.set("db2");
        TabString model = new TabString();
        model.setRemark(remark);
        return tabStringService.save(model);
    }

    @GetMapping("/string/update")
    public TabString updateString(String id,String remark) {
        TabString model = new TabString();
        model.setId(id);
        model.setRemark(remark);
        return tabStringService.save(model);
    }

    @GetMapping("/string/insertDto")
    public String insertStringDto(String remark) {
        TabStringDTO dto = new TabStringDTO();
        dto.setRemark(remark);
        return tabStringService.save(dto);
    }

    @GetMapping("/string/updateDto")
    public void updateStringDto(String id,String remark) {
        TabStringDTO dto = new TabStringDTO();
        dto.setId(id);
        dto.setRemark(remark);
        tabStringService.save(dto);
    }


    @GetMapping("/string/insertBatch")
    public List<TabString> insertBatchString() {
        List<TabString> models = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            TabString model = new TabString();
            model.setRemark("remark" + i);
            models.add(model);
        }
        tabStringService.insertBatch(models);

        return models;
    }
}
