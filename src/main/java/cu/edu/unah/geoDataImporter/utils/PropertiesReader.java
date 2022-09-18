package cu.edu.unah.geoDataImporter.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Esta clase permite obtener los valores clave - valor del archivo <b></b>application.properties</b>.
 * @author Nerzur
 */
@Component
@Getter @Setter
public class PropertiesReader {

    /**
     * Indica la ruta donde serán almacenados temporalmente los ficheros.
     */
    @Value("${file.upload-dir}")
    public String fileUploadDir;

    /**
     * Indica el usuario que ejecutará la operación de importación a la base de datos. Este usuario deberá tener privilegios
     * de adición en la base de datos. Por defecto será <i>postgres</i>
     */
    @Value("${db_user}")
    public String dbUser;

    /**
     * Nombre de la base de datos donde se importarán los datos.
     */
    @Value("${db_name}")
    public String dbName;
}
