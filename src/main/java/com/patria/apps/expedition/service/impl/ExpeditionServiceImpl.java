package com.patria.apps.expedition.service.impl;

import com.patria.apps.config.AESHelperService;
import com.patria.apps.entity.Expedition;
import com.patria.apps.exception.GeneralException;
import com.patria.apps.helper.SecurityHelperService;
import com.patria.apps.helper.SortHelperService;
import com.patria.apps.helper.ValidationHelperService;
import com.patria.apps.expedition.filter.ExpeditionFilterService;
import com.patria.apps.expedition.request.ExpeditionCreateRequest;
import com.patria.apps.expedition.request.ExpeditionDeleteRequest;
import com.patria.apps.expedition.request.ExpeditionDestroyRequest;
import com.patria.apps.expedition.request.ExpeditionListRequest;
import com.patria.apps.expedition.request.ExpeditionRestoreRequest;
import com.patria.apps.expedition.request.ExpeditionUpdateRequest;
import com.patria.apps.expedition.response.ExpeditionListResponse;
import com.patria.apps.expedition.serializer.ExpeditionSerializerService;
import com.patria.apps.expedition.service.ExpeditionService;
import com.patria.apps.repository.CategoryRepository;
import com.patria.apps.repository.ExpeditionRepository;
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

@Service
@RequiredArgsConstructor
public class ExpeditionServiceImpl implements ExpeditionService {

    private final ExpeditionRepository expeditionRepository;
    private final CategoryRepository categoryRepository;
    private final SortHelperService sortHelperService;
    private final ValidationHelperService validationHelperService;
    private final AESHelperService aesService;
    private final ExpeditionSerializerService expeditionSerializerService;
    private final ExpeditionFilterService expeditionFilterService;

    private final Map<String, String> sortMap = new HashMap<String, String>() {
        {
            //parameter and column name
            put("expeditionName", "expeditionName");
            put("-expeditionName", "expeditionName");
            put("price", "price");
            put("-price", "price");
        }
    };

    @Override
    public ReadResponse<List<ExpeditionListResponse>> list(ExpeditionListRequest request) {
        Sort sortBy = sortHelperService.setSort(sortMap, request.getSort(), "expeditionName");
        Pageable pageable = PageRequest.of((request.getPage() - 1), request.getPerPage(), sortBy);
        Page<Expedition> expedition = expeditionRepository.findAll(expeditionFilterService.specification(request), pageable);
        List<ExpeditionListResponse> usersResponse = expedition
                .getContent()
                .stream()
                .map(datas -> expeditionSerializerService.serialize(
                datas
        ))
                .toList();

        Page<ExpeditionListResponse> responsePage = new PageImpl<>(usersResponse, pageable,
                expedition.getTotalElements());

        return ReadResponse.<List<ExpeditionListResponse>>builder()
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
    public ReadResponse createExpedition(ExpeditionCreateRequest request) {
        validationHelperService.validate(request);

        Expedition expedition = new Expedition();
        expedition.setExpeditionName(request.getExpeditionName());
        expedition.setPrice(request.getPrice());
        expedition.setCreatedAt(LocalDateTime.now());
        expedition.setCreatedBy(1L);

        expeditionRepository.save(expedition);
        expeditionRepository.flush();

        return expeditionSerializerService.serializeTransaction(
                "Success create expedition!"
        );
    }

    @Override
    public ReadResponse deleteExpedition(ExpeditionDeleteRequest request) {
        validationHelperService.validate(request);
        
        Long id = aesService.getDecryptedString(request.getKey());
        Expedition expedition = expeditionRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(
                () -> new GeneralException(HttpStatus.NOT_FOUND, "Data Not Found")
        );
        
        expedition.setDeletedBy(SecurityHelperService.getPrincipal().getId());
        expedition.setDeletedAt(LocalDateTime.now());
        
        expeditionRepository.save(expedition);
        expeditionRepository.flush();
        
        return expeditionSerializerService.serializeTransaction(
                "Success delete expedition!"
        );
    }

    @Override
    public ReadResponse restoreExpedition(ExpeditionRestoreRequest request) {
        validationHelperService.validate(request);
        
        Long id = aesService.getDecryptedString(request.getKey());
        Expedition expedition = expeditionRepository.findByIdAndDeletedAtIsNotNull(id).orElseThrow(
                () -> new GeneralException(HttpStatus.NOT_FOUND, "Data Not Found")
        );
        
        expedition.setDeletedBy(null);
        expedition.setDeletedAt(null);
        expedition.setUpdatedBy(SecurityHelperService.getPrincipal().getId());
        expedition.setUpdatedAt(LocalDateTime.now());
        
        expeditionRepository.save(expedition);
        expeditionRepository.flush();
        
        return expeditionSerializerService.serializeTransaction(
                "Success restore expedition!"
        );
    }

    @Override
    public ReadResponse destroyExpedition(ExpeditionDestroyRequest request) {
        validationHelperService.validate(request);
        
        Long id = aesService.getDecryptedString(request.getKey());
        Expedition expedition = expeditionRepository.findByIdAndDeletedAtIsNotNull(id).orElseThrow(
                () -> new GeneralException(HttpStatus.NOT_FOUND, "Data Not Found")
        );
        
        expeditionRepository.delete(expedition);
        
        return expeditionSerializerService.serializeTransaction(
                "Success destroy expedition!"
        );
    }

    @Override
    public ReadResponse updateExpedition(ExpeditionUpdateRequest request) {
        validationHelperService.validate(request);

        Long id = aesService.getDecryptedString(request.getKey());
        Expedition expedition = expeditionRepository.findById(id).orElseThrow(
                () -> new GeneralException(HttpStatus.NOT_FOUND, "Data Not Found")
        );
        expedition.setExpeditionName(request.getExpeditionName());      
        expedition.setPrice(request.getPrice());      
        expedition.setUpdatedAt(LocalDateTime.now());
        expedition.setUpdatedBy(SecurityHelperService.getPrincipal().getId());
        
        expeditionRepository.save(expedition);
        expeditionRepository.flush();
        

        return expeditionSerializerService.serializeTransaction(
                "Success update expedition!"
        );
    }

}
