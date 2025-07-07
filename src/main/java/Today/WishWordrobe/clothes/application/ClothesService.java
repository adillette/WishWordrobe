package Today.WishWordrobe.clothes.application;

import Today.WishWordrobe.clothes.domain.Clothes;
import Today.WishWordrobe.clothes.domain.ClothingCategory;
import Today.WishWordrobe.clothes.infrastructure.ClothesRepository;
import Today.WishWordrobe.weather.domain.TempRange;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.*;
import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class ClothesService {
    /**
    @Qualifier("clothesCacheRedisTemplate")
    private final RedisTemplate<ClothesCacheKey, ClothesCacheValue> clothesRedisTemplate;
     */
    private final ClothesRepository clothesRepository;




    /**
    1. look aside 방식으로 사용자 옷장을 조회
     */

    @Cacheable(
            value="clothesCache",
            key="#userId+':'+ #tempRange.name() + ':' + (#category != null ? #category.name() : 'ALL')",
            condition = "#userId != null"  //** userId가 null이 아닐 때만 캐싱
    )
    public List<Clothes> getClothesWithCache(Long userId, TempRange tempRange, ClothingCategory category){
        //step 1: 캐시에서 데이터를 조회
      //  ClothesCacheKey cacheKey = ClothesCacheKey.of(userId, tempRange, category);

      //  ClothesCacheValue cachedValue = clothesRedisTemplate.opsForValue().get(userId);


        //        if(cachedValue != null){
        //            //캐시된 데이터가 1시간 이내이면 사용할수 있음
        //            if(cachedValue.getCacheTime().isAfter(LocalDateTime.now().minusHours(1)))
        //            return convertToClothes(cachedValue.getClothesList());
        //        }
        //step2: db에서 조회 후 캐시 저장 // 캐시 미스 또는 만료시 캐시에 데이터 없으면 db조회
//        List<Clothes> clothesFromDB;
//        if(category!=null){
//            clothesFromDB= clothesRepository.findByUserIdAndTempRangeAndCategory(userId, tempRange, category);
//        }else{
//            clothesFromDB = clothesRepository.findbyUserIdAndTempRange(userId,tempRange);
//        }
//
//            //step3. 데이터를 캐시에 저장
//        ClothesCacheValue cacheValue = convertToCacheValue(clothesFromDB);
//
//        clothesRedisTemplate.opsForValue().set(cacheKey,cacheValue, Duration.ofHours(2));
//
//        return clothesFromDB; //db에서 조회한 데이터 반환


        if(category !=null){
            return clothesRepository.findByUserIdAndTempRangeAndCategory(userId, tempRange, category);
         } else  {
            return clothesRepository.findbyUserIdAndTempRange(userId, tempRange);
        }

    }

/*
    // Write-Behind 패턴으로 옷장 수정시 캐시 무효화
    public void invalidateUserClothesCache(Long userId){
        //해당 사용자의 모든 옷장 캐시 삭제
        String pattern ="CLOTHES::" + userId +"::*";
        Set<ClothesCacheKey> keys =clothesRedisTemplate.keys(pattern);
        if(keys!=null && !keys.isEmpty()){
            clothesRedisTemplate.delete(keys);
        }
    }
*/
    /*
    2. 저장 write- behind 적용, 저장시 캐시 무효화
     */

    @CacheEvict(
            value = "clothesCache",
            key="#clothes.userId + ':*'", //해당 사용자의 모든 캐시를 삭제한다.
            allEntries = false
    )
    public Clothes save(Clothes clothes){
        return clothesRepository.save(clothes);
    }

    @CacheEvict(
            value="clothesCache",
            condition = "#userId !=null",
            allEntries = true //모든 캐시 삭제
    )
    public void invalidateUserClothesCache(Long userId){
        //메서드 바디는 암것도 없는 상태가 맞음
    }

    /*
    3. 옷 수정
     */
    @CacheEvict(
            value = "clothesCache",
            key = "#clothes.userId + ':*'",
            allEntries = false
    )

   // clothes ㅇdto 만들기 전임
    public Clothes update(Clothes clothes){
        return clothesRepository.save(clothes);
    }





    /*
    4. 옷삭제
     */
    @CacheEvict(
            value="clothesCache",
            allEntries = true //삭제할때 모든 캐시 삭제
    )
    public void deleteById(Long clothesId){
        clothesRepository.deleteById(clothesId);
    }

    /*
    5. 캐시 없이 db에서 찾기
     */
    public List<Clothes> findAll(){
        return clothesRepository.findAll();
    }

}
