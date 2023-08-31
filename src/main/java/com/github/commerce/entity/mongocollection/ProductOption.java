package com.github.commerce.entity.mongocollection;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "product_options")
public class ProductOption {
    private int productId;
    private String content;
}
