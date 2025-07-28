package Today.WishWordrobe.clothes.application;

import Today.WishWordrobe.clothes.domain.Clothes;
import Today.WishWordrobe.clothes.infrastructure.ClothesRepository;
import Today.WishWordrobe.presentation.dto.ClothesDto;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static Today.WishWordrobe.clothes.domain.ClothingCategory.TOP;
import static Today.WishWordrobe.weather.domain.TempRange.HOT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class ClothesServiceTest1 {

    @Autowired
    private ClothesService clothesService;

    @Autowired
    private ClothesRepository clothesRepository;

    @Test
    public void 옷장에_옷_등록_테스트(){
        //given
        Clothes saveclothes = new Clothes(1L,"aland",TOP,HOT, "C:/downloads");


        //when
        clothesService.save(saveclothes);

        //then
        assertThat(saveclothes).isNotNull();
        assertThat(saveclothes.getUserId()).isEqualTo(1L);
        assertThat(saveclothes.getName()).isEqualTo("aland");
        assertThat(saveclothes.getCategory()).isEqualTo(TOP);
        assertThat(saveclothes.getTempRange()).isEqualTo(HOT);
        assertThat(saveclothes.getImageUrl()).isEqualTo("C:/downloads");

        //db에 저장이 되고 있나?
        Optional<Clothes> findClothes = clothesRepository.findById(saveclothes.getClothesId());
        assertThat(findClothes).isPresent();
        assertThat(findClothes.get().getName()).isEqualTo("aland");


    }

}