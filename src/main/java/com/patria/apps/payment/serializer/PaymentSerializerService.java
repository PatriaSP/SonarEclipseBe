package com.patria.apps.payment.serializer;

import com.patria.apps.config.AESHelperService;
import com.patria.apps.entity.Payment;
import com.patria.apps.exception.GeneralException;
import com.patria.apps.payment.response.PaymentListResponse;
import com.patria.apps.response.ReadResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentSerializerService {

    private final AESHelperService aesService;
    
    public PaymentListResponse serialize(
            Payment payment
    ) {
        try {
            PaymentListResponse.PaymentListResponseBuilder builder = PaymentListResponse.builder()
                    .key(aesService.encrypt(String.valueOf(payment.getId())))
                    .method(payment.getMethod())
                    ;

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
    
}
