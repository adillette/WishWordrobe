package Today.WishWordrobe.weather.configuration;

import com.google.api.client.util.Value;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
@EnableReactiveMongoRepositories(
        basePackages = "Today.WishWordrobe.weather",
        reactiveMongoTemplateRef = "reactiveMongoTemplate"
)
public class ReactiveMongoConfig  {

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    @Bean
    public ReactiveMongoTemplate reactiveMongoTemplate(){
        return new ReactiveMongoTemplate(
                MongoClients.create(mongoUri),
                "wishweather"
        );
    }


}
