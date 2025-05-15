package Today.WishWordrobe.domain;

import java.util.Objects;
import java.time.LocalDate;

public class WeatherCacheKey {
    private static final String PREFIX="WEATHER::";

    private String location;
    private LocalDate date;

    private WeatherCacheKey(String location,LocalDate date){
        if(Objects.isNull(location))
            throw new IllegalArgumentException("location can't be null");
        if(Objects.isNull(date))
            throw new IllegalArgumentException("date can't be null");
        this.location=location;
        this.date = date;
    }
}
