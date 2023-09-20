package com.github.commerce.web.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductRequest {

        @NotBlank
        @Size(max = 20)
        private String name;

        private String content;

        @NotNull
        private Integer price;

        private Integer leftAmount;

        private String productCategory;

        private String ageCategory;

        private String genderCategory;

        private List<String> options;

        private String deleteThumbnailUrl;

        private List<String> deleteImageUrls;


}
