package cu.edu.unah.geoDataImporter.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

@Data
@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class WeatherData {

    Double temp_max;
    Double temp_min;
    Double humidity;
    Double wind;
    Double rain;

    @Override
    public String toString() {
        return  "temp_max=" + temp_max +
                ", \ntemp_min=" + temp_min +
                ", \nhumedad=" + humidity +
                ", \nviento=" + wind +
                ", \nlluvias=" + rain;
    }
}
