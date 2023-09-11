package com.github.commerce.repository.chat;

import com.github.commerce.entity.collection.Chat;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;
public interface ChatRepository extends MongoRepository<Chat, ObjectId> {

    List<Chat> findBySellerId(Long sellerId);

    List<Chat> findByUserId(Long userId);

    Optional<Chat> findByCustomRoomId(String customRoomId);
}
