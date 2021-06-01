package com.web.web_shop.Service;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * StorageService
 */
@Service
public interface StorageService {

    void init();

    void store(MultipartFile file, String filename);

    @Bean
    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename);

    Resource loadAsResourceByDirname(String dirname);

    void deleteAll();

}
