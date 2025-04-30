package Today.WishWordrobe.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name="vilageforecast")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VilageForecst {

    //@GeneratedValue()
    @Id
    private long no;
    private long baseDate;
    private long fcstDate;
    private long fcstTime;
    private long x;
    private long y;
    private String category;
    private String fcstValue;

    public VilageForecst(long no, long baseDate, long fcstDate, long fcstTime, long x, long y, String category, String fcstValue) {
        this.no = no;
        this.baseDate = baseDate;
        this.fcstDate = fcstDate;
        this.fcstTime = fcstTime;
        this.x = x;
        this.y = y;
        this.category = category;
        this.fcstValue = fcstValue;
    }
}
