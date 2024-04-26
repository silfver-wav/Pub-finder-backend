package com.pubfinder.pubfinder.applicationRunner;

import com.pubfinder.pubfinder.models.OpeningHours;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class extractTime {

  protected static Map<DayOfWeek, List<OpeningHours>> openingHours(JSONArray array)
      throws JSONException {
    Map<DayOfWeek, List<OpeningHours>> map = new HashMap<>();
    for (int j = 0; j < array.length(); j++) {
      JSONObject object = array.getJSONObject(j);

      DayOfWeek dayOfWeek = DayOfWeek.valueOf(object.getString("day").toUpperCase());
      Object hoursObj = object.get("hours");

      List<OpeningHours> openingHours = new ArrayList<>();
      if (hoursObj instanceof String) {
        OpeningHours openingHour = extractOpeningHours((String) hoursObj);
          if (openingHour != null) {
              openingHours.add(openingHour);
          }
      } else if (hoursObj instanceof JSONArray hoursArray) {
        for (int i = 0; i < hoursArray.length(); i++) {
          String hours = hoursArray.getString(i);
          OpeningHours openingHour = extractOpeningHours(hours);
            if (openingHour != null) {
                openingHours.add(openingHour);
            }
        }
      }

      map.put(dayOfWeek, openingHours);
    }
    return map;
  }

  private static OpeningHours extractOpeningHours(String hours) {
    if (hours.equals("Closed")) {
      return null;
    }
    String[] hoursSplit = hours.split(" to ");

    String startTimeString = hoursSplit[0].trim();
    String endTimeString = hoursSplit[1].trim();

    Object[] objectsEnd = extractOpeningHours(endTimeString, null);
    LocalTime endTime = (LocalTime) objectsEnd[0];
    String period = (String) objectsEnd[1];

    Object[] objectsStart = extractOpeningHours(startTimeString, period);
    LocalTime startTime = (LocalTime) objectsStart[0];

    return new OpeningHours(startTime, endTime);
  }

  private static Object[] extractOpeningHours(String timeString, String period) {
    LocalTime time;
    if (timeString.contains("PM")) {
      String timeTrim = timeString.replace("PM", "").replaceAll("[\\p{Z}\\s]", "");
      time = pm(timeTrim);
      period = "PM";
    } else if (timeString.contains("AM")) {
      String timeTrim = timeString.replace("AM", "").replaceAll("[\\p{Z}\\s]", "");
      time = am(timeTrim);
      period = "AM";
    } else {
      String timeTrim = timeString.replaceAll("[\\p{Z}\\s]", "");
      if (period.equals("AM")) {
        time = am(timeTrim);
      } else {
        time = pm(timeTrim);
      }
    }
    return new Object[]{time, period};
  }

  private static LocalTime pm(String time) {
    String[] timeSplit = time.split(":");

    int hour;
    int minute = 0;
    if (timeSplit.length == 1) {
      hour = Integer.parseInt(timeSplit[0].trim());
    } else {
      hour = Integer.parseInt(timeSplit[0].trim());
      minute = Integer.parseInt(timeSplit[1].trim());
    }

    if (hour != 12) {
      hour += 12;
    }

    return LocalTime.of(hour % 24, minute);
  }

  private static LocalTime am(String time) {
    String[] timeSplit = time.split("[:\\.]");

    int hour;
    int minute = 0;
    if (timeSplit.length == 1) {
      hour = Integer.parseInt(timeSplit[0].trim());
    } else {
      hour = Integer.parseInt(timeSplit[0].trim());
      minute = Integer.parseInt(timeSplit[1].trim());
    }

    if (hour == 12) {
      hour = 0;
    }

    return LocalTime.of(hour, minute);
  }
}
