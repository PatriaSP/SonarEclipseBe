package com.patria.apps.product.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(title = "Product Delete Request")
public class ProductDeleteRequest {

    @NotEmpty
    private String key;

    //User ID
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
}
