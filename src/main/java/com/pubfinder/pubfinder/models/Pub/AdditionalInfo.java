package com.pubfinder.pubfinder.models.Pub;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

/**
 * Additional info for pub.
 */
@Entity(name = "Additional_Info")
@Table(name = "additional_info")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdditionalInfo {

  @Id
  @Column(name = "pub_id")
  private UUID id;

  @Column
  private String website;
  @Type(JsonType.class)
  @Column(columnDefinition = "jsonb")
  private Accessibility accessibility;
  @Column
  private Boolean washroom;
  @Column
  private Boolean outDoorSeating;

  @OneToOne
  @MapsId
  @JoinColumn(name = "pub_id")
  private Pub pub;
}
