package com.patria.apps.transaction.service.impl;

import com.patria.apps.config.AESHelperService;
import com.patria.apps.entity.Basket;
import com.patria.apps.entity.Product;
import com.patria.apps.exception.GeneralException;
import com.patria.apps.helper.SortHelperService;
import com.patria.apps.helper.ValidationHelperService;
import com.patria.apps.repository.BasketRepository;
import com.patria.apps.repository.ProductRepository;
import com.patria.apps.repository.UsersRepository;
import com.patria.apps.response.PaginationResponse;
import com.patria.apps.response.ReadResponse;
import com.patria.apps.transaction.filter.BasketFilterService;
import com.patria.apps.transaction.request.BasketCreateRequest;
import com.patria.apps.transaction.request.BasketUpdateRequest;
import com.patria.apps.transaction.request.BasketListRequest;
import com.patria.apps.transaction.request.BasketDestroyRequest;
import com.patria.apps.transaction.response.BasketListResponse;
import com.patria.apps.transaction.serializer.BasketSerializerService;
import com.patria.apps.transaction.service.BasketService;
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

@Service
@RequiredArgsConstructor
public class BasketServiceImpl implements BasketService {

    private final BasketRepository basketRepository;
    private final ProductRepository productRepository;
    private final UsersRepository usersRepository;
    private final SortHelperService sortHelperService;
    private final ValidationHelperService validationHelperService;
    private final AESHelperService aesService;
    private final BasketFilterService basketFilterService;
    private final BasketSerializerService basketSerializerService;

    private final Map<String, String> sortMap = new HashMap<String, String>() {
        {
            //parameter and column name
            put("productName", "product.name");
            put("-productName", "product.name");
            put("qty", "qty");
            put("-qty", "qty");
        }
    };

    @Override
    public ReadResponse<List<BasketListResponse>> list(BasketListRequest request) {
        Sort sortBy = sortHelperService.setSort(sortMap, request.getSort(), "date", "desc");
        Pageable pageable = PageRequest.of((request.getPage() - 1), request.getPerPage(), sortBy);
        Page<Basket> basket = basketRepository.findAll(basketFilterService.specification(request), pageable);
        List<BasketListResponse> usersResponse = basket
                .getContent()
                .stream()
                .map(datas -> basketSerializerService.serialize(
                datas
        ))
                .toList();

        Page<BasketListResponse> responsePage = new PageImpl<>(usersResponse, pageable,
                basket.getTotalElements());

        return ReadResponse.<List<BasketListResponse>>builder()
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
    public ReadResponse createBasket(BasketCreateRequest request) {
        validationHelperService.validate(request);

        if(request.getQty() == 0){
            throw new GeneralException(HttpStatus.NOT_ACCEPTABLE, "Please add quantity!");
        }
                
        Long productId = aesService.getDecryptedString(request.getProductKey());
        Product product = productRepository.findByIdAndDeletedAtIsNull(productId).orElseThrow(
                () -> new GeneralException(HttpStatus.NOT_FOUND, "Data Not Found")
        );

        if (product.getStock() == 0) {
            throw new GeneralException(HttpStatus.INTERNAL_SERVER_ERROR, "Out of stock!");
        }

        Basket basket = basketRepository.findByUsersIdAndProductId(request.getId(), productId).orElse(new Basket());
        basket.setProduct(product);
        if (request.getQty() > product.getStock()) {
            basket.setQty(product.getStock());
        } else {
            basket.setQty(request.getQty() + basket.getQty());
        }
        basket.setUsers(usersRepository.findById(request.getId()).get());
        basket.setDate(LocalDateTime.now());

        basketRepository.save(basket);
        basketRepository.flush();

        return basketSerializerService.serializeTransaction(
                "Success add basket!"
        );
    }

    @Override
    public ReadResponse destroyBasket(BasketDestroyRequest request) {
        validationHelperService.validate(request);

        Long id = aesService.getDecryptedString(request.getKey());
        Basket basket = basketRepository.findById(id).orElseThrow(
                () -> new GeneralException(HttpStatus.NOT_FOUND, "Data Not Found")
        );

        if (!request.getId().equals(basket.getUsers().getId())) {
            throw new GeneralException(HttpStatus.FORBIDDEN, "You cannot delete this data");
        }

        basketRepository.delete(basket);
        basketRepository.flush();

        return basketSerializerService.serializeTransaction(
                "Success delete basket!"
        );
    }

    @Override
    public ReadResponse updateBasket(BasketUpdateRequest request) {
        validationHelperService.validate(request);

        Long id = aesService.getDecryptedString(request.getKey());
        Basket basket = basketRepository.findById(id).orElseThrow(
                () -> new GeneralException(HttpStatus.NOT_FOUND, "Data Not Found")
        );

        if (!request.getId().equals(basket.getUsers().getId())) {
            throw new GeneralException(HttpStatus.FORBIDDEN, "You cannot delete this data");
        }

        if (request.getQty() == 0) {
            basketRepository.delete(basket);
            basketRepository.flush();

            return basketSerializerService.serializeTransaction(
                    "Success delete basket!"
            );
        }

        if (request.getQty() > basket.getProduct().getStock()) {
            basket.setQty(basket.getProduct().getStock());
        } else {
            basket.setQty(request.getQty());
        }

        basketRepository.save(basket);
        basketRepository.flush();

        return basketSerializerService.serializeTransaction(
                "Success update basket!"
        );
    }

    @Override
    public ReadResponse<BasketListResponse> retrieveSingleData(String id) {
        
        if(id.isBlank()){
            throw new GeneralException(HttpStatus.INTERNAL_SERVER_ERROR, "Id is not found");
        }
        
        Long decryptedId = aesService.getDecryptedString(id);

        Basket basket = basketRepository.findById(decryptedId).orElseThrow(
                () -> new GeneralException(HttpStatus.NOT_FOUND, "Data Not Found")
        );
        return basketSerializerService.serializeRetrieve(basket);
    }

}
