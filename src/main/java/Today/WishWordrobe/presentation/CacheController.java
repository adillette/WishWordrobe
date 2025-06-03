package Today.WishWordrobe.presentation;

import Today.WishWordrobe.application.ClothesService;
import Today.WishWordrobe.domain.Clothes;
import Today.WishWordrobe.domain.ClothingCategory;
import Today.WishWordrobe.domain.TempRange;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class CacheController {

    private final ClothesService clothesService;



    /*
    2. 옷장 api 캐시 적용
     */

    @GetMapping("/clothes/recommendations")
    public ResponseEntity<List<Clothes>> getRecommendedClothes(
            @RequestParam Long userId,
            @RequestParam int temperature,
            @RequestParam(required = false)ClothingCategory category  ){
        log.info("옷 추천 요청(캐시 포함) : userId={} , temp={},category={}",userId, temperature, category);

        TempRange tempRange = TempRange.fromTemperature(temperature); //온도를 범위로 변경
        List<Clothes> clothes = clothesService.getClothesWithCaches(userId, tempRange, category);

        return ResponseEntity.ok(clothes);
    }

    @PostMapping("/clothes")
    public ResponseEntity<Clothes> addClothes(@RequestBody Clothes clothes){
        //옷 저장 로직
        Clothes savedClothes = clothesService.save(clothes);

        //해당 사용자의 옷장 캐시 무효화
        clothesService.invalidateUserClothesCache(clothes.getUserId());

        log.info("새옷 추가 및 캐시 무효화 완료: userId={}" ,clothes.getUserId());
        return ResponseEntity.ok(savedClothes);
    }


}
