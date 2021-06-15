package com.nscmobileapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CameraUtils {
    public static List<Camera> makeCameraList(List<Map> jsonToParse) {
        List<Camera> cameraList = new ArrayList<>();
        for (Map camMap : jsonToParse) {
            List nestedList = (List) camMap.get("Cameras");
            Map camDetails = (Map) nestedList.get(0);
            List<Double> coordinateList = (List<Double>) camMap.get("PointCoordinate");

            cameraList.add(new Camera(coordinateList.get(0), coordinateList.get(1),
                    (String) camDetails.get("Id"), (String) camDetails.get("Description"),
                    (String) camDetails.get("ImageUrl"), (String) camDetails.get("Type")));
        }
        return cameraList;
    }
}
