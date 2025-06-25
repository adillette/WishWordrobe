package Today.WishWordrobe.application;

import Today.WishWordrobe.WebPush.WebPushNotificationRequest;
import Today.WishWordrobe.WebPush.WebPushService;
import Today.WishWordrobe.WebPush.WebPushSubscription;
import Today.WishWordrobe.firebase.FCMPushNotificationRequest;
import Today.WishWordrobe.firebase.FCMService;
import Today.WishWordrobe.presentation.dto.PushNotificationRequest;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PushNotificationServiceTest {

    @Mock
    private FCMService fcmService;

    @Mock
    private WebPushService webPushService;

    private PushNotificationService pushNotificationService;

    @BeforeEach
    void setUp(){
        pushNotificationService = new PushNotificationService(fcmService,webPushService);
    }

    @Test
    void saveSubscription() {
    }

    @Test
    void sendNotification() {
        //given
        Map<String, Object> raqSubscription = new HashMap<>();
        raqSubscription.put("endpoint","https://test-endpoint.com");

        Map<String, String> keys = new HashMap<>();
        keys.put("p256dh","test-p256dh-key");
        keys.put("auth","test-auth-key");
        raqSubscription.put("keys",keys);

        //when
        Mono<Void> result = pushNotificationService.saveSubscription(raqSubscription);

        //then
        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void sendNotification_sendFcmtokenMessage(){
        //given
        String expectedMessageId = "test-message-id";

        FCMPushNotificationRequest request = FCMPushNotificationRequest.builder()
                .title("Test Title")
                .message("Test Message")
                .token("test-device-token")
                .build();
        when(fcmService.sendTokenMessage(any(FCMPushNotificationRequest.class)))
                .thenReturn(Mono.just(expectedMessageId));

        //when
        Mono<Map<String, Object>> result = pushNotificationService.sendNotification(request);
        //then
        StepVerifier.create(result)
                .expectNextMatches(map->{
                    assertEquals("success",map.get("fcmResult"));
                    assertEquals(expectedMessageId,map.get("fcmMessageId"));
                    return true;
                })
                .verifyComplete();

        verify(fcmService,times(1)).sendTokenMessage(any(FCMPushNotificationRequest.class));
        verify(fcmService,never()).sendTopicMessage(any(FCMPushNotificationRequest.class));
        verify(webPushService,never()).sendNotification(any(WebPushNotificationRequest.class));

    }

    @Test
    void sendNotification_sendFcmTopicMessage(){
        //given
        String expectedMessageId ="test-topic-message-id";

        FCMPushNotificationRequest request =FCMPushNotificationRequest.builder()
                .title("Test topic title")
                .message("test topic message")
                .topic("test-topic")
                .build();
        when(fcmService.sendTopicMessage(any(FCMPushNotificationRequest.class)))
                .thenReturn(Mono.just(expectedMessageId));
        //when
        Mono<Map<String, Object>> result= pushNotificationService.sendNotification(request);

        //then
        StepVerifier.create(result)
                .expectNextMatches(map->{
                    assertEquals("success",map.get("topicResult"));
                    assertEquals(expectedMessageId,map.get("topicMessageId"));
                    return true;
                })
                .verifyComplete();

        verify(fcmService,never()).sendTokenMessage(any(FCMPushNotificationRequest.class));
        verify(fcmService,times(1)).sendTopicMessage(any(FCMPushNotificationRequest.class));
        verify(webPushService,never()).sendNotification(any(WebPushNotificationRequest.class));
    }

    @Test
    void sendNotification_sendWebPushNotification(){
        //given
        WebPushSubscription.Keys keys = new WebPushSubscription.Keys();
        keys.setP256dh("test-p256dh-key");
        keys.setAuth("test-auth-key");

        WebPushSubscription subscription = new WebPushSubscription();
        subscription.setEndpoint("https://test-endpoint.com");
        subscription.setKeys(keys);

        WebPushNotificationRequest request = WebPushNotificationRequest.builder()
                .title("Test Web Push title")
                .message("test web push message")
                .subscription(subscription)
                .build();

        when(webPushService.sendNotification(any(WebPushNotificationRequest.class)))
                .thenReturn(Mono.empty());

        //when
        Mono<Map<String, Object>> result = pushNotificationService.sendNotification(request);
        //then
        StepVerifier.create(result)
                .expectNextMatches(map ->{
                    assertEquals("success",map.get("webpushresult"));
                    return true;
                })
                .verifyComplete();
        verify(fcmService,never()).sendTokenMessage(any(FCMPushNotificationRequest.class));
        verify(fcmService,never()).sendTopicMessage(any(FCMPushNotificationRequest.class));
        verify(webPushService,times(1)).sendNotification(any(WebPushNotificationRequest.class));
    }

    @Test
    void sendNotification_Broadcast_allsubsc(){
        //given
        Map<String, Object> rawSubscription1 = new HashMap<>();
        rawSubscription1.put("endpoint", "http://test-endpoint1.com");

        Map<String, String> keys1= new HashMap<>();
        keys1.put("p256dh", "test-p256dh-key1");
        keys1.put("auth", "test-auth-key1");
        rawSubscription1.put("keys",keys1);

        Map<String, Object> rawSubscription2 = new HashMap<>();
        rawSubscription2.put("endpoint","http://test-endpoint2.com");

        Map<String, String> keys2 = new HashMap<>();
        keys1.put("p256dh", "test-p256dh-key2");
        keys1.put("auth", "test-auth-key2");
        rawSubscription2.put("keys2",keys2);

        pushNotificationService.saveSubscription(rawSubscription1).block();
        pushNotificationService.saveSubscription(rawSubscription2).block();

        PushNotificationRequest request = PushNotificationRequest.builder()
                .title("broadcast title")
                .message("broadcast message")
                .build();

        when(webPushService.sendNotification(any(WebPushNotificationRequest.class)))
                .thenReturn(Mono.empty());
        //when
        Mono<Map<String, Object>> result = pushNotificationService.sendNotification(request);
        //then
        StepVerifier.create(result)
                .expectNextMatches(map->{
                    assertEquals(2,map.get("broadcastCount"));
                    return true;
                })
                .verifyComplete();

        verify(webPushService,times(2)).sendNotification(any(WebPushNotificationRequest.class));
    }
    @Test
    void sendNotification_shouldHandleFCMError() {
        // Arrange
        FCMPushNotificationRequest request = FCMPushNotificationRequest.builder()
                .title("Test Error Title")
                .message("Test Error Message")
                .token("test-device-token")
                .build();

        when(fcmService.sendTokenMessage(any(FCMPushNotificationRequest.class)))
                .thenReturn(Mono.error(new RuntimeException("FCM Error")));

        // Act
        Mono<Map<String, Object>> result = pushNotificationService.sendNotification(request);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(map -> {
                    assertEquals("FCM Error", map.get("fcmError"));
                    return true;
                })
                .verifyComplete();
    }

    @Test
    void sendNotification_shouldHandleWebPushError() {
        // Arrange
        WebPushSubscription.Keys keys = new WebPushSubscription.Keys();
        keys.setP256dh("test-p256dh-key");
        keys.setAuth("test-auth-key");

        WebPushSubscription subscription = new WebPushSubscription();
        subscription.setEndpoint("https://test-endpoint.com");
        subscription.setKeys(keys);

        WebPushNotificationRequest request = WebPushNotificationRequest.builder()
                .title("Test Web Push Error Title")
                .message("Test Web Push Error Message")
                .subscription(subscription)
                .build();

        when(webPushService.sendNotification(any(WebPushNotificationRequest.class)))
                .thenReturn(Mono.error(new RuntimeException("Web Push Error")));

        // Act
        Mono<Map<String, Object>> result = pushNotificationService.sendNotification(request);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(map -> {
                    assertEquals("Web Push Error", map.get("webPushError"));
                    return true;
                })
                .verifyComplete();
    }

}