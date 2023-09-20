package com.github.commerce.config.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class ExpiredUsersCouponScheduler { //매일 오전 12:00:00 유효기간 만료된 쿠폰 삭제

    @Autowired
    private JobLauncher jobLauncher; //Scheduling할 때 따로 AutoWired 해주어야함.

    @Autowired
    private Job expiredUsersCouponJob;

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul") //초 분 시 일 월 요일 (*: 매번) - 매일 오전 12:00:00 유효기간 만료된 쿠폰 삭제
    public void expiredUsersCouponJobRun() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {

        JobParameters jobParameters = new JobParameters(
                Collections.singletonMap("requestTime", new JobParameter(System.currentTimeMillis()))
        );

        jobLauncher.run(expiredUsersCouponJob, jobParameters);
    }
}
