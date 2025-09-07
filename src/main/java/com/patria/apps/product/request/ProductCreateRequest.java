package com.patria.apps.product.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(title = "Product Add Request")
public class ProductCreateRequest {

    @NotEmpty
    private String categoryKey;
    
    @NotEmpty
    private String name;

    @NotNull
    private Integer price;

    @NotNull
    private Integer stock;
    
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private List<MultipartFile> images;

    //User ID
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
}
