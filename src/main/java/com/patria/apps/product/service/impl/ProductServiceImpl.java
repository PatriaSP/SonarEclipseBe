package com.patria.apps.product.service.impl;

import com.patria.apps.config.AESHelperService;
import com.patria.apps.entity.Category;
import com.patria.apps.entity.Product;
import com.patria.apps.exception.GeneralException;
import com.patria.apps.helper.MinioHelperService;
import com.patria.apps.helper.SortHelperService;
import com.patria.apps.helper.ValidationHelperService;
import com.patria.apps.product.filter.ProductFilterService;
import com.patria.apps.product.request.ProductCreateRequest;
import com.patria.apps.product.request.ProductDeleteRequest;
import com.patria.apps.product.request.ProductDestroyRequest;
import com.patria.apps.product.request.ProductListRequest;
import com.patria.apps.product.request.ProductRestoreRequest;
import com.patria.apps.product.request.ProductUpdateRequest;
import com.patria.apps.product.response.ProductListResponse;
import com.patria.apps.product.serializer.ProductSerializerService;
import com.patria.apps.product.service.ProductService;
import com.patria.apps.repository.CategoryRepository;
import com.patria.apps.repository.ProductRepository;
import com.patria.apps.response.PaginationResponse;
import com.patria.apps.response.ReadResponse;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SortHelperService sortHelperService;
    private final ValidationHelperService validationHelperService;
    private final AESHelperService aesService;
    private final MinioHelperService minioService;
    private final ProductSerializerService productSerializerService;
    private final ProductFilterService productFilterService;

    private final Map<String, String> sortMap = new HashMap<String, String>() {
        {
            //parameter and column name
            put("name", "name");
            put("-name", "name");
            put("price", "price");
            put("-price", "price");
            put("stock", "stock");
            put("-stock", "stock");
        }
    };

    @Override
    public ReadResponse<List<ProductListResponse>> list(ProductListRequest request) {
        Sort sortBy = sortHelperService.setSort(sortMap, request.getSort(), "name");
        Pageable pageable = PageRequest.of((request.getPage() - 1), request.getPerPage(), sortBy);
        Page<Product> product = productRepository.findAll(productFilterService.specification(request), pageable);
        List<ProductListResponse> usersResponse = product
                .getContent()
                .stream()
                .map(datas -> productSerializerService.serialize(
                datas
        ))
                .toList();

        Page<ProductListResponse> responsePage = new PageImpl<>(usersResponse, pageable,
                product.getTotalElements());

        return ReadResponse.<List<ProductListResponse>>builder()
                .status(HttpStatus.OK.value())
                .data(responsePage.getContent())
                .pagination(
                        PaginationResponse.builder()
                                .currentPage(responsePage.getNumber() + 1)
                                .totalPage(responsePage.getTotalPages())
                                .perPage(responsePage.getSize())
                                .total(responsePage.getTotalElements())
                                .count(responsePage.getNumberOfElements())
                                .hasNext(responsePage.hasNext())
                                .hasPrevious(responsePage.hasPrevious())
                                .hasContent(responsePage.hasContent())
                                .build())
                .build();
    }

    @Override
    public ReadResponse createProduct(ProductCreateRequest request) {
        validationHelperService.validate(request);

        Product product = new Product();
        product.setName(request.getName());

        Long categoryId = aesService.getDecryptedString(request.getCategoryKey());
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new GeneralException(HttpStatus.NOT_FOUND, "Data Category Not Found")
        );

        product.setCategory(category);
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setImage(category.getName() + "/" + request.getName());
        product.setCreatedAt(LocalDateTime.now());
        product.setCreatedBy(request.getId());
        product.setActive(Boolean.TRUE);

        if (request.getImages() != null) {
            for (MultipartFile images : request.getImages()) {
                try {
                    minioService.putObject(images, product.getImage() + "/" + images.getOriginalFilename(), images.getContentType());
                } catch (Exception ex) {
                    throw new GeneralException(HttpStatus.INTERNAL_SERVER_ERROR, "Error put object storage!", ex);
                }
            }
        }

        productRepository.save(product);
        productRepository.flush();

        return productSerializerService.serializeTransaction(
                "Success create product!"
        );
    }

    @Override
    public ReadResponse deleteProduct(ProductDeleteRequest request) {
        validationHelperService.validate(request);

        Long id = aesService.getDecryptedString(request.getKey());
        Product product = productRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(
                () -> new GeneralException(HttpStatus.NOT_FOUND, "Data Not Found")
        );

        product.setActive(Boolean.FALSE);
        product.setDeletedBy(request.getId());
        product.setDeletedAt(LocalDateTime.now());

        productRepository.save(product);
        productRepository.flush();

        return productSerializerService.serializeTransaction(
                "Success delete product!"
        );
    }

    @Override
    public ReadResponse restoreProduct(ProductRestoreRequest request) {
        validationHelperService.validate(request);

        Long id = aesService.getDecryptedString(request.getKey());
        Product product = productRepository.findByIdAndDeletedAtIsNotNull(id).orElseThrow(
                () -> new GeneralException(HttpStatus.NOT_FOUND, "Data Not Found")
        );

        product.setActive(Boolean.FALSE);
        product.setDeletedBy(null);
        product.setDeletedAt(null);
        product.setUpdatedBy(request.getId());
        product.setUpdatedAt(LocalDateTime.now());

        productRepository.save(product);
        productRepository.flush();

        return productSerializerService.serializeTransaction(
                "Success restore product!"
        );
    }

    @Override
    public ReadResponse destroyProduct(ProductDestroyRequest request) {
        validationHelperService.validate(request);

        Long id = aesService.getDecryptedString(request.getKey());
        Product product = productRepository.findByIdAndDeletedAtIsNotNull(id).orElseThrow(
                () -> new GeneralException(HttpStatus.NOT_FOUND, "Data Not Found")
        );

        try {
            minioService.removeFolder(product.getImage());
        } catch (Exception ex) {
            throw new GeneralException(HttpStatus.INTERNAL_SERVER_ERROR, "Error remove object storage!", ex);
        }

        productRepository.delete(product);

        return productSerializerService.serializeTransaction(
                "Success destroy product!"
        );
    }

    @Override
    public ReadResponse updateProduct(ProductUpdateRequest request) {
        validationHelperService.validate(request);

        Long id = aesService.getDecryptedString(request.getKey());
        Product product = productRepository.findById(id).orElseThrow(
                () -> new GeneralException(HttpStatus.NOT_FOUND, "Data Not Found")
        );

        product.setName(request.getName());

        Long categoryId = aesService.getDecryptedString(request.getCategoryKey());
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new GeneralException(HttpStatus.NOT_FOUND, "Data Category Not Found")
        );

        product.setCategory(category);
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setImage(category.getName() + "/" + request.getName());
        product.setUpdatedAt(LocalDateTime.now());
        product.setUpdatedBy(request.getId());
        product.setActive(request.getIsActive());

        productRepository.save(product);
        productRepository.flush();

        return productSerializerService.serializeTransaction(
                "Success update product!"
        );
    }

    @Override
    public ReadResponse<ProductListResponse> retrieveSingleData(String id) {
        
        if(id.isBlank()){
            throw new GeneralException(HttpStatus.INTERNAL_SERVER_ERROR, "Id is not found");
        }
        
        Long decryptedId = aesService.getDecryptedString(id);

        Product product = productRepository.findById(decryptedId).orElseThrow(
                () -> new GeneralException(HttpStatus.NOT_FOUND, "Data Not Found")
        );
        return productSerializerService.serializeRetrieve(product);
    }

}
