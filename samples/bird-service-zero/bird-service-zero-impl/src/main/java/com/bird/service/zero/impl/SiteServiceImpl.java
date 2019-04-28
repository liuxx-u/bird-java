package com.bird.service.zero.impl;

import com.bird.service.common.service.AbstractService;
import com.bird.service.zero.SiteService;
import com.bird.service.zero.mapper.SiteMapper;
import com.bird.service.zero.model.Site;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = "zero_site")
@org.apache.dubbo.config.annotation.Service
public class SiteServiceImpl extends AbstractService<SiteMapper,Site> implements SiteService {
}
