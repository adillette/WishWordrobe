package Today.WishWordrobe.infrastructure;

import Today.WishWordrobe.presentation.dto.PushNotificationRequest;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface PushNotificationRepository extends ReactiveMongoRepository<PushNotificationRequest,String> {
}
