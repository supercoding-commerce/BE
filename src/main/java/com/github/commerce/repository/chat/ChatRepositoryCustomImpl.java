package com.github.commerce.repository.chat;

import com.github.commerce.entity.collection.Chat;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.core.query.Query;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;

@Repository
public class ChatRepositoryCustomImpl implements ChatRepository {
    private final MongoTemplate mongoTemplate;

    public ChatRepositoryCustomImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<Chat> getUserChatList(Long userId, Long sellerId) {
        Query query = new Query(Criteria.where("userId").is(userId).and("sellerId").is(sellerId));
        query.fields().include("customRoomId", "userId", "productId","sellerId","shopName","userName", "chats");

        return mongoTemplate.find(query, Chat.class);

    }

    public List<Chat> getSellerChatList(Long sellerId, Long productId) {
        Query query = new Query(Criteria.where("sellerId").is(sellerId).and("productId").is(productId));
        query.fields().include("customRoomId", "userId", "productId","sellerId","shopName","userName", "chats");

        return mongoTemplate.find(query, Chat.class);
    }


    @Override
    public Optional<Chat> findByCustomRoomId(String customRoomId) {
        return Optional.empty();
    }


    public void cleanupOldChats() {
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        String oldestChatTimeStr = sevenDaysAgo.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        // Fetch all Chat objects
        List<Chat> chats = mongoTemplate.findAll(Chat.class, "chat");

        // Iterate through each Chat object and filter its chats field
        for (Chat chat : chats) {
            Map<String, Map<String, String>> chatMap = chat.getChats();

            if (chatMap != null) {
                Map<String, Map<String, String>> filteredChatMap = new HashMap<>();
                for (Map.Entry<String, Map<String, String>> entry : chatMap.entrySet()) {
                    if (entry.getKey().compareTo(oldestChatTimeStr) > 0) {
                        filteredChatMap.put(entry.getKey(), entry.getValue());
                    }
                }

                // Update the Chat object's chats field
                chat.setChats(filteredChatMap);
                mongoTemplate.save(chat);
            }
        }
    }

    @Override
    public <S extends Chat> S save(S entity) {
        return null;
    }

    @Override
    public <S extends Chat> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Chat> findById(ObjectId objectId) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(ObjectId objectId) {
        return false;
    }

    @Override
    public List<Chat> findAll() {
        return null;
    }

    @Override
    public Iterable<Chat> findAllById(Iterable<ObjectId> objectIds) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(ObjectId objectId) {

    }

    @Override
    public void delete(Chat entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends ObjectId> objectIds) {

    }

    @Override
    public void deleteAll(Iterable<? extends Chat> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<Chat> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Chat> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Chat> S insert(S entity) {
        return null;
    }

    @Override
    public <S extends Chat> List<S> insert(Iterable<S> entities) {
        return null;
    }

    @Override
    public <S extends Chat> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Chat> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Chat> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Chat> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Chat> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Chat> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Chat, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }


}