package Today.WishWordrobe.application;

import Today.WishWordrobe.domain.Clothes;
import Today.WishWordrobe.infrastructure.ClothesRepository;
import Today.WishWordrobe.presentation.dto.ClothesDto;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;
import javax.transaction.Transactional;

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

    private final ClothesRepository clothesRepository;



    @Transactional
    public List<Clothes> findClothes(){
        return clothesRepository.findAll();
    }
    @Transactional
    public Clothes save(Clothes clothes){
        return clothesRepository.save(clothes);
    }



}
