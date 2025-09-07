package com.patria.apps.product.serializer;

import com.patria.apps.config.AESHelperService;
import com.patria.apps.entity.Product;
import com.patria.apps.entity.ProductReview;
import com.patria.apps.exception.GeneralException;
import com.patria.apps.helper.MinioHelperService;
import com.patria.apps.product.response.ProductListResponse;
import com.patria.apps.product.response.ProductReviewResponse;
import com.patria.apps.response.ReadResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductSerializerService {

    private final AESHelperService aesService;
    private final MinioHelperService minioHelperService;
    
    public ProductListResponse serialize(
            Product product
    ) {
        try {
            ProductListResponse.ProductListResponseBuilder builder = ProductListResponse.builder()
                    .key(aesService.encrypt(String.valueOf(product.getId())))
                    .categoryKey(aesService.encrypt(String.valueOf(product.getCategory().getId())))
                    .categoryName(product.getCategory().getName())
                    .name(product.getName())
                    .price(product.getPrice())
                    .stock(product.getStock())
                    .isActive(product.isActive())
                    ;
            List<ProductReviewResponse> productReviewList = new ArrayList<>();
            for(ProductReview review : product.getProductReview()){
                productReviewList.add(ProductReviewResponse.builder()
                        .key(aesService.encrypt(String.valueOf(review.getId())))
                        .review(review.getReview())
                        .userName(review.getUsers().getFirstName() + "" + review.getUsers().getLastName())
                        .score(review.getScore())
                        .build());
            }
            builder.productReview(productReviewList)
                    .images(minioHelperService.listImageUrlsInFolder(product.getImage()));
      
            return builder.build();
        } catch (Exception e) {
            throw new GeneralException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error!", e);
        }
    }

    public ReadResponse serializeTransaction(String message) {
        return ReadResponse.builder()
                .status(HttpStatus.OK.value())
                .message(message)
                .build();
    }
    
}
