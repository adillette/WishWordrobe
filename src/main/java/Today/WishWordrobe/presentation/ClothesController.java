package Today.WishWordrobe.presentation;

import Today.WishWordrobe.ResourceNotFoundException2;
import Today.WishWordrobe.application.ClothesService;
import Today.WishWordrobe.domain.Clothes;
import Today.WishWordrobe.domain.ClothingCategory;
import Today.WishWordrobe.domain.TempRange;
import Today.WishWordrobe.domain.User;
import Today.WishWordrobe.infrastructure.ClothesRepository;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping(value="/wishwardrobe")
public class ClothesController{//아래 setter 바꿔야한다

   // private final ClothesRepository clothesRepository;
    private final ClothesService clothesService;

    public ClothesController(ClothesService clothesService) {
        this.clothesService = clothesService;
    }

    /*
     ★★ 추천★★★
     */

    @GetMapping("/recommendations")
    public ResponseEntity<List<Clothes>> getRecommendedClothes(
            @RequestParam Long userId,
            @RequestParam int temperature,
            @RequestParam(required = false) ClothingCategory category  ){
        log.info("옷 추천 요청(캐시 포함) : userId={} , temp={},category={}",userId, temperature, category);

        TempRange tempRange = TempRange.fromTemperature(temperature); //온도를 범위로 변경
        List<Clothes> clothes = clothesService.getClothesWithCache(userId, tempRange, category);

        return ResponseEntity.ok(clothes);
    }
    /*
    옷장에 save 시킴
     */
    @PostMapping
    public ResponseEntity<Clothes> addClothes(@RequestBody Clothes clothes){
        //save 메서드가 자동으로 캐시 무효화 처리해줌
        Clothes savedClothes = clothesService.save(clothes);

        log.info("새옷 추가 및 캐시 무효화 완료: userId={}" ,clothes.getUserId());
        return ResponseEntity.ok(savedClothes);
    }

    /*
    userid로 업데이트
     */
    @PostMapping
    public ResponseEntity<Clothes> updateClothes(
            @PathVariable Long id,
            @RequestBody Clothes clothes){
        clothes.setId(id);
        Clothes updatedClothes = clothesService.update(clothes);

        return ResponseEntity.ok(updatedClothes);
    }

    /*
    해당 id 내용 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        clothesService.deleteById(id);
        return ResponseEntity.ok().build();
    }


    /*
      관리자용 전체 옷장조회 (캐시 없이 그냥 db 조회)
    */
    @GetMapping
    public String getAllClothes(Model model){
        List<Clothes> clothesList =  clothesService.findAll();
        model.addAttribute("clothesList", clothesList);//뷰(HTML 템플릿)에서는 이 이름(clothesList)으로 데이터에 접근
        return "clothes/list";
    }



    //등록 get
    @GetMapping("/add")
    public String addClothes(Model model){
        Clothes clothes = new Clothes();
        model.addAttribute("clothes",clothes);
        return "/addClothes";
    }

    //등록 post
    @PostMapping("/add")
    public String addClothes(@Valid @ModelAttribute("clothes") Clothes clothes,
                             BindingResult result) {
        if (result.hasErrors()) {
            return "clothes/addForm";
        }
        clothesService.save(clothes);//저장
        return "redirect:/wishwardrobe";
    }

    /*
    //수정
    @GetMapping("/{clothesId}/edit")
    public String showEditForm(@PathVariable Long clothesId, Model model){
        Clothes clothes = clothesRepository.findById(clothesId)
                .orElseThrow(() -> new ResourceNotFoundException2("Clothes not found"));
        model.addAttribute("clothes", clothes);
        return "수정창";
        }
    @PostMapping("/{clothesId}/edit")
    public String edit(@PathVariable Long clothesId,
                       @Valid  @ModelAttribute Clothes clothes,
                       BindingResult result){
        if (result.hasErrors()) {
            return "clothes/editForm";
        }
        Clothes existClothes = clothesRepository.findById(clothesId)
                        .orElseThrow(()-> new ResourceNotFoundException2("Clothes not found"))
        clothesRepository.update(clothesId, clothes);
        return "redirect:/wishwardrobe/{clothesId}";
    }
    // 삭제 처리
    @PostMapping("/{clothesId}/delete")
    public String deleteClothes(@PathVariable Long clothesId) {
        clothesRepository.deleteById(clothesId);
        return "redirect:/wishwardrobe";
    }
    //옷 상세페이지
    @GetMapping("/{clothesId}")
    public String 메서드이름(@PathVariable Long clothesId, Model model){
        Clothes clothes = clothesRepository.findById(clothesId)
                .orElseThrow(()-> new ResourceNotFoundException2("Clothes is not found"));
        model.addAttribute("clothes", clothes);
        return "clothes/detail";
    }

*/
}
