package com.patria.apps.product.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(title = "Product List Response")
public class ProductListResponse {

    private String key;
    
    private String categoryKey;
    
    private String categoryName;
    
    private String name;
    
    private Double price;
    
    private Integer stock;
    
    private List<ProductReviewResponse> productReview;
    
    private boolean isActive;
    
    private List<String> images;
}
