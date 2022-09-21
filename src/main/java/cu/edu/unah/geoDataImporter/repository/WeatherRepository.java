package cu.edu.unah.geoDataImporter.repository;

import cu.edu.unah.geoDataImporter.entity.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherRepository extends JpaRepository<Weather, Long> {
}
