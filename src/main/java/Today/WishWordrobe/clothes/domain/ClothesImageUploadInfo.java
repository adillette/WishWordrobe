package Today.WishWordrobe.clothes.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ClothesImageUploadInfo {
    private final Long id;
    private final  Long clothesId;

    private final String imageName;

    private final String imagePath;

    private final int seq;

    public ClothesImageUploadInfo(Long clothesId,String imageName, String imagePath, int seq) {
        this.id = null;
        this.clothesId=clothesId;
        this.imageName = imageName;
        this.imagePath = imagePath;
        this.seq = seq;
    }
    public ClothesImageUploadInfo(String imageName, String imagePath, int seq) {
        this.id = null;
        this.clothesId=null;
        this.imageName = imageName;
        this.imagePath = imagePath;
        this.seq = seq;
    }
}
