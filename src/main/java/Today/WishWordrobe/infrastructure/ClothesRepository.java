package Today.WishWordrobe.infrastructure;

import Today.WishWordrobe.domain.Clothes;
import Today.WishWordrobe.domain.ClothingCategory;
import Today.WishWordrobe.domain.TempRange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.*;
import javax.transaction.Transactional;

@Repository
public interface ClothesRepository extends JpaRepository<Clothes,Long> {
    /**
     * 회원이랑 연결하면 쓸것들
     */

    /**
     * List<Clothes> findByUserId(Long userId);
     *     List<Clothes> findByUserIdAndCategory(Long userId, ClothingCategory category);
     *     List<Clothes> findByUserIdAndTempRange(Long userId, TempRange tempRange);
     *     List<Clothes> findByUserIdAndCategoryAndTempRange(Long userId, ClothingCategory category, TempRange tempRange);
     */

    /**
     * clothes 관련
     */


}
