package Today.WishWordrobe.infrastructure;

import Today.WishWordrobe.weather.application.WeatherClient;
import Today.WishWordrobe.weather.configuration.WeatherConfig;
import Today.WishWordrobe.weather.domain.Geographic;
import Today.WishWordrobe.presentation.dto.VillageForecastResponse;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WeatherClientTest {

    private MockWebServer mockWebServer;
    private WeatherClient weatherClient;
    private WeatherConfig weatherConfig;
    private Geographic location;

    @BeforeEach
    void setUp() throws IOException {
        // MockWebServer 설정
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        // Mock WeatherConfig 설정
        weatherConfig = mock(WeatherConfig.class);
        when(weatherConfig.getBaseUrl()).thenReturn(mockWebServer.url("").toString());
        when(weatherConfig.getApiKey()).thenReturn("테스트_API_키");

        // 실제 WebClient로 테스트
        WebClient webClient = WebClient.builder().build();

        // WeatherClient 생성
        weatherClient = new WeatherClient(webClient, weatherConfig);

        // 테스트 위치 데이터 설정 - nx: 62, ny: 125
        location = Geographic.builder()
                .gridX(62)
                .gridY(125)
                .build();
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void testGetVillageForecast_Success() throws InterruptedException {
        // 모의 응답 설정
        String responseBody = createMockJsonResponse();
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(responseBody)
        );

        // API 호출
        Mono<VillageForecastResponse> result = weatherClient.getVillageForecast(location);

        // 응답 검증
        StepVerifier.create(result)
                .expectNextMatches(response -> {
                    // 응답 코드 확인
                    assertEquals("00", response.getResponse().getHeader().getResultCode());
                    assertEquals("NORMAL_SERVICE", response.getResponse().getHeader().getResultMsg());

                    // 응답 바디 확인
                    assertTrue(response.getResponse().getBody().getTotalCount() > 0);

                    // 첫 번째 아이템 확인
                    VillageForecastResponse.Item item = response.getResponse().getBody().getItems().getItem().get(0);
                    assertEquals("TMP", item.getCategory());
                    assertEquals("15", item.getFcstValue());
                    assertEquals(62, item.getNx());
                    assertEquals(125, item.getNy());

                    return true;
                })
                .verifyComplete();

        // 요청 검증
        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        String requestPath = recordedRequest.getPath();

        // nx=62, ny=125 확인
        assertTrue(requestPath.contains("nx=62"));
        assertTrue(requestPath.contains("ny=125"));

        // baseTime=0500 확인 (테스트에서는 고정값 사용)
        assertTrue(requestPath.contains("base_time="));
    }

    @Test
    void testGetVillageForecast_Error() {
        // 에러 응답 설정
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(500)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody("{\"error\": \"Internal Server Error\"}")
        );

        // API 호출
        Mono<VillageForecastResponse> result = weatherClient.getVillageForecast(location);

        // 응답 검증 - 에러 발생 확인
        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof RuntimeException &&
                                throwable.getMessage().contains("Error from Weather API"))
                .verify();
    }

    @Test
    void testGetVillageForecast_EmptyResponse() {
        // 비어있는 응답 설정
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody("{}")
        );

        // API 호출
        Mono<VillageForecastResponse> result = weatherClient.getVillageForecast(location);

        // 응답 검증 - 비어있는 응답은 필드가 null인 객체
        StepVerifier.create(result)
                .expectNextMatches(response->response.getResponse()==null)
                .verifyComplete();
    }

    private String createMockJsonResponse() {
        // 오늘 날짜 사용
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        return "{\n" +
                "  \"response\": {\n" +
                "    \"header\": {\n" +
                "      \"resultCode\": \"00\",\n" +
                "      \"resultMsg\": \"NORMAL_SERVICE\"\n" +
                "    },\n" +
                "    \"body\": {\n" +
                "      \"dataType\": \"JSON\",\n" +
                "      \"pageNo\": 1,\n" +
                "      \"numOfRows\": 1000,\n" +
                "      \"totalCount\": 10,\n" +
                "      \"items\": {\n" +
                "        \"item\": [\n" +
                "          {\n" +
                "            \"baseDate\": \"" + today + "\",\n" +
                "            \"baseTime\": \"0500\",\n" +
                "            \"category\": \"TMP\",\n" +
                "            \"fcstDate\": \"" + today + "\",\n" +
                "            \"fcstTime\": \"0600\",\n" +
                "            \"fcstValue\": \"15\",\n" +
                "            \"nx\": 62,\n" +
                "            \"ny\": 125\n" +
                "          },\n" +
                "          {\n" +
                "            \"baseDate\": \"" + today + "\",\n" +
                "            \"baseTime\": \"0500\",\n" +
                "            \"category\": \"SKY\",\n" +
                "            \"fcstDate\": \"" + today + "\",\n" +
                "            \"fcstTime\": \"0600\",\n" +
                "            \"fcstValue\": \"1\",\n" +
                "            \"nx\": 62,\n" +
                "            \"ny\": 125\n" +
                "          },\n" +
                "          {\n" +
                "            \"baseDate\": \"" + today + "\",\n" +
                "            \"baseTime\": \"0500\",\n" +
                "            \"category\": \"PTY\",\n" +
                "            \"fcstDate\": \"" + today + "\",\n" +
                "            \"fcstTime\": \"0600\",\n" +
                "            \"fcstValue\": \"0\",\n" +
                "            \"nx\": 62,\n" +
                "            \"ny\": 125\n" +
                "          },\n" +
                "          {\n" +
                "            \"baseDate\": \"" + today + "\",\n" +
                "            \"baseTime\": \"0500\",\n" +
                "            \"category\": \"REH\",\n" +
                "            \"fcstDate\": \"" + today + "\",\n" +
                "            \"fcstTime\": \"0600\",\n" +
                "            \"fcstValue\": \"70\",\n" +
                "            \"nx\": 62,\n" +
                "            \"ny\": 125\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";
    }
}