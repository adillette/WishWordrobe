# WishWordrobe

## Webflux , Push api , MSA 프로젝트

#250506 이슈
날씨 api 컨트롤러 흐름

1.클라이언트에서 행정구역명(예: "역삼2동")을 파라미터로 컨트롤러에 요청을 보냅니다.
2. 컨트롤러는 findGeographicByLocationName 메서드를 호출하여:

JSON 파일(MongoDB에 저장된 파일 또는 static 폴더에 있는 파일)에서 지역 정보를 찾습니다.
ObjectMapper를 사용하여 JSON 파일을 파싱합니다.
입력받은 지역명과 일치하는 행정구역을 찾아 Geographic 객체를 생성하여 반환합니다.
일치하는 지역이 없을 경우 null을 반환합니다 (이게 마지막 return null의 이유입니다. 모든 지역을 검색했지만 일치하는 것이 없을 때 null을 반환).


3.컨트롤러는 찾은 Geographic 객체의 격자 좌표(nx, ny)를 사용하여 WeatherClient를 통해 기상청 API에 요청을 보냅니다.
4.기상청 API가 응답(VillageForecastResponse)을 반환하면:

convertToWeatherForecastDTO 메서드를 사용하여 API 응답을 WeatherForecastDTO로 변환합니다.
5.카테고리별로 날씨 정보(온도, 습도, 강수확률 등)를 분류하고 필요한 변환을 수행합니다.
날짜와 시간 형식을 적절히 변환합니다.


6.최종적으로 WeatherForecastDTO를 JSON 형태로 클라이언트에 반환합니다.
