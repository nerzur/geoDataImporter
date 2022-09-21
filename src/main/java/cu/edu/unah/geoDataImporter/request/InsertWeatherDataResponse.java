package cu.edu.unah.geoDataImporter.request;

import com.vividsolutions.jts.geom.Point;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Response del servicio que permite insertar datos geográficos.
 * @author Nerzur
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InsertWeatherDataResponse {

    /**
     * Localización del punto geográfico
     */
    Point location;

    /**
     * Información meteorológica
     */
    String data;

    /**
     * Estado de la operación
     */
    boolean status;

    /**
     * Mensaje del resultado de la operación. En caso de ser satisfactoria "ok" y en caso de error mensaje pertinente.
     */
    String message;

    /**
     * Fecha de la entrada.
     */
    @DateTimeFormat
    String date;

}
