//package com.github.commerce.web.controller.order;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
//
//import java.io.IOException;
//import java.time.Duration;
//
//@RequiredArgsConstructor
//@RestController
//@RequestMapping("/sse")
//public class SSEController {
//    private final SseEmitters sseEmitters;
//
//    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    public ResponseEntity<SseEmitter> connect() {
//        SseEmitter emitter = new SseEmitter();
//        sseEmitters.add(emitter);
//        try {
//            emitter.send(SseEmitter.event()
//                    .name("connect")
//                    .data("connected!"));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return ResponseEntity.ok(emitter);
//    }
//    @GetMapping(value = "/sendEvent", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    public Flux<String> sendEvent() {
//        // 구체적인 이벤트 생성 로직을 여기에 추가
//        return Flux.just("상품 재고 부족 알림 1", "상품 재고 부족 알림 2", "상품 재고 부족 알림 3")
//                .delayElements(Duration.ofSeconds(1)); // 1초 간격으로 이벤트를 발생시킵니다.
//    }
//
//
////    // 판매자에게 SSE 알림을 보내는 메서드
////    public void sendNotificationToClient(Long sellerUserId, String message) {
////        // 실제로 어떻게 SSE 알림을 생성하고 반환할지 구현합니다.
////    }
//}
