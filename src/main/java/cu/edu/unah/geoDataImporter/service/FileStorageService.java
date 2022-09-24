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
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

/**
 * Este servicio permite realizar las funciones necesarias con los ficheros. Contiene las funciones para el almacenamiento
 * de uno o varios ficheros {@link MultipartFile} en una ruta especificada. Así como su posterior eliminación.
 * @version 1.0
 */
@Service
@Getter @Setter
public class FileStorageService {

    /**
     * Ruta donde serán almacenados los ficheros temporales.
     */
    private final Path fileStorageLocation;

    /**
     * Tamaño de el/los ficheros del request.
     */
    private long fileSize = 0;

    /**
     * Constructor de la clase. Inicializa la ruta de almacenamiento, de acuerdo al valor localizado en el fichero
     * application.properties. En caso de no existir crea la carpeta.
     * @param fileStorageProperties Clase que contiene el intérprete para obtener los datos del application.properties.
     */
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

    /**
     * Esta función permite almacenar un fichero {@link MultipartFile} en una ruta especificada. Al nombre del fichero
     * se le añade una fecha indicada en el parámetro <em>date</em>.
     * @param file Fichero {@link MultipartFile} que será almacenado.
     * @param date Fecha que será incluida en el nombre del fichero.
     * @return Devuelve el nombre del fichero almacenado.
     */
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
            String newName = normalizeFileName(FilenameUtils.getBaseName(fileName)) + " " + date + "." + FilenameUtils.getExtension(fileName);
            String newPath = this.fileStorageLocation.toFile().getAbsolutePath()+"\\"+newName;
            oldFile.renameTo(new File(newPath));
            fileSize+=new File(newPath).length();
            return newName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    /**
     * Esta función permite almacenar un conjunto de ficheros {@link MultipartFile}. A cada nombre de fichero se le
     * adiciona una fecha.
     * @param files Ficheros {@link MultipartFile} que deben ser almacenados.
     * @param date Fecha que debe ser adicionada a los nombres de los ficheros.
     * @return Devuelve un array con los nombres de los ficheros almacenados.
     */
    public String[] storeMultipleFiles (MultipartFile[] files, String date){
        String[] fileNames = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            fileNames[i] = storeFile(files[i], date);
        }
        return fileNames;
    }

    /**
     * Esta función permite eliminar un conjunto de ficheros a partir de sus nombres.
     * @param fileNames Nombres de los ficheros a eliminar.
     * @return Devuelve un <em>boolean</em> indicando si ha sido posible eliminar los ficheros.
     */
    public boolean cleanDumpFiles(String... fileNames){
        int filesDeleted = 0;
        for (String fileName : fileNames) {
            new File (this.fileStorageLocation.toFile().getAbsolutePath()+"\\"+fileName).delete();
            filesDeleted++;
        }
        return filesDeleted==fileNames.length;
    }

    /**
     * Esta función permite normalizar un nombre de archivo, eliminando así los caracteres especiales.
     * @param filename Nombre del fichero a normalizar.
     * @return Nombre del fichero normalizado
     */
    private String normalizeFileName (String filename){
        return filename.toLowerCase().replace("á", "a")
                .replace("é", "e")
                .replace("í", "i")
                .replace("ó", "o")
                .replace("ú", "u");
    }
}
