package com.github.commerce.web.dto.order;

import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Setter
@Getter
public class GetOrderDto {
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response{
        private List<OrderDto> content;

        public static List<OrderDto> fromPage(Page<OrderDto> page){
            return page.getContent();
        }
    }

}

