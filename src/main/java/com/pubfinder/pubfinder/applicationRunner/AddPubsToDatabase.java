package com.pubfinder.pubfinder.applicationRunner;

import static com.pubfinder.pubfinder.applicationRunner.extractInfo.getAccessibility;
import static com.pubfinder.pubfinder.applicationRunner.extractInfo.getOutDoorSeating;
import static com.pubfinder.pubfinder.applicationRunner.extractInfo.getStringOrNull;
import static com.pubfinder.pubfinder.applicationRunner.extractInfo.getWashroom;
import static com.pubfinder.pubfinder.applicationRunner.extractTime.openingHours;

import com.pubfinder.pubfinder.db.PubRepository;
import com.pubfinder.pubfinder.models.Pub.Accessibility;
import com.pubfinder.pubfinder.models.Pub.AdditionalInfo;
import com.pubfinder.pubfinder.models.Pub.OpeningHours;
import com.pubfinder.pubfinder.models.Pub.Pub;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

// @Component
public class AddPubsToDatabase implements ApplicationRunner {

  @Autowired
  private PubRepository pubRepository;

  @Autowired
  private ResourceLoader resourceLoader;

  @Override
  public void run(ApplicationArguments args) throws Exception {
    String json = getJson();
    JSONArray jsonArray = new JSONArray(json);
    List<Pub> pubs = parseThroughArray(jsonArray);

    for (Pub pub : pubs) {
      System.out.println(pub.toString());
      pubRepository.save(pub);
    }
  }

  private String getJson() throws IOException {
    Resource resource = resourceLoader.getResource("classpath:static/dataset.json");
    File file = resource.getFile();
    FileReader reader = new FileReader(file);
    StringBuilder stringBuilder = new StringBuilder();
    int character;
    while ((character = reader.read()) != -1) {
      stringBuilder.append((char) character);
    }
    reader.close();
    return stringBuilder.toString();
  }

  private List<Pub> parseThroughArray(JSONArray jsonArray) throws JSONException {
    List<Pub> pubs = new ArrayList<>();
    for (int i = 0; i < jsonArray.length(); i++) {
      JSONObject obj = jsonArray.getJSONObject(i);

      JSONObject geo = obj.getJSONObject("location");

      Map<DayOfWeek, List<OpeningHours>> openingHours = null;
      if (!obj.getJSONArray("openingHours").isEmpty()) {
        openingHours = openingHours(obj.getJSONArray("openingHours"));
      }

      String description = getStringOrNull(obj, "description");
      String price = getStringOrNull(obj, "price");
      String website = getStringOrNull(obj, "website");

      Boolean washroom = false;
      if (obj.getJSONObject("additionalInfo").has("Amenities")) {
        washroom = getWashroom(obj.getJSONObject("additionalInfo").getJSONArray("Amenities"));
      }

      Boolean outDoorSeating = false;
      if (obj.getJSONObject("additionalInfo").has("Service options")) {
        outDoorSeating = getOutDoorSeating(
            obj.getJSONObject("additionalInfo").getJSONArray("Service options"));
      }
      Accessibility accessibility;
      if (obj.getJSONObject("additionalInfo").has("Accessibility")) {
        accessibility = getAccessibility(
            obj.getJSONObject("additionalInfo").getJSONArray("Accessibility"));
      } else {
        accessibility = Accessibility.builder().build();
      }

      Pub pub = Pub.builder()
          .name(obj.getString("title"))
          .lat(geo.getDouble("lat"))
          .lng(geo.getDouble("lng"))
          .openingHours(openingHours)
          .description(description)
          .location(obj.getString("address"))
          .lat(geo.getDouble("lat"))
          .lng(geo.getDouble("lng"))
          .price(price)
          .additionalInfo(
              AdditionalInfo.builder()
                  .website(website)
                  .accessibility(accessibility)
                  .washroom(washroom)
                  .outDoorSeating(outDoorSeating)
                  .build()
          )
          .build();
      pubs.add(pub);
    }
    return pubs;
  }
}
