package com.patria.apps.transaction.serializer;

import com.patria.apps.config.AESHelperService;
import com.patria.apps.entity.Basket;
import com.patria.apps.exception.GeneralException;
import com.patria.apps.response.ReadResponse;
import com.patria.apps.transaction.response.BasketListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class BasketSerializerService {

    private final AESHelperService aesService;
    
    public BasketListResponse serialize(
            Basket basket
    ) {
        try {
            BasketListResponse.BasketListResponseBuilder builder = BasketListResponse.builder()
                    .key(aesService.encrypt(String.valueOf(basket.getId())))
                    .productKey(aesService.encrypt(String.valueOf(basket.getProduct().getId())))
                    .productName(basket.getProduct().getName())
                    .qty(basket.getQty())
                    .date(basket.getDate());
      
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

    public ReadResponse<BasketListResponse> serializeRetrieve(Basket basket) {
        BasketListResponse basketResponse = this.serialize(
                basket
        );
        return ReadResponse.<BasketListResponse>builder()
                .status(HttpStatus.OK.value())
                .data(basketResponse)
                .build();
    }
    
}
