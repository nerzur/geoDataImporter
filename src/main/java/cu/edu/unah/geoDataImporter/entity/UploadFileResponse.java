package cu.edu.unah.geoDataImporter.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UploadFileResponse {

    private String fileName;
    private String fileType;
    private String message;
    private boolean status;
    private long size;

    @DateTimeFormat
    private String date;

    @DateTimeFormat
    private String uploadDate;

}
