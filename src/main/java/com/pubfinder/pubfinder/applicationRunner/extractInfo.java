package com.pubfinder.pubfinder.applicationRunner;

import com.pubfinder.pubfinder.models.Accessibility;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class extractInfo {

  protected static String getStringOrNull(JSONObject obj, String key) {
    Object value = obj.opt(key);
    return value instanceof String ? (String) value : null;
  }

  protected static Boolean getOutDoorSeating(JSONArray array) throws JSONException {
    boolean outDoorSeatingAvailable = false;

    for (int j = 0; j < array.length(); j++) {
      JSONObject amenityObj = array.getJSONObject(j);
      if (amenityObj.has("Outdoor seating")) {
        outDoorSeatingAvailable = amenityObj.getBoolean("Outdoor seating");
        break;
      }
    }
    return outDoorSeatingAvailable;
  }

  protected static Boolean getWashroom(JSONArray array) throws JSONException {
    boolean washroomAvailable = false;

    for (int j = 0; j < array.length(); j++) {
      JSONObject amenityObj = array.getJSONObject(j);
      if (amenityObj.has("Washroom")) {
        washroomAvailable = amenityObj.getBoolean("Washroom");
        break;
      }
    }
    return washroomAvailable;
  }

  protected static Accessibility getAccessibility(JSONArray array) throws JSONException {
    boolean accessibleSeating = false;
    boolean accessibleEntrance = false;
    boolean accessibleParking = false;

    for (int j = 0; j < array.length(); j++) {
      JSONObject amenityObj = array.getJSONObject(j);
      if (amenityObj.has("Wheelchair accessible seating")) {
        accessibleSeating = amenityObj.getBoolean("Wheelchair accessible seating");
      } else if (amenityObj.has("Wheelchair-accessible entrance")) {
        accessibleEntrance = amenityObj.getBoolean("Wheelchair-accessible entrance");
      } else if (amenityObj.has("Wheelchair-accessible parking lot")) {
        accessibleParking = amenityObj.getBoolean("Wheelchair-accessible parking lot");
      }
    }

    return Accessibility.builder()
        .accessibleSeating(accessibleSeating)
        .accessibleEntrance(accessibleEntrance)
        .accessibleParking(accessibleParking)
        .build();

  }
}
