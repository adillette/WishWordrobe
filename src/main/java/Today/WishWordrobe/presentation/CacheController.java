package Today.WishWordrobe.presentation;

import Today.WishWordrobe.clothes.application.ClothesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class CacheController {

    private final ClothesService clothesService;



    /*
    2. 옷장 api 캐시 적용
     */



}
