package cu.edu.unah.geoDataImporter.utils;

import lombok.AllArgsConstructor;

import java.io.*;

/**
 * Esta clase tiene como objetivo realizar las operaciones en el shell del sistema operativo para la adición de los ficheros
 * a la base de datos. Contiene una función para mostrar los resultados del programa ejecutado en el shell.
 * @author Nerzur
 */
@AllArgsConstructor
public class ShellUtils {

    /**
     * Contiene la ruta de la carpeta temporal donde serán almacenados los ficheros.
     */
    public String fileUploadDir;

    /**
     * Contiene el nombre de usuario con el que se accederá a la base de datos.
     */
    public String dbUser;

    /**
     * Contiene el nombre de la base de datos.
     */
    public String dbName;

    /**
     * Esta función ejecuta un comando indicado en el shell del sistema operativo y espera el resultado de la ejecución.
     * @param command Comando a ser ejecutado en el sistema operativo.
     * @return Devuelve un entero que indica el resultado de la ejecución del proceso. <b>0</b> si la ejecución se ha
     * realizado correctamente, en caso contrario mostrará un valor diferente.
     * @throws IOException Es lanzada si existe un error de entrada / salida.
     * @throws InterruptedException Es lanzada si la operación es interrumpida de forma abrupta.
     */
    public int executeProcess(String command) throws IOException, InterruptedException {
        Process process = Runtime.getRuntime()
                .exec("cmd /C " + command, null, new File(fileUploadDir));
        process.waitFor();
//        System.out.println(obtainProcessConsoleResults(process));
//        System.out.println(process.exitValue());
        return process.exitValue();
    }

    /**
     * Esta función permite obtener los resultados mostrados en el shell de un proceso indicado.
     * @param process Proceso del que se desea obtener los resultados.
     * @return Devuelve un <em>String</em> que contiene los resultados de la ejecución del proceso.
     * @throws IOException Es lanzado cuando existen errores de entrada / salida.
     */
    private String obtainProcessConsoleResults(Process process) throws IOException {
        InputStream inputstream = process.getInputStream();
        BufferedInputStream bufferedinputstream = new BufferedInputStream(inputstream);
        byte[] contents = new byte[1024];
        int bytesRead = 0;
        String strFileContents = "";
        while ((bytesRead = bufferedinputstream.read(contents)) != -1) {
            strFileContents += new String(contents, 0, bytesRead);
        }
        return strFileContents;
    }

    /**
     * Esta función permite insertar un <b>shapefile</b> en una base de datos indicada. Para ello debe proporcionarse
     * el <em>fileName</em> del fichero a insertar. El resto de la ruta del fichero, el nombre de la base de datos y el
     * nombre del usuario para acceder a la base de datos deben haber sido ingresados en las variables <em>fileUploadDir</em>,
     * <em>dbName</em> y <em>dbUser</em> respectivamente.
     * @param fileName Nombre del archivo <b>shapefile</b> que se desea insertar.
     * @return Devuelve el resultado de la operación de inserción. Si la operación ha sido realizada satisfactoriamente
     * esta función devolverá <em>0</em>, en caso contrario devolverá un valor que indicará el error o estado de finalización
     * de la app.
     */
    public int addShapeFile(String fileName) {
        String command = "shp2pgsql -d \"" + fileUploadDir + fileName + "\" | psql " + dbName + " " + dbUser;
        try {
            return executeProcess(command);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Esta función permite insertar un archivo con los formatos <em>kml</em> u <em>obb</em> en una base de datos indicada.
     * Para ello debe proporcionarse el <em>fileName</em> del fichero a insertar. El resto de la ruta del fichero,
     * el nombre de la base de datos y el nombre del usuario para acceder a la base de datos deben haber sido ingresados
     * en las variables <em>fileUploadDir</em>,<em>dbName</em> y <em>dbUser</em>
     * @param fileName Nombre del archivo que se desea insertar.
     * @return Devuelve el resultado de la operación de inserción. Si la operación ha sido realizada satisfactoriamente
     * esta función devolverá <em>0</em>, en caso contrario devolverá un valor que indicará el error o estado de finalización
     * de la app.
     */
    public int addCommonFile(String fileName) {
        String command = "ogr2ogr -skipfailures -overwrite -progress --config PG_USE_COPY YES -f PGDump /vsistdout/ \"" + fileUploadDir + fileName + "\" | psql " + dbName + " " + dbUser;
        try {
            return executeProcess(command);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
