package com.patria.apps.transaction.serializer;

import com.patria.apps.config.AESHelperService;
import com.patria.apps.entity.ExpeditionHistory;
import com.patria.apps.entity.Transactions;
import com.patria.apps.exception.GeneralException;
import com.patria.apps.expedition.response.ExpeditionHistoryResponse;
import com.patria.apps.product.response.ProductReviewResponse;
import com.patria.apps.response.ReadResponse;
import com.patria.apps.transaction.response.TransactionsListResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionsSerializerService {

    private final AESHelperService aesService;

    public TransactionsListResponse serialize(
            Transactions transactions
    ) {
        try {
            TransactionsListResponse.TransactionsListResponseBuilder builder = TransactionsListResponse.builder()
                    .key(aesService.encrypt(String.valueOf(transactions.getId())))
                    .invoiceNum(transactions.getInvoiceNum())
                    .status(transactions.getStatus())
                    .qty(transactions.getQty())
                    .date(transactions.getCreatedAt())
                    .payment(transactions.getPayment().getMethod())
                    .userName(transactions.getUsers().getFirstName() + " " + transactions.getUsers().getLastName())
                    .address(transactions.getUsersAddress().getAddress())
                    .expedition(transactions.getExpedition().getExpeditionName());

            List<ExpeditionHistoryResponse> expeditionHitoryList = new ArrayList<>();
            for (ExpeditionHistory expedition : transactions.getExpeditionHistory()) {
                expeditionHitoryList.add(ExpeditionHistoryResponse.builder()
                        .detail(expedition.getDetail())
                        .date(expedition.getCreatedAt())
                        .build());
            }
            builder.expeditionHistory(expeditionHitoryList);

            if (transactions.getProductReview() != null) {
                builder.productReviewResponse(ProductReviewResponse.builder()
                        .key(aesService.encrypt(String.valueOf(transactions.getProductReview().getId())))
                        .review(transactions.getProductReview().getReview())
                        .score(transactions.getProductReview().getScore())
                        .build());
            }

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
