package cu.edu.unah.geoDataImporter.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request del servicio que permite insertar datos geográficos
 * @author Nerzur
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InsertWeatherDataRequest {

    /**
     * Longitud
     */
    Double longitude;

    /**
     * Latitud
     */
    Double latitude;

    /**
     * Fecha de la entrada
     */
    String date;

    /**
     * Información meteorológica
     */
    String data;
}
