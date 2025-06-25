package Today.WishWordrobe.WebPush;

import Today.WishWordrobe.presentation.dto.PushNotificationRequest;
import lombok.*;
import lombok.experimental.SuperBuilder;

import Map;


@Getter
@Setter
@SuperBuilder
public class WebPushNotificationRequest extends PushNotificationRequest {
    //웹 푸시 전용 필드
    private WebPushSubscription subscription;


    public WebPushNotificationRequest(String title, String message, String icon, String clickAction,
                                      Map<String, String> data, String url, WebPushSubscription subscription) {
        super(title, message, icon, clickAction, data, url);
        this.subscription = subscription;
    }
}
