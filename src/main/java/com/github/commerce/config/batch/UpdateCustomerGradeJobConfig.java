package com.github.commerce.config.batch;

import com.github.commerce.entity.Grade;
import com.github.commerce.entity.UsersInfo;
import com.github.commerce.repository.order.OrderRepository;
import com.github.commerce.repository.user.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Configuration
@EnableBatchProcessing //Spring Batch
@RequiredArgsConstructor
public class UpdateCustomerGradeJobConfig {

    private final UserInfoRepository userInfoRepository;
    private final OrderRepository orderRepository;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job updateCustomerGradeJob(Step updateCustomerGradeStep){
        return jobBuilderFactory.get("updateCustomerGradeJob")
                .incrementer(new RunIdIncrementer())
                .start(updateCustomerGradeStep())
                .build();
    }

    //Job 하위 Step 1개
    @JobScope
    @Bean
    public Step updateCustomerGradeStep(){
        return stepBuilderFactory.get("updateCustomerGradeStep")
                .tasklet(updateCustomerGradeTasklet())
                .build();
    }

    //Step 하위 Tasklet 1개
    @StepScope
    @Bean
    public Tasklet updateCustomerGradeTasklet(){
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                //log.info("회원 등급 조정 시작");
                List<Map<String, Object>> totalPriceSumAndUserIdList = orderRepository.getUserTotalPriceFromOneMonth(LocalDateTime.now().minusMonths(1).withDayOfMonth(1).withHour(0).withMinute(0).withNano(0)); //이전 달의 1일

                for (Map<String, Object> totalPriceSumAndUserId : totalPriceSumAndUserIdList) {
                    Long totalPrice = (Long) totalPriceSumAndUserId.get("totalPrice");
                    Long userId = (Long) totalPriceSumAndUserId.get("userId");
                    //log.info("userId={}, totalPrice={}", userId, totalPrice);

                    Optional<UsersInfo> usersInfoOptional = userInfoRepository.findByUsersId(userId);
                    if (usersInfoOptional.isEmpty()) {
                        continue;
                    }

                    UsersInfo usersInfo = usersInfoOptional.get();
                    if (100000 <= totalPrice && totalPrice < 300000) {
                        usersInfo.setGrade(Grade.ORANGE);
                    } else if (300000 <= totalPrice && totalPrice < 500000) {
                        usersInfo.setGrade(Grade.RED);
                    } else if (500000 <= totalPrice) {
                        usersInfo.setGrade(Grade.VIP);
                    }

                    //log.info("userId={}, totalPrice={}, grade={}", userId, totalPrice, usersInfo.getGrade());
                    userInfoRepository.save(usersInfo);
                }

                //log.info("회원 등급 조정 종료");
                return RepeatStatus.FINISHED;
            }
        };
    }
}
