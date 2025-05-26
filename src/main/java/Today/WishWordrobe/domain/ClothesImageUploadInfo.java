package Today.WishWordrobe.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ClothesImageUploadInfo {
    private Long id;
    private String imageName;

    private String imagePath;

    private int seq;
}
