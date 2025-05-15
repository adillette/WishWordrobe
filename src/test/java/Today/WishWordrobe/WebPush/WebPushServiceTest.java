package Today.WishWordrobe.WebPush;

import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WebPushServiceTest {

    @Mock
    private PushService pushService;

    private WebPushService webPushService;

    @BeforeEach
    void setUp(){
        webPushService = new WebPushService(pushService);
    }

    @Test
    void sendNotification_shouldSendNotificationSuccessfully() throws Exception{

        WebPushSubscription.Keys keys = new WebPushSubscription.Keys();
        keys.setP256dh("test-p256dh-key");
        keys.setAuth("test-auth-key");

        WebPushSubscription subscription = new WebPushSubscription();
        subscription.setEndpoint("https://test-endpoint.com");
        subscription.setKeys(keys);

        //푸시 알림 요청 객체 생성
        Map<String, String> data = new HashMap<>();
        data.put("testKey","testValue");

        WebPushNotificationRequest request = WebPushNotificationRequest.builder()
                .title("Test Title")
                .message("Test Message")
                .icon("test-icon.png")
                .clickAction("https://test-action.com")
                .data(data)
                .url("https://test-url.com")
                .subscription(subscription)
                .build();
        //pushservice.sent 메소드가 호출될때 예외를 던지지 않도록 설정
        doNothing().when(pushService).send(any(Notification.class));
        //act
        Mono<Void> result = webPushService.sendNotification(request);

        //assert
        //mono가 완료되는지 확인
        StepVerifier.create(result)
                .verifyComplete();

        verify(pushService,times(1));
    }


}