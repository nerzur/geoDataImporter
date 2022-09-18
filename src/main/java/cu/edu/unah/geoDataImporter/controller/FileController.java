package cu.edu.unah.geoDataImporter.controller;

import cu.edu.unah.geoDataImporter.entity.UploadFileResponse;
import cu.edu.unah.geoDataImporter.service.FileStorageService;
import cu.edu.unah.geoDataImporter.utils.PropertiesReader;
import cu.edu.unah.geoDataImporter.utils.ShellUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Esta clase contiene los servicios api-rest para la inserción de ficheros <em>shapefile</em>, <em>kml</em> y <em>obb</em>
 * en una base de datos. Debe tenerse en cuenta que esta base de datos deberá ser de tipo PostgreSQL y se deberá tener
 * a sus ficheros. Debe contarse con el nombre de un usuario con privilegios de adición y una base de datos creada.
 * La base de datos debe contener la extensión <em>pPstgis</em>.<br/>
 * La configuración de este servicio se encuentra en el fichero <b>application.properties</b> donde se configurarán los
 * elementos anteriormente mencionados.
 *
 * @author Nerzur
 */
@RestController
@RequestMapping(value = "/uploadFile")
public class FileController {

    /**
     * Contiene los tipos de extensión que van a ser procesados por los servicios de este controlador.
     */
    private final String[] EXTENSIONS = {"SHP", "OBB", "KML"};

    /**
     * Contiene las extensiones de ficheros requeridas para el procesamiento de un fichero de tipo <em>shapefile</em>
     */
    private final String[] SHP_REQUIRED_FILE_EXTENSIONS = {"DBF", "PRJ", "SHP", "SHX"};

    /**
     * Instancia del <em>propertiesReader</em> para acceder a los valores contenidos en el fichero <em>application.properties</em>
     */
    @Autowired
    PropertiesReader propertiesReader;

    /**
     * Instancia de la clase {@link FileStorageService}. Esta clase contiene las funciones necesarias para el almacenamiento
     * de los fucheros.
     */
    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/commonFile")
    public ResponseEntity<UploadFileResponse> processCommonFile(@RequestParam("file") MultipartFile file, @RequestParam(name = "date", required = false) String date) {
        Date now = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormatCurrentDate = new SimpleDateFormat("yyyy-MM-dd HH mm");
        String originalfileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String fileExtension = FilenameUtils.getExtension(originalfileName);
        String dateToFileName = simpleDateFormatCurrentDate.format(now);
        boolean isValidExtension = false;
        UploadFileResponse errorResponse = UploadFileResponse.builder()
                .fileType(fileExtension)
                .uploadDate(simpleDateFormatCurrentDate.format(now))
                .message("ERROR RESPONSE")
                .date(dateToFileName)
                .status(false)
                .build();
        for (String extension : EXTENSIONS) {
            if (extension.toUpperCase().equals("SHP"))
                continue;
            if (fileExtension.toUpperCase().equals(extension)) {
                isValidExtension = true;
                break;
            }
        }
        if (!isValidExtension) {
            errorResponse.setMessage("Invalid extension file");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        try {
            if (null != date) {
                simpleDateFormatCurrentDate.parse(date);
                dateToFileName = date;
            }
        } catch (ParseException e) {
            errorResponse.setMessage("Invalid date format");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        fileStorageService.setFileSize(0);
        String fileName = fileStorageService.storeFile(file, dateToFileName);
        int resultCode = -1;
        ShellUtils shellUtils = new ShellUtils(propertiesReader.getFileUploadDir(), propertiesReader.getDbUser(), propertiesReader.getDbName());
        resultCode = shellUtils.addCommonFile(fileName);
        System.out.println(fileStorageService.cleanDumpFiles(fileName));

        if (resultCode != 0) {
            errorResponse.setMessage("An error has been occurred processing de file");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        return ResponseEntity.ok(UploadFileResponse.builder()
                .fileName(fileName)
                .fileType(fileExtension)
                .uploadDate(simpleDateFormatCurrentDate.format(now))
                .message("ok")
                .size(fileStorageService.getFileSize())
                .date(dateToFileName).build());
    }

    @PostMapping("/shapeFile")
    public ResponseEntity<UploadFileResponse> processShapeFile(@RequestParam("file") MultipartFile[] files, @RequestParam(name = "date", required = false) String date) {
        Date now = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormatCurrentDate = new SimpleDateFormat("yyyy-MM-dd HH mm");
        String dateToFileName = simpleDateFormatCurrentDate.format(now);
        UploadFileResponse errorResponse = UploadFileResponse.builder()
                .fileType("shp")
                .uploadDate(simpleDateFormatCurrentDate.format(now))
                .message("ERROR RESPONSE")
                .date(dateToFileName)
                .status(false)
                .build();

        if (!verifyFilesExtension(files)) {
            errorResponse.setMessage("Invalid extension file");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        try {
            if (null != date) {
                simpleDateFormatCurrentDate.parse(date);
                dateToFileName = date;
            }
        } catch (ParseException e) {
            errorResponse.setMessage("Invalid date format");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        fileStorageService.setFileSize(0);
        String[] fileNames = fileStorageService.storeMultipleFiles(files, dateToFileName);
        System.out.println();
        int resultCode = -1;
        ShellUtils shellUtils = new ShellUtils(propertiesReader.getFileUploadDir(), propertiesReader.getDbUser(), propertiesReader.getDbName());
        String shapeFileName = getShapeFileName(fileNames);
        resultCode = shellUtils.addShapeFile(shapeFileName);
        System.out.println(fileStorageService.cleanDumpFiles(fileNames));

        if (resultCode != 0) {
            errorResponse.setMessage("An error has been occurred processing de file");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        return ResponseEntity.ok(UploadFileResponse.builder()
                .fileName(shapeFileName)
                .fileType("shp")
                .uploadDate(simpleDateFormatCurrentDate.format(now))
                .message("ok")
                .size(fileStorageService.getFileSize())
                .date(dateToFileName).build());
    }

    private boolean verifyFilesExtension(MultipartFile... files) {
        boolean[] extensionsExist = new boolean[SHP_REQUIRED_FILE_EXTENSIONS.length];
        String originalfileName = FilenameUtils.getBaseName(StringUtils.cleanPath(Objects.requireNonNull(files[0].getOriginalFilename())));
        for (MultipartFile file : files) {
            String currentFileName = FilenameUtils.getBaseName(StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename())));
            String fileExtension = FilenameUtils.getExtension(StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()))).toUpperCase();
            for (int i = 0; i < SHP_REQUIRED_FILE_EXTENSIONS.length; i++) {
                if (fileExtension.equals(SHP_REQUIRED_FILE_EXTENSIONS[i]) && originalfileName.equals(currentFileName)) {
                    extensionsExist[i] = true;
                    break;
                }
            }
        }
        for (boolean exist : extensionsExist) {
            if (!exist) {
                return false;
            }
        }
        return true;
    }

    private String getShapeFileName(String[] fileNames) {
        for (String fileName : fileNames) {
            if (FilenameUtils.getExtension(fileName).toUpperCase().equals("SHP"))
                return fileName;
        }
        return "";
    }

}
