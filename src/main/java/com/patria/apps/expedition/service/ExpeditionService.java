package com.patria.apps.expedition.service;

import com.patria.apps.expedition.request.ExpeditionCreateRequest;
import com.patria.apps.expedition.request.ExpeditionDeleteRequest;
import com.patria.apps.expedition.request.ExpeditionDestroyRequest;
import com.patria.apps.expedition.request.ExpeditionListRequest;
import com.patria.apps.expedition.request.ExpeditionRestoreRequest;
import com.patria.apps.expedition.request.ExpeditionUpdateRequest;
import com.patria.apps.expedition.response.ExpeditionListResponse;
import com.patria.apps.response.ReadResponse;
import java.util.List;

public interface ExpeditionService {

    ReadResponse<List<ExpeditionListResponse>> list(ExpeditionListRequest request);

    ReadResponse createExpedition(ExpeditionCreateRequest request);

    ReadResponse deleteExpedition(ExpeditionDeleteRequest request);

    ReadResponse restoreExpedition(ExpeditionRestoreRequest request);

    ReadResponse destroyExpedition(ExpeditionDestroyRequest request);

    ReadResponse updateExpedition(ExpeditionUpdateRequest request);

}
