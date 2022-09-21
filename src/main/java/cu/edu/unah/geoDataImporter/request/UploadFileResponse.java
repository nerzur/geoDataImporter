package cu.edu.unah.geoDataImporter.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Response de los servicios de subida de ficheros
 * @author Nerzur
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UploadFileResponse {

    /**
     * Nombre del fichero subido
     */
    private String fileName;

    /**
     * Tipo de fichero subido
     */
    private String fileType;

    /**
     * Mensaje del resultado de la operación. En caso de ser satisfactoria "ok" y en caso de error mensaje pertinente.
     */
    private String message;

    /**
     * Estado de la operación.
     */
    private boolean status;

    /**
     * Tamaño del fichero.
     */
    private long size;

    /**
     * Fecha de los datos..
     */
    @DateTimeFormat
    private String date;

    /**
     * Fecha de subida del fichero.
     */
    @DateTimeFormat
    private String uploadDate;

}
