package com.patria.apps.controller;

import com.patria.apps.helper.SecurityHelperService;
import com.patria.apps.product.request.ProductCreateRequest;
import com.patria.apps.product.request.ProductDeleteRequest;
import com.patria.apps.product.request.ProductDestroyRequest;
import com.patria.apps.product.request.ProductListRequest;
import com.patria.apps.product.request.ProductRestoreRequest;
import com.patria.apps.product.request.ProductUpdateRequest;
import com.patria.apps.product.response.ProductListResponse;
import com.patria.apps.product.serializer.ProductSerializerService;
import com.patria.apps.product.service.ProductService;
import com.patria.apps.response.ReadResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1/api/product")
@Tag(name = "Product Controller")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductSerializerService productSerializerService;

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ReadResponse<List<ProductListResponse>> list(
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "perPage", required = false, defaultValue = "5") Integer perPage,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "price", required = false) Integer price,
            @RequestParam(value = "isAvailable", required = false) Boolean isAvailable
    ) {
        ProductListRequest request = ProductListRequest.builder()
                .page(page)
                .perPage(perPage)
                .sort(sort)
                .name(name)
                .price(price)
                .isAvailable(isAvailable)
                .build();

        return productService.list(request);
    }

    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ReadResponse create(
            HttpServletRequest servletRequest,
            @RequestParam(value = "categoryKey", required = false) String categoryKey,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "price", required = false) Integer price,
            @RequestParam(value = "stock", required = false) Integer stock,
            @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) {
        ProductCreateRequest.ProductCreateRequestBuilder request = ProductCreateRequest.builder()
                .id(SecurityHelperService.getPrincipal().getId())
                .categoryKey(categoryKey)
                .name(name)
                .stock(stock)
                .price(price)
                .images(images);

        return productService.createProduct(request.build());
    }

    @PostMapping(
            path = "/update",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ReadResponse update(
            HttpServletRequest servletRequest,
            @RequestBody ProductUpdateRequest request
    ) {
        request.setId(SecurityHelperService.getPrincipal().getId());

        return productService.updateProduct(request);

    }

    @DeleteMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ReadResponse delete(
            @RequestBody ProductDeleteRequest request
    ) {
        request.setId(SecurityHelperService.getPrincipal().getId());

        return productService.deleteProduct(request);
    }

    @DeleteMapping(
            path = "/destroy",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ReadResponse destroy(
            @RequestBody ProductDestroyRequest request
    ) {
        request.setId(SecurityHelperService.getPrincipal().getId());

        return productService.destroyProduct(request);
    }

    @PatchMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ReadResponse restore(
            @RequestBody ProductRestoreRequest request
    ) {
        request.setId(SecurityHelperService.getPrincipal().getId());

        return productService.restoreProduct(request);
    }
}
