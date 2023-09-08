package com.github.commerce.web.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    @NotBlank
    @Size(max = 255)
    private String name;

    private String content;

    @NotNull
    private Long price;

    private Long leftAmount;

    private Integer productCategory;

    private Integer ageCategory;

    private Integer genderCategory;


}

