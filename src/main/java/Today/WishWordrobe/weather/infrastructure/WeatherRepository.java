package Today.WishWordrobe.weather.infrastructure;


import Today.WishWordrobe.weather.domain.VilageForecst;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherRepository extends ReactiveMongoRepository<VilageForecst,Long> {
}
