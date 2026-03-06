package com.svalero.enajenarte.util;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.mapbox.geojson.Point;
import com.mapbox.maps.MapView;
import com.mapbox.maps.plugin.annotation.AnnotationConfig;
import com.mapbox.maps.plugin.annotation.AnnotationPlugin;
import com.mapbox.maps.plugin.annotation.AnnotationPluginImplKt;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManagerKt;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions;
import com.svalero.enajenarte.R;

public class MapUtils {

    public static PointAnnotationManager buildAnnotationManager(MapView mapView) {
        AnnotationPlugin annotationPlugin = AnnotationPluginImplKt.getAnnotations(mapView);
        AnnotationConfig annotationConfig = new AnnotationConfig();
        return PointAnnotationManagerKt.createPointAnnotationManager(annotationPlugin, annotationConfig);
    }

    public static void addMarker(Context context, PointAnnotationManager pointAnnotationManager, Point point) {
        PointAnnotationOptions annotationOptions = new PointAnnotationOptions()
                .withPoint(point)
                .withIconImage(BitmapFactory.decodeResource(
                        context.getResources(),
                        R.drawable.red_marker
                ));
        pointAnnotationManager.create(annotationOptions);
    }
}