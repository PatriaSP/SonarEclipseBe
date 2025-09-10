package com.patria.apps.transaction.service;

import com.patria.apps.response.ReadResponse;
import com.patria.apps.transaction.request.TransactionsCreateRequest;
import com.patria.apps.transaction.request.TransactionsDestroyRequest;
import com.patria.apps.transaction.request.TransactionsListRequest;
import com.patria.apps.transaction.request.TransactionsPayRequest;
import com.patria.apps.transaction.request.TransactionsProductReviewRequest;
import com.patria.apps.transaction.request.TransactionsUpdateProductReviewRequest;
import com.patria.apps.transaction.request.TransactionsUpdateRequest;
import com.patria.apps.transaction.request.TransactionsUpdateStatusRequest;
import com.patria.apps.transaction.response.TransactionsListResponse;
import java.util.List;

public interface TransactionsService {

    ReadResponse<List<TransactionsListResponse>> list(TransactionsListRequest request);
    
    ReadResponse createTransactions(TransactionsCreateRequest request);

    ReadResponse destroyTransactions(TransactionsDestroyRequest request);

    ReadResponse updateTransactions(TransactionsUpdateRequest request);
    
    ReadResponse updateStatusTransactions(TransactionsUpdateStatusRequest request);

    ReadResponse payTransactions(TransactionsPayRequest request);

    ReadResponse productReview(TransactionsProductReviewRequest request);

    ReadResponse updateProductReview(TransactionsUpdateProductReviewRequest request);

    ReadResponse<TransactionsListResponse> retrieveSingleData(String id);
}
