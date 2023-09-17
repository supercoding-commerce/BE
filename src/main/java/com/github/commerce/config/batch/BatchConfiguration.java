//package com.github.commerce.config.batch;
//
//import com.github.commerce.entity.collection.Chat;
//import lombok.RequiredArgsConstructor;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
//import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
//import org.springframework.batch.item.ItemProcessor;
//import org.springframework.batch.item.data.MongoItemReader;
//import org.springframework.batch.item.data.MongoItemWriter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.mongodb.core.MongoTemplate;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@RequiredArgsConstructor
//@Configuration
//@EnableBatchProcessing
//public class BatchConfiguration {
//
//    private final JobBuilderFactory jobBuilderFactory;
//    private final StepBuilderFactory stepBuilderFactory;
//    private final MongoTemplate mongoTemplate;
//
//    // Define the job
//    @Bean
//    public Job cleanupChatJob(Step cleanupStep) {
//        return jobBuilderFactory.get("cleanupChatJob")
//                .flow(cleanupStep)
//                .end()
//                .build();
//    }
//
//    // Define the step
//    @Bean
//    public Step cleanupStep() {
//        return stepBuilderFactory.get("cleanupStep")
//                .<Chat, Chat>chunk(100)
//                .reader(mongoReader())
//                .processor(cleanupProcessor())
//                .writer(mongoWriter())
//                .build();
//    }
//
//    // Define the Reader
//    @Bean
//    public MongoItemReader<Chat> mongoReader() {
//        Map<String, Sort.Direction> sortOptions = new HashMap<>();
//        sortOptions.put("chatId", Sort.Direction.ASC);
//
//        MongoItemReader<Chat> reader = new MongoItemReader<>();
//        reader.setTemplate(mongoTemplate);
//        reader.setCollection("chat");
//        reader.setQuery("{}");
//        reader.setSort(sortOptions);
//        reader.setTargetType(Chat.class);
//        return reader;
//    }
//
//    // Define the Processor
//    @Bean
//    public ItemProcessor<Chat, Chat> cleanupProcessor() {
//        return new CleanupProcessor();
//    }
//
//    // Define the Writer
//    @Bean
//    public MongoItemWriter<Chat> mongoWriter() {
//        MongoItemWriter<Chat> writer = new MongoItemWriter<>();
//        writer.setTemplate(mongoTemplate);
//        writer.setCollection("chat");
//        return writer;
//    }
//
//    // Custom ItemProcessor to filter out chats older than 7 days
//    public static class CleanupProcessor implements ItemProcessor<Chat, Chat> {
//
//        @Override
//        public Chat process(Chat chat) throws Exception {
//            // Implement your cleanup logic here
//            // For example, filter out chats older than 7 days
//            // ...
//
//            return chat;
//        }
//    }
//}
