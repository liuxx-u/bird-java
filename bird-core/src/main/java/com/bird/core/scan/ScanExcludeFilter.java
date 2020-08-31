package com.bird.core.scan;

import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import java.io.IOException;

/**
 * IgnoreScan 注解的类忽略ComponentsScan扫描
 *
 * @author liuxx
 * @since 2020/5/29
 */
public class ScanExcludeFilter extends TypeExcludeFilter {

    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
        if(metadataReader.getAnnotationMetadata().hasAnnotation(IgnoreScan.class.getName())){
            return true;
        }

        return super.match(metadataReader, metadataReaderFactory);
    }
}
