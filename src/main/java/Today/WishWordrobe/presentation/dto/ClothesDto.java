package Today.WishWordrobe.presentation.dto;

import Today.WishWordrobe.domain.ClothingCategory;
import Today.WishWordrobe.weather.domain.TempRange;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ClothesDto {
    private Long id;

    @NotBlank(message = "의류 이름은 필수입니다")
    private String name;

    @NotBlank(message = "의류 종류는 필수입니다")
    private ClothingCategory category;

    @NotNull(message="온도 범위 지정은 필수입니다.")
    private TempRange tempRange;

    private String imageUrl;
}
