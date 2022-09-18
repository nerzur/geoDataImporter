package cu.edu.unah.geoDataImporter.service;

import cu.edu.unah.geoDataImporter.exceptions.FileStorageException;
import cu.edu.unah.geoDataImporter.properties.FileStorageProperties;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
@Getter @Setter
public class FileStorageService {

    private final Path fileStorageLocation;

    private long fileSize = 0;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String storeFile(MultipartFile file, String date) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            File oldFile = targetLocation.toFile();
            String newName = FilenameUtils.getBaseName(fileName) + " " + date + "." + FilenameUtils.getExtension(fileName);
            String newPath = this.fileStorageLocation.toFile().getAbsolutePath()+"\\"+newName;
            oldFile.renameTo(new File(newPath));
            fileSize+=new File(newPath).length();
            return newName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public String[] storeMultipleFiles (MultipartFile[] files, String date){
        String[] fileNames = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            fileNames[i] = storeFile(files[i], date);
        }
        return fileNames;
    }

    public boolean cleanDumpFiles(String... fileNames){
        int filesDeleted = 0;
        for (String fileName : fileNames) {
            new File (this.fileStorageLocation.toFile().getAbsolutePath()+"\\"+fileName).delete();
            filesDeleted++;
        }
        return filesDeleted==fileNames.length;
    }
}
