package Today.WishWordrobe.presentation;

import Today.WishWordrobe.application.ClothesService;
import Today.WishWordrobe.domain.Clothes;
import Today.WishWordrobe.domain.ClothingCategory;
import Today.WishWordrobe.weather.domain.TempRange;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(controllers = ClothesController.class)
@AutoConfigureMockMvc(addFilters = false)
class ClothesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClothesService clothesService;

    @DisplayName("위시옷장에 옷등록 성공")
    @Test
    void addSucess() throws Exception{
        //given
        Clothes clothes = createClothes();
        //when
        ResultActions resultActions = mockMvc.perform(
                post("/wishwordrobe/add")
                        .flashAttr("clothes",clothes)
        );
        //then
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/wishwordrobe"));
        verify(clothesService,times(1)).save(any(Clothes.class));
    }

    private Clothes createClothes() {
        return Clothes.builder()
                .userId(1L)
                .name("내가 젤 좋아하는 톰보이코트")
                .category(ClothingCategory.OUTER)
                .tempRange(TempRange.COLD)
                .imageUrl("image.jpg")
                .build();
    }
}
