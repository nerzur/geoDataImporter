package cu.edu.unah.geoDataImporter.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import com.vividsolutions.jts.geom.Point;

import org.springframework.boot.autoconfigure.AutoConfiguration;

import javax.persistence.*;
import java.util.Date;

/**
 * Se corresponde a los datos de la tabla de datos meteorológicos de la base de datos.
 * @author Nerzur
 * @version 1.0
 */
@Entity
@Table(name = "weather")
@AutoConfiguration
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
public class Weather {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Localización del lugar
     */
    @Column(columnDefinition = "geography")
    Point location;

    /**
     * Fecha de la información
     */
    Date date;

    /**
     * Información meteorológica
     */
    String data;
}
