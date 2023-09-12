package com.github.commerce.repository.chat;

import com.github.commerce.entity.collection.Chat;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.core.query.Query;

import java.util.*;
import java.util.function.Function;

@Repository
public class ChatRepositoryCustomImpl implements ChatRepository {
    private final MongoTemplate mongoTemplate;

    public ChatRepositoryCustomImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<Chat> getUserChatList(Long userId) {
        Query query = new Query(Criteria.where("userId").is(userId));
        query.fields().include("chats");

        return mongoTemplate.find(query, Chat.class);

    }

    @Override
    public List<Chat> findBySellerId(Long sellerId) {
        return null;
    }

    @Override
    public List<Chat> findByUserId(Long userId) {
        return null;
    }

    @Override
    public Optional<Chat> findByCustomRoomId(String customRoomId) {
        return Optional.empty();
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