//package com.github.commerce.service.user;
//
//import com.github.commerce.entity.Grade;
//import com.github.commerce.entity.UsersInfo;
//import com.github.commerce.repository.order.OrderRepository;
//import com.github.commerce.repository.user.UserInfoRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class UserInfoService {
//
//    private final UserInfoRepository userInfoRepository;
//    private final OrderRepository orderRepository;
//
      /**Spring Batch로 전환하였습니다.*/
//    //주기적으로 전체 회원 등급 조정 - 매월 1일 오전 12시 기준 구매 금액 정산
//    @Scheduled(cron = "0 0 0 1 * *", zone = "Asia/Seoul")
//    @Transactional
//    public void calculateNewCustomerGrade(){
//
//        List<Map<String, Object>> totalPriceSumAndUserIdList = orderRepository.getUserTotalPriceFromOneMonth(LocalDateTime.now().minusMonths(1).withDayOfMonth(1).withHour(0).withMinute(0).withNano(0)); //이전 달의 1일
//
//        for(Map<String, Object> totalPriceSumAndUserId : totalPriceSumAndUserIdList){
//            Long totalPrice = (Long)totalPriceSumAndUserId.get("totalPrice");
//            Long userId = (Long) totalPriceSumAndUserId.get("userId");
//
//            Optional<UsersInfo> usersInfoOptional = userInfoRepository.findByUsersId(userId);
//            if(usersInfoOptional.isEmpty()){
//                continue;
//            }
//
//            UsersInfo usersInfo = usersInfoOptional.get();
//            if(100000<=totalPrice && totalPrice<300000){
//                usersInfo.setGrade(Grade.ORANGE);
//            }
//            else if(300000<=totalPrice && totalPrice<500000){
//                usersInfo.setGrade(Grade.RED);
//            }
//            else if(500000<=totalPrice){
//                usersInfo.setGrade(Grade.VIP);
//            }
//
//            userInfoRepository.save(usersInfo);
//        }
//    }
//}
