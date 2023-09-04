package com.github.commerce.web.controller.order;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@Slf4j
public class SseEmitters {

    /**
     * 주의할 점은 이 콜백이 SseEmitter를 관리하는 다른 스레드에서 실행된다는 것입니다.
     * 따라서 thread-safe한 자료구조를 사용하지 않으면 ConcurrnetModificationException이 발생할 수 있습니다.
     * 여기서는 thread-safe한 자료구조인 CopyOnWriteArrayList를 사용하였습니다.
     *
     */
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    SseEmitter add(SseEmitter emitter) {
        this.emitters.add(emitter);
        log.info("new emitter added: {}", emitter);
        log.info("emitter list size: {}", emitters.size());
        emitter.onCompletion(() -> {
            log.info("onCompletion callback");
            this.emitters.remove(emitter);    // 만료되면 리스트에서 삭제
        });
        emitter.onTimeout(() -> {
            log.info("onTimeout callback");
            emitter.complete();
        });

        return emitter;
    }
}
