package cu.edu.unah.geoDataImporter;


import cu.edu.unah.geoDataImporter.utils.ShellUtils;
import org.junit.jupiter.api.Test;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class shellScriptTest {

    ShellUtils shellUtils = new ShellUtils("/home/nerzur/Vídeos/siembra/", "postgres", "geo1");

    @Test
    public void testImportSHPFileInCMD() throws IOException, InterruptedException {
        assert shellUtils.addShapeFile("aa.shp") == 0;
    }

    @Test
    public void testImportCommonFileInCMD() throws IOException, InterruptedException {
        assert shellUtils.addCommonFile("riego.kml") == 0;
    }
}
