package Today.WishWordrobe;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResultDto {
    private boolean success;
    private String msg;
    private int code;
}
