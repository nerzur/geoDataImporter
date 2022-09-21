package cu.edu.unah.geoDataImporter.service;

import cu.edu.unah.geoDataImporter.entity.Weather;
import cu.edu.unah.geoDataImporter.repository.WeatherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WeatherServiceImpl implements WeatherService{

    @Autowired
    private WeatherRepository weatherRespository;

    @Override
    public Weather createWeatherEntry(Weather weather) {
        return weatherRespository.save(weather);
    }
}
