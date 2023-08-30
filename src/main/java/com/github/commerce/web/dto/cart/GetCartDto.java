package com.github.commerce.web.dto.cart;

import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Setter
@Getter
public class GetCartDto {
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response{
        private List<CartDto> content;

        public static List<CartDto> from(Page<CartDto> page){
            return page.getContent();
        }
    }

}
