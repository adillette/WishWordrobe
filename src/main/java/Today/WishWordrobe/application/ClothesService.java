package Today.WishWordrobe.application;

import Today.WishWordrobe.domain.*;
import Today.WishWordrobe.infrastructure.ClothesRepository;
import Today.WishWordrobe.presentation.dto.ClothesDto;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.*;
import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class ClothesService {
    /**
     *  Clothes addClothes(Long userId, String name, ClothingCategory category, TempRange tempRange, String imageUrl);
     *     Clothes updateClothes(Long id, String name, ClothingCategory category, TempRange tempRange, String imageUrl);
     *     void deleteClothes(Long id);
     *     List<Clothes> findClothesByUserId(Long userId);
     *     List<Clothes> findClothesByUserIdAndCategory(Long userId, ClothingCategory category);
     *     List<Clothes> findClothesByUserIdAndTempRange(Long userId, TempRange tempRange);
     *     List<Clothes> getRecommendedClothes(Long userId, int temperature);
     */

    @Qualifier("clothesCacheRedisTemplate")
    private final RedisTemplate<ClothesCacheKey, ClothesCacheValue> clothesRedisTemplate;

    private final ClothesRepository clothesRepository;



    @Transactional
    public List<Clothes> findClothes(){
        return clothesRepository.findAll();
    }

    @Transactional
    public Clothes save(Clothes clothes){
        return clothesRepository.save(clothes);
    }

    //look aside 방식으로 사용자 옷장을 조회
    public List<Clothes> getClothesIncident(Long userId, TempRange tempRange, ClothingCategory category){
        //step 1: 캐시에서 데이터를 조회
        ClothesCacheKey cacheKey = ClothesCacheKey.of(userId, tempRange, category);

        ClothesCacheValue cachedValue = clothesRedisTemplate.opsForValue().get(userId);


        if(cachedValue != null){
            //캐시된 데이터가 1시간 이내이면 사용할수 있음
            if(cachedValue.getCacheTime().isAfter(LocalDateTime.now().minusHours(1)))
            return convertToClothes(cachedValue.getClothesList());
        }
        //step2: 캐시에 데이터 없으면 db조회
        List<Clothes> clothesFromDB;
        if(category!=null){
            clothesFromDB= clothesRepository.findByUserIdAndTempRangeAndCategory(userId, tempRange, category);
        }else{
            clothesFromDB = clothesRepository.findbyUserIdAndTempRange(userId,tempRange);
        }

            //step3. 데이터를 캐시에 저장
        ClothesCacheValue cacheValue = convertToCacheValue(clothesFromDB);

        clothesRedisTemplate.opsForValue().set(cacheKey,cacheValue, Duration.ofHours(2));

        return clothesFromDB; //db에서 조회한 데이터 반환

    }

    // Write-Behind 패턴으로 옷장 수정시 캐시 무효화
    public void invalidateUwerClothesCache(Long userId){
        //해당 사용자의 모든 옷장 캐시 삭제
        String pattern ="CLOTHES::" + userId +"::*";
        Set<ClothesCacheKey> keys =clothesRedisTemplate.keys(pattern);
        if(keys!=null && !keys.isEmpty()){
            clothesRedisTemplate.delete(keys);
        }
    }


}
