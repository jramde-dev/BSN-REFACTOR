package dev.jramde.book_network.file;

import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
public class FileStorageService {

    // File upload path which on-depends from one env to another
    @Value("${application.file.upload.photos-output-path}")
    private String fileUploadPath;

    /**
     * Extract cover file path.
     *
     * @param sourceFile : the original file we want to save in the server
     * @param userId     : the connected user
     * @return file url
     */
    public String getCoverPath(@Nonnull MultipartFile sourceFile, @Nonnull String userId) {
        // Upload path for each user
        final String fileUploadSubPath = "users" + File.separator + userId;
        return uploadFile(sourceFile, fileUploadSubPath);
    }

    /**
     * Upload file from our system to save it to the server.
     * @param sourceFile : file we uploaded
     * @param fileUploadSubPath: new path we created
     * @return file new path.
     */
    private String uploadFile(@Nonnull MultipartFile sourceFile, @Nonnull String fileUploadSubPath) {
        // Final upload path : ./uploads/users/user-id-0987792783
        final String finalUploadPath = fileUploadPath + File.separator + fileUploadSubPath;
        File targetFolder = new File(finalUploadPath);

        if (!targetFolder.exists()) {
            boolean createdFolder = targetFolder.mkdirs();
            if (!createdFolder) {
                log.warn("Failed to create the target folder.");
                return null;
            }
        }

        // Extract the file extension
        final String fileExtension = getFileExtension(sourceFile.getOriginalFilename());

        // Just renaming the finalUploadPath : targetFilePath = ./uploads/users/userId/3456778889.png;
        String finalTargetFilePath = finalUploadPath + File.separator + System.currentTimeMillis() + "." + fileExtension;
        Path finalTargetPath = Paths.get(finalTargetFilePath);
        try {
            Files.write(finalTargetPath, sourceFile.getBytes());
            log.info("Successfully saved the file to {}", finalTargetFilePath);
            return finalTargetFilePath;
        } catch (IOException e) {
            log.error("File was not saved.", e);
            return null;
        }
    }

    /**
     * Extract the file extension.
     *
     * @param originalFilename : original file to extract extension
     * @return file extension
     */
    private String getFileExtension(String originalFilename) {
        if (originalFilename == null || originalFilename.isEmpty()) {
            return "";
        }

        int lastDotIndex = originalFilename.lastIndexOf('.');

        // When not have extension
        if (lastDotIndex == -1) {
            return "";
        }

        return originalFilename.substring(lastDotIndex + 1).toLowerCase();
    }
}
