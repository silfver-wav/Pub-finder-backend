package com.pubfinder.pubfinder.models;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Entity(name = "Pub")
@Table(name = "pub")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Pub {
    @Id
    @GeneratedValue
    @Column(unique = true, nullable = false)
    private UUID id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Double lat;
    @Column(nullable = false)
    private Double lng;
    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private Map<DayOfWeek, List<OpeningHours>> openingHours;
    @Column(nullable = false)
    private String location;
    @Column()
    private String description;
    @Column()
    private String price;
    @Column()
    private String website;
    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private Accessibility accessibility;
    @Column()
    private Boolean washroom;
    @Column()
    private Boolean outDoorSeating;

    @Override
    public String toString() {
        return "Pub{" + "\n" +
                "id=" + id + "\n" +
                ", name='" + name + '\'' + "\n" +
                ", lat=" + lat + "\n" +
                ", lng=" + lng + "\n" +
                ", openingHours=" + openingHours + "\n" +
                ", location='" + location + '\'' + "\n" +
                ", description='" + description + '\'' + "\n" +
                ", price='" + price + '\'' + "\n" +
                ", website='" + website + '\'' + "\n" +
                ", accessibility=" + accessibility + "\n" +
                ", washroom=" + washroom + "\n" +
                ", outDoorSeating=" + outDoorSeating + "\n" +
                '}';
    }
}