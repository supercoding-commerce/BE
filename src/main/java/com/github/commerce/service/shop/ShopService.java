package com.github.commerce.service.shop;

import com.github.commerce.entity.Product;
import com.github.commerce.entity.Seller;
import com.github.commerce.repository.product.ProductRepository;
import com.github.commerce.service.order.util.ValidateOrderMethod;
import com.github.commerce.web.dto.product.SellingProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ShopService {

    private final ValidateOrderMethod validateOrderMethod;
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<SellingProductDto> getSellingProducts(Long userId) {
        Seller seller = validateOrderMethod.validateSellerByUserId(userId);
        List<Product> productList = productRepository.findProductsBySellerIdAndIsDeleted(seller.getId(), false);

        return productList.stream().map(SellingProductDto::new).collect(Collectors.toList());
    }

}
