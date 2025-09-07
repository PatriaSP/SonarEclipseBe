package com.patria.apps.product.service;

import com.patria.apps.product.request.ProductCreateRequest;
import com.patria.apps.product.request.ProductDeleteRequest;
import com.patria.apps.product.request.ProductDestroyRequest;
import com.patria.apps.product.request.ProductListRequest;
import com.patria.apps.product.request.ProductRestoreRequest;
import com.patria.apps.product.request.ProductUpdateRequest;
import com.patria.apps.product.response.ProductListResponse;
import com.patria.apps.response.ReadResponse;
import java.util.List;

public interface ProductService {

    ReadResponse<List<ProductListResponse>> list(ProductListRequest request);

    ReadResponse createProduct(ProductCreateRequest request);

    ReadResponse deleteProduct(ProductDeleteRequest request);

    ReadResponse restoreProduct(ProductRestoreRequest request);

    ReadResponse destroyProduct(ProductDestroyRequest request);

    ReadResponse updateProduct(ProductUpdateRequest request);

}
