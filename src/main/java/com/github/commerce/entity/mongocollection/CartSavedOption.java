package com.github.commerce.entity.mongocollection;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "cart_options")
public class CartSavedOption {
    @Id // MongoDB ObjectId
    private String optionId;

    private Long userId;

    private Long productId;

    private Map<String, String> options;

}
