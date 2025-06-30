package Today.WishWordrobe.weather.configuration;

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

    @Bean
    public ReactiveMongoTemplate reactiveMongoTemplate(){
        return new ReactiveMongoTemplate(
                MongoClients.create("mongodb://localhost:27017"),
                "wishweather"
        );
    }


}
