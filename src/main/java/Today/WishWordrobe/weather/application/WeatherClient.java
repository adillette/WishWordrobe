package Today.WishWordrobe.weather.application;

import Today.WishWordrobe.presentation.dto.VillageForecastResponse;
import Today.WishWordrobe.weather.configuration.WeatherConfig;
import Today.WishWordrobe.weather.domain.Geographic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.*;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class WeatherClient {
    private final WebClient webClient;
    private final WeatherConfig config;

    /**
     * 격자 좌표 조회
     */
    public Mono<VillageForecastResponse> getVillageForecast(Geographic location){
        Map<String, String> baseDateTime = calculateBaseTime();
        String baseDate = baseDateTime.get("baseDate");
        String baseTime = baseDateTime.get("baseTime");


        String uri = UriComponentsBuilder.fromUriString(config.getBaseUrl() + "/getVilageFcst")
                .queryParam("serviceKey", config.getApiKey())
                .queryParam("numOfRows", 1000)
                .queryParam("pageNo", 1)
                .queryParam("dataType", "JSON")
                .queryParam("base_date", baseDate)
                .queryParam("base_time", baseTime)
                .queryParam("nx", location.getGridX())
                .queryParam("ny", location.getGridY())
                .build()
                .toUriString();

        log.info("생성된 api url: {}",uri);

        return webClient
                .get()
                .uri(uri)
                .retrieve()
                .bodyToMono(VillageForecastResponse.class)
                .doOnNext(response ->
                        log.info("Successfully retrieved forecast data for nx={}, ny={}",
                                location.getGridX(), location.getGridY()))
                .onErrorMap(e -> {
                    log.error("Error fetching forecast for nx={}, ny={}: {}",
                            location.getGridX(), location.getGridY(), e.getMessage());
                    return new RuntimeException("Error from Weather API", e);
                });
    }

    private Map<String, String> calculateBaseTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        int[] baseTimes = {2, 5, 8, 11, 14, 17, 20, 23};
        int hour = now.getHour();
        int minute = now.getMinute();

        int baseTimeIndex =-1;
        for(int i= baseTimes.length -1; i>=0; i--){
            if(hour > baseTimes[i] ||(hour == baseTimes[i] && minute>=10)){
                baseTimeIndex=i;
                break;
            }
        }

        Map<String, String> result = new HashMap<>();

        // 발표 시각이 없는 경우 (0시~2시 10분) 전날 마지막 발표 사용
        if (baseTimeIndex == -1) {
            result.put("baseDate", now.minusDays(1).format(dateFormatter));
            result.put("baseTime", "2300");
        } else {
            result.put("baseDate", now.format(dateFormatter));
            result.put("baseTime", String.format("%02d00", baseTimes[baseTimeIndex]));
        }

        return result;

    }



}
