package Today.WishWordrobe.domain;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ClothesInfo {
    private final Long id;
    private final Long clothesId;
    private final String name;
    private final ClothingCategory category;
    private final String imageUrl;

    public ClothesInfo(Long id,Long clothesId, String name, ClothingCategory category, String imageUrl) {
        this.id = id;
        this.clothesId=clothesId;
        this.name = name;
        this.category = category;
        this.imageUrl = imageUrl;
    }
}
