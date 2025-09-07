package com.patria.apps.product.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(title = "Product List Request")
public class ProductListRequest {
 
    private Integer page;

    private Integer perPage;

    private String sort;
    
    private String name;
    
    private Integer price;
    
    private Boolean isAvailable;
    
    private Boolean isActive;
    
    private Boolean isTrash;
}
