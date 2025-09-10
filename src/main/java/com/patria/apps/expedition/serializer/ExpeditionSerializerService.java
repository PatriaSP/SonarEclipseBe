package com.patria.apps.expedition.serializer;

import com.patria.apps.config.AESHelperService;
import com.patria.apps.entity.Expedition;
import com.patria.apps.exception.GeneralException;
import com.patria.apps.expedition.response.ExpeditionListResponse;
import com.patria.apps.response.ReadResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExpeditionSerializerService {

    private final AESHelperService aesService;
    
    public ExpeditionListResponse serialize(
            Expedition expedition
    ) {
        try {
            ExpeditionListResponse.ExpeditionListResponseBuilder builder = ExpeditionListResponse.builder()
                    .key(aesService.encrypt(String.valueOf(expedition.getId())))
                    .expeditionName(expedition.getExpeditionName())
                    .price(expedition.getPrice())
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

    public ReadResponse<ExpeditionListResponse> serializeRetrieve(Expedition expedition) {
        ExpeditionListResponse expeditionResponse = this.serialize(
                expedition
        );
        return ReadResponse.<ExpeditionListResponse>builder()
                .status(HttpStatus.OK.value())
                .data(expeditionResponse)
                .build();
    }
    
}
