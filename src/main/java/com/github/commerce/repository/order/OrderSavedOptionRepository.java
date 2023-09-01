package com.github.commerce.repository.order;

import com.github.commerce.entity.mongocollection.CartSavedOption;
import com.github.commerce.entity.mongocollection.OrderSavedOption;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderSavedOptionRepository extends MongoRepository<OrderSavedOption, ObjectId> {
    List<OrderSavedOption> findAllByOrderId(Long orderId);

    OrderSavedOption findByOrderId(Long orderId);

    List<OrderSavedOption> findAllByUserId(Long userId);

    void deleteByOrderId(Long orderId);

    OrderSavedOption findByOrderIdAndUserId(Long orderId, Long userId);
}
