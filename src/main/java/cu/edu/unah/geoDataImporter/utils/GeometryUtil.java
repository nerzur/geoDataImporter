package cu.edu.unah.geoDataImporter.utils;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

public class GeometryUtil {

    public static final int SRID = 4326; //LatLng
    private static WKTReader wktReader = new WKTReader();

    private static Geometry wktToGeometry(String wellKnownText) {
        Geometry geometry = null;

        try {
            geometry = wktReader.read(wellKnownText);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println("###geometry :"+geometry);
        return geometry;
    }
    public static Point parseLocation(double x, double y) {
        Geometry geometry = GeometryUtil.wktToGeometry(String.format("POINT (%s %s)",x,y));
        Point p =(Point)geometry;
        p.setSRID(4326);
        return p;
    }
}
