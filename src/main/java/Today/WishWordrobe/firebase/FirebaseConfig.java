package Today.WishWordrobe.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;

@Configuration
@Slf4j
public class FirebaseConfig {

    //@PostConstruct: 빈의 초기화를 위해 모든 의존성이 주입된후에 실행됨
    public void initFirebase(){
        try{
            FileInputStream serviceAccount =
                    new FileInputStream("service-account-key.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
