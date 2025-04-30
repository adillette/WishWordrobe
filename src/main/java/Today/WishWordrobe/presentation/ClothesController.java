package Today.WishWordrobe.presentation;

import Today.WishWordrobe.ResourceNotFoundException2;
import Today.WishWordrobe.application.ClothesService;
import Today.WishWordrobe.domain.Clothes;
import Today.WishWordrobe.domain.User;
import Today.WishWordrobe.infrastructure.ClothesRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import javax.validation.Valid;


@Controller
@RequestMapping(value="/wishwardrobe")
public class ClothesController{//아래 setter 바꿔야한다

   // private final ClothesRepository clothesRepository;
    private final ClothesService clothesService;

    public ClothesController(ClothesService clothesService) {
        this.clothesService = clothesService;
    }

//    public ClothesController(ClothesRepository clothesRepository) {
//        this.clothesRepository = clothesRepository;
//    }


    //리스트(유저별 옷장조회)
    @GetMapping
    public String getAllClothes(Model model){

       // Long userId = ((User) userDetails).getId(); // 현재 로그인한 사용자 ID 가져오기
        List<Clothes> clothesList = clothesService.findClothes();
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
