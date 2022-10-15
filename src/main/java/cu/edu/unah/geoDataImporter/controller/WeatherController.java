package cu.edu.unah.geoDataImporter.controller;

import cu.edu.unah.geoDataImporter.entity.Weather;
import cu.edu.unah.geoDataImporter.request.InsertWeatherDataRequest;
import cu.edu.unah.geoDataImporter.request.InsertWeatherDataResponse;
import cu.edu.unah.geoDataImporter.service.WeatherService;
import cu.edu.unah.geoDataImporter.utils.GeometryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Esta clase contiene el servicio api-rest para adicionar los datos del clima de un lugar especificado en un momento del
 * tiempo. Para ello se utiliza la clase {@link InsertWeatherDataRequest} que recogerá los datos indicados por el usuario
 * en el servicio, los procesará y procederá a insertarlos en la base de datos. Una vez finalizada la operación se mostrarán
 * los resultados de la misma mediante la clase {@link InsertWeatherDataResponse}.
 * @author Nerzur
 * @version 1.0
 */
@RestController
@RequestMapping(value = "/weather")
public class WeatherController {

    /**
     * Instancia de la clase {@link WeatherService}, la cual contiene las funciones necesarias para insertar el nuevo
     * elemento en la base de datos.
     */
    @Autowired
    private WeatherService weatherService;

    /**
     * Servicio web que contendrá los datos del estado del tiempo de un lugar en un momento específico. Para ello se
     * utiliza la clase en el request {@link InsertWeatherDataRequest} la cual contiene los valores necesarios. En caso
     * de los parámetros <em>latitude</em> y <em>longitude</em>, si no son especificados, por defecto se colocarán los valores
     * en caso de no indicarse <b>longitude = -82.1653382</b> y <b>latitude = 22.9968894</b>, que se corresponden con
     * la localización de la UDP Guayabal (Mayabeque, Cuba). En caso de no especificarse el parámetro <em>date</em> se tomará
     * como valor predeterminado la fecha actual.
     * <br/>
     * El resultado estará compuesto por una instancia de la clase {@link InsertWeatherDataResponse} que indicará los
     * resultados de la operación.
     * @param request Datos del request.
     * @return Resultado de la operación que será mostrado en el response al usuario.
     */
    @PostMapping("/insert")
    public ResponseEntity<InsertWeatherDataResponse> createWeatherEntry(@RequestBody InsertWeatherDataRequest request) {
        double longitude = -82.1653382, latitude = 22.9968894;
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH mm");
        InsertWeatherDataResponse insertWeatherDataResponseError = InsertWeatherDataResponse.builder()
                .data("{error: error}")
                .date(simpleDateFormat.format(date))
                .location(GeometryUtil.parseLocation(longitude, latitude))
                .status(false)
                .message("ERROR RESPONSE")
                .build();

        if(request.getLatitude() != null && request.getLongitude() != null){
            longitude = request.getLongitude();
            latitude = request.getLatitude();
        }

        if(request.getDate()!=null){
            try {
                date = simpleDateFormat.parse(request.getDate());
            }catch (ParseException e) {
                e.printStackTrace();
                insertWeatherDataResponseError.setMessage("Invalid date format");
                return ResponseEntity.badRequest().body(insertWeatherDataResponseError);
            }
        }

        Weather weatherRequest = Weather.builder()
                .id(0L)
                .data(request.getData().toString())
                .date(date)
                .location(GeometryUtil.parseLocation(longitude, latitude))
                .build();
        Weather weatherCreate = weatherService.createWeatherEntry(weatherRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(InsertWeatherDataResponse.builder()
                .data(weatherCreate.getData())
                .date(simpleDateFormat.format(date))
                .location(weatherCreate.getLocation())
                .status(true)
                .message("ok")
                .build());
    }
}
