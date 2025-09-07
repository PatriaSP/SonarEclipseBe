package com.patria.apps.transaction.service;

import com.patria.apps.response.ReadResponse;
import com.patria.apps.transaction.request.BasketCreateRequest;
import com.patria.apps.transaction.request.BasketUpdateRequest;
import com.patria.apps.transaction.request.BasketListRequest;
import com.patria.apps.transaction.request.BasketDestroyRequest;
import com.patria.apps.transaction.response.BasketListResponse;
import java.util.List;

public interface BasketService {

    ReadResponse<List<BasketListResponse>> list(BasketListRequest request);
            
    ReadResponse createBasket(BasketCreateRequest request);
    
    ReadResponse destroyBasket(BasketDestroyRequest request);
    
    ReadResponse updateBasket(BasketUpdateRequest request);
}
