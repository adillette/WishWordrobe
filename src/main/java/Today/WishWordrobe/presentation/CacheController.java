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



}
