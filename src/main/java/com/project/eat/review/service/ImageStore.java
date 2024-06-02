package com.project.eat.review.service;

import com.project.eat.review.entity.ReviewPic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class ImageStore {

    @Value("${upload.dir}")
    private String fileDir;

    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    public List<ReviewPic> storeFiles(List<MultipartFile> files) {
        List<ReviewPic> storeFileResult = new ArrayList<>();
        for (MultipartFile file : files) {
            if (!file.isEmpty()){
                try {
                    storeFileResult.add(storeFile(file));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return storeFileResult;
    }

    public ReviewPic storeFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();

        String storeFileName = createStoreFileName(originalFilename);
        multipartFile.transferTo(new File(getFullPath(storeFileName)));

        return ReviewPic.builder().uploadFileName(originalFilename).storeFileName(storeFileName).build();

    }

    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos);
    }
}
