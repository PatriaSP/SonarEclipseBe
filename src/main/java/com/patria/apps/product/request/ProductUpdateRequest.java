package com.patria.apps.product.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(title = "Product Edit Request")
public class ProductUpdateRequest {

    @NotEmpty
    private String key;
    
    @NotEmpty
    private String categoryKey;
    
    @NotEmpty
    private String name;

    @NotNull
    private Integer price;

    @NotNull
    private Integer stock;
    
    @NotNull
    private Boolean isActive;
    
    //User ID
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
}
