package cu.edu.unah.geoDataImporter.utils;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

/**
 * Esta clase contiene algunas funciones necesarias para obtener un objeto {@link Point} a partir de las coordenadas de
 * un punto.
 * @author Nerzur
 * @version 1.0
 */
public class GeometryUtil {

    public static final int SRID = 4326; //LatLng
    private static WKTReader wktReader = new WKTReader();

    /**
     * Esta función permite parsear un <em>String</em> que contiene un punto formado por dos coordenadas. Esta función
     * permitirá obtener un objeto de tipo {@link Geometry}.
     * @param wellKnownText Texto que contiene los datos de un punto formado por sus coordenadas.
     * @return Devuelve un objeto de tipo {@link Geometry}.
     */
    private static Geometry wktToGeometry(String wellKnownText) {
        Geometry geometry = null;

        try {
            geometry = wktReader.read(wellKnownText);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return geometry;
    }

    /**
     * Esta función permite obtener un {@link Point} a partir de las coordenadas de una localización. Se indicarán la
     * longitud y la latitud.
     * @param longitude Longitud donde se encuentra ubicado el punto
     * @param latitude Latitud donde se encuentra ubicado el punto.
     * @return Devuelve el {@link Point} donde se encuentra ubicada la localización.
     */
    public static Point parseLocation(double longitude, double latitude) {
        Geometry geometry = GeometryUtil.wktToGeometry(String.format("POINT (%s %s)",longitude,latitude));
        Point p =(Point)geometry;
        p.setSRID(4326);
        return p;
    }
}
