package Today.WishWordrobe.firebase;

import com.google.api.core.ApiFuture;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MessagingErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class FCMServiceTest {

    private FCMService fcmService;

    @Mock
    private FirebaseMessaging firebaseMessaging;

    @BeforeEach
    void setUp() {
        fcmService = new FCMService();
    }

    @Test
    void sendPushNotification_shouldSendMessage() {
        // Arrange
        String expectedMessageId = "message-id-123";
        Map<String, String> data = new HashMap<>();
        data.put("key1", "value1");

        FCMPushNotificationRequest request = FCMPushNotificationRequest.builder()
                .title("Test Title")
                .message("Test Message")
                .icon("test-icon.png")
                .clickAction("https://test-action.com")
                .data(data)
                .token("test-device-token")
                .build();

        // Firebase SDK를 정적으로 모킹
        try (MockedStatic<FirebaseMessaging> mockedFirebaseMessaging = mockStatic(FirebaseMessaging.class)) {
            mockedFirebaseMessaging.when(FirebaseMessaging::getInstance).thenReturn(firebaseMessaging);

            CompletableFuture<String> future = CompletableFuture.completedFuture(expectedMessageId);
            when(firebaseMessaging.sendAsync(any(Message.class))).thenReturn((ApiFuture<String>) future);

            // Act
            Mono<String> result = fcmService.sendPushNotification(request);

            // Assert
            StepVerifier.create(result)
                    .expectNext(expectedMessageId)
                    .verifyComplete();

            // FirebaseMessaging.getInstance().sendAsync가 호출되었는지 확인
            verify(firebaseMessaging, times(1)).sendAsync(any(Message.class));
        }
    }

    @Test
    void sendTopicMessage_shouldSendMessage() {
        // Arrange
        String expectedMessageId = "topic-message-id-123";
        Map<String, String> data = new HashMap<>();
        data.put("key1", "value1");

        FCMPushNotificationRequest request = FCMPushNotificationRequest.builder()
                .title("Test Topic Title")
                .message("Test Topic Message")
                .data(data)
                .topic("test-topic")
                .build();

        try (MockedStatic<FirebaseMessaging> mockedFirebaseMessaging = mockStatic(FirebaseMessaging.class)) {
            mockedFirebaseMessaging.when(FirebaseMessaging::getInstance).thenReturn(firebaseMessaging);

            CompletableFuture<String> future = CompletableFuture.completedFuture(expectedMessageId);
            when(firebaseMessaging.sendAsync(any(Message.class))).thenReturn((ApiFuture<String>) future);

            // Act
            Mono<String> result = fcmService.sendTopicMessage(request);

            // Assert
            StepVerifier.create(result)
                    .expectNext(expectedMessageId)
                    .verifyComplete();

            verify(firebaseMessaging, times(1)).sendAsync(any(Message.class));
        }
    }
    @Test
    void sendTokenMessage_shouldSendMessage() {
        // Arrange
        String expectedMessageId = "token-message-id-123";
        Map<String, String> data = new HashMap<>();
        data.put("key1", "value1");

        FCMPushNotificationRequest request = FCMPushNotificationRequest.builder()
                .title("Test Token Title")
                .message("Test Token Message")
                .data(data)
                .token("test-device-token")
                .build();

        try (MockedStatic<FirebaseMessaging> mockedFirebaseMessaging = mockStatic(FirebaseMessaging.class)) {
            mockedFirebaseMessaging.when(FirebaseMessaging::getInstance).thenReturn(firebaseMessaging);

            CompletableFuture<String> future = CompletableFuture.completedFuture(expectedMessageId);
            when(firebaseMessaging.sendAsync(any(Message.class))).thenReturn((ApiFuture<String>) future);

            // Act
            Mono<String> result = fcmService.sendTokenMessage(request);

            // Assert
            StepVerifier.create(result)
                    .expectNext(expectedMessageId)
                    .verifyComplete();

            verify(firebaseMessaging, times(1)).sendAsync(any(Message.class));
        }
    }

    @Test
    void sendPushNotification_shouldHandleException() {
        // Arrange
        FCMPushNotificationRequest request = FCMPushNotificationRequest.builder()
                .title("Test Title")
                .message("Test Message")
                .token("test-device-token")
                .build();

        try (MockedStatic<FirebaseMessaging> mockedFirebaseMessaging = mockStatic(FirebaseMessaging.class)) {
            mockedFirebaseMessaging.when(FirebaseMessaging::getInstance).thenReturn(firebaseMessaging);

            // ExecutionException을 사용하여 CompletableFuture 실패 모킹
            CompletableFuture<String> future = new CompletableFuture<>();
            future.completeExceptionally(new ExecutionException(
                    new RuntimeException("Test error message")
            ));

            when(firebaseMessaging.sendAsync(any(Message.class))).thenReturn((ApiFuture<String>) future);

            // Act
            Mono<String> result = fcmService.sendPushNotification(request);

            // Assert
            StepVerifier.create(result)
                    .expectError(RuntimeException.class)
                    .verify();

            verify(firebaseMessaging, times(1)).sendAsync(any(Message.class));
        }
    }


}