package com.web.web_shop.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import com.web.web_shop.Exception.StorageException;
import com.web.web_shop.Exception.StorageFileNotFoundException;
import com.web.web_shop.Configuration.StorageProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * FileSystemStorageService
 */
@Service(value = "FileSystemStorageService")
@Primary()
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;

    @Autowired
    public FileSystemStorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    public void store(MultipartFile file,String pathname) {
        //String filename = StringUtils.cleanPath(file.getOriginalFilename());
        pathname = StringUtils.cleanPath(pathname);
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + pathname);
            }
            if (pathname.contains("..")) {
                // This is a security check
                throw new StorageException(
                    "Cannot store file with relative path outside current directory "
                    + pathname);
            }
            pathname =this.rootLocation.resolve(pathname).toString();
            String dirname = pathname.substring(0,pathname.lastIndexOf('/'));
            File dir = new File(dirname);
            if(!dir.exists() && !dir.isDirectory()) {
                dir.mkdir();
            } else if(dir.exists() && dir.isDirectory()) {
                File[] filenames = dir.listFiles(filepath-> {
                    return filepath.isFile();
                });
                for(File filename : filenames) {
                    filename.delete();
                }
            }
            try (InputStream inputStream = file.getInputStream()) {

                Files.copy(inputStream, this.rootLocation.resolve(pathname),
                           StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + pathname, e);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                   .filter(path -> !path.equals(this.rootLocation))
                   .map(this.rootLocation::relativize);
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException(
                    "Could not read file: " + filename);

            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public Resource loadAsResourceByDirname(String dirname) {
        try {
            dirname =this.rootLocation.resolve(dirname).toString();
            File dir = new File(dirname);
            if(!dir.exists()) {
                String new_path = "static/shop.png";
                return this.loadAsResource(new_path);
            }
            File[] filenames = dir.listFiles(pathname-> {
                return pathname.isFile();
            });
            if(filenames.length == 0) {
                String new_path = "static/shop.png";
                return this.loadAsResource(new_path);
            }
            return this.loadAsResource(filenames[0].getName());
        } catch (Exception e) {
            throw new StorageFileNotFoundException("Could not read file: " + dirname, e);
        }

    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }

}
