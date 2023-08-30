package com.github.commerce.repository.cart;

import com.github.commerce.entity.mongocollection.CartSavedOption;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CartSavedOptionRepository extends MongoRepository<CartSavedOption, Integer> {

    //MongoRepository를 사용하여 데이터를 조회할 때 List를 반환하는 것은 일반적인 방법이라고 합니다.
    List<CartSavedOption> findCartSavedOptionsByUserId(Long userId);

    CartSavedOption findByOptionId(String optionId);
}
