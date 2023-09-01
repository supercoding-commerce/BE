package com.github.commerce.entity.mongocollection;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "order_options")
public class OrderSavedOption {
    @Id // MongoDB ObjectId
    private String optionId;

    private Long userId;

    private Long productId;

    private Long orderId;

    private Map<String, String> options;

}
