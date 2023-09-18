package com.github.commerce.service.product.util;

import com.github.commerce.entity.Product;
import com.github.commerce.entity.Seller;
import com.github.commerce.entity.User;
import com.github.commerce.repository.cart.CartRepository;
import com.github.commerce.repository.order.OrderRepository;
import com.github.commerce.repository.product.ProductRepository;
import com.github.commerce.repository.user.SellerRepository;
import com.github.commerce.repository.user.UserRepository;
import com.github.commerce.service.order.exception.OrderErrorCode;
import com.github.commerce.service.order.exception.OrderException;
import com.github.commerce.service.product.exception.ProductErrorCode;
import com.github.commerce.service.product.exception.ProductException;
import com.github.commerce.web.advice.custom.CustomException;
import com.github.commerce.web.advice.custom.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Transactional
public class ValidateProductMethod {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final SellerRepository sellerRepository;

    public User validateUser(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new OrderException(OrderErrorCode.USER_NOT_FOUND));
    }
    public Seller validateSeller(Long userId){
        return sellerRepository.findByUsersId(userId)
                .orElseThrow(() -> new ProductException(ProductErrorCode.NOT_REGISTERED_SELLER));
    }

    public boolean isThisProductSeller(Long sellerId, Long userId) {
        Optional<Seller> sellerOptional = sellerRepository.findByUsersId(userId);
        return sellerOptional.isPresent() && sellerOptional.get().getId().equals(sellerId);
    }

    public Product validateProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(ProductErrorCode.NOTFOUND_PRODUCT));
    }
    public void validateImage(List<MultipartFile> imageFiles) {
        long maxFileSize = 5L * 1024 * 1024; // 5 MB in bytes

        for (MultipartFile file : imageFiles) {
            if (file.getSize() > maxFileSize) {
                throw new ProductException(ProductErrorCode.HEAVY_FILESIZE);
            }
        }
    }

}
