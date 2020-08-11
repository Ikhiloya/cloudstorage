package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.FileDTO;
import com.udacity.jwdnd.course1.cloudstorage.model.FileForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class FileStorageService {
    private final Logger logger = LoggerFactory.getLogger(FileStorageService.class);
    private final FileMapper fileMapper;

    public FileStorageService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public String saveFile(Integer userId, MultipartFile file) {
        String message = null;
        // Normalize file name
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        //check for duplicate file name
        if (!isFileNameAvailableForUser(fileName, userId)) {
            logger.info("file name already exists");
            message = "The file name already exists.";
            return message;
        }

        // Check if the file's name contains invalid characters
        if (fileName.contains("..")) {
            message = "There was an error saving your File, file name contains invalid characters. Please try again.";
            return message;
        }

        try {
            File dbFile = new File(fileName, file.getContentType(), userId, file.getBytes());
            fileMapper.insert(dbFile);
        } catch (Exception ex) {

            message = "There was an error saving your file. Please try again.";
        }
        return message;
    }


    public List<FileDTO> getFilesForUser(Integer userId) {
        List<File> dbFiles = fileMapper.getFilesForUser(userId);
        List<FileDTO> fileData = new ArrayList<>();
        dbFiles.forEach(file -> fileData.add(new FileDTO(file.getFileId(), file.getFileName(), file.getContentType(), file.getUserId())));
        return fileData;
    }

    public File getFile(Integer fileId) {
        return fileMapper.geFileById(fileId);
    }

    public int deleteFile(FileForm fileForm) {
        return fileMapper.delete(fileForm.getFileId());
    }

    public boolean isFileNameAvailableForUser(String fileName, Integer userId) {
        return fileMapper.geFileByNameAndUserId(fileName, userId) == null;
    }
}
