package nl.pdok.gml3_1_1_2.convertors;

import org.opengis.gml_3_1_1.AbstractCurveType;
import org.opengis.gml_3_1_1.AbstractGeometryType;
import org.opengis.gml_3_1_1.AbstractSurfaceType;
import org.opengis.gml_3_1_1.MultiPointType;
import org.opengis.gml_3_1_1.MultiSurfaceType;
import org.opengis.gml_3_1_1.PointType;

import com.vividsolutions.jts.geom.Geometry;

import nl.pdok.gml3.exceptions.GeometryException;
import nl.pdok.gml3.geometry.extended.ExtendedGeometryFactory;

/**
 * Converteerd van gml3.1.1 naar JTS polygoon
 *
 * @author GinkeM
 */
public class GMLToJTSGeometryConvertor {

    private final GMLToPointConvertor gmlToPointConvertor;
    private final GMLToSurfaceConvertor gmlToSurfaceConvertor;
    private final GMLToLineConvertor gmlToLineConvertor;

    public GMLToJTSGeometryConvertor(ExtendedGeometryFactory geometryFactory) {
        gmlToPointConvertor = new GMLToPointConvertor(geometryFactory);
        gmlToLineConvertor = new GMLToLineConvertor(geometryFactory, gmlToPointConvertor);
        gmlToSurfaceConvertor = new GMLToSurfaceConvertor(geometryFactory, gmlToLineConvertor);
    }

    public Geometry convertGeometry(AbstractGeometryType abstractGeometryType) throws GeometryException {
        if (abstractGeometryType instanceof AbstractSurfaceType) {
            return gmlToSurfaceConvertor.convertSurface((AbstractSurfaceType) abstractGeometryType);

        } else if (abstractGeometryType instanceof MultiPointType) {
            return gmlToPointConvertor.convertMultiPoint((MultiPointType) abstractGeometryType);
        } else if (abstractGeometryType instanceof PointType) {
            return gmlToPointConvertor.convertPoint((PointType) abstractGeometryType);

        } else if (abstractGeometryType instanceof AbstractCurveType) {
            return gmlToLineConvertor.convertAbstractCurve((AbstractCurveType) abstractGeometryType);
        } else if (abstractGeometryType instanceof MultiSurfaceType) {
            return gmlToSurfaceConvertor.convertMultiSurface((MultiSurfaceType) abstractGeometryType);
        } else {
            throw new IllegalArgumentException("Geometry type not supported: "
                    + abstractGeometryType.getClass());
        }
    }

}
