package Today.WishWordrobe.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name="geographic_coordinate")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Geographic {

    @Id
    @Column
    private Long no;
    private String country;
    private Long area;
    private String state;
    private long gridx;
    private long gridy;
    private long longitudeHour;
    private long longitudeMinute;
    private double longitudeSecond;
    private double longitude;
    private double latitude;

}
