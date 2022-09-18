package cu.edu.unah.geoDataImporter;


import cu.edu.unah.geoDataImporter.utils.ShellUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class shellScriptTest {

    ShellUtils shellUtils = new ShellUtils("C:\\uploads\\", "postgres", "geo1");

    @Test
    public void testImportSHPFileInCMD() throws IOException, InterruptedException {
        String command = "cmd /C shp2pgsql -d \"C:\\uploads\\Layer #0 2022-09-17 16 30.shp\" | psql geo1 postgres";
        shellUtils.executeProcess(command);
    }

    @Test
    public void testImportCommonFileInCMD() throws IOException, InterruptedException {
        String command = "cmd /C ogr2ogr -skipfailures -overwrite -progress --config PG_USE_COPY YES -f PGDump /vsistdout/ \"D:\\Nueva Carpeta\\Guayabal\\Máquina de riego\\Máquina de riego.kml\" | psql geo1 postgres";
        shellUtils.executeProcess(command);
    }
}
