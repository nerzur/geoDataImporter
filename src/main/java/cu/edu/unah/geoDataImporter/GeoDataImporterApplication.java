package cu.edu.unah.geoDataImporter;

import cu.edu.unah.geoDataImporter.properties.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
		FileStorageProperties.class
})
public class GeoDataImporterApplication {

	public static void main(String[] args) {
		SpringApplication.run(GeoDataImporterApplication.class, args);
	}

}
