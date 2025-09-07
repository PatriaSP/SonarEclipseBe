package com.patria.apps.transaction.filter;

import com.patria.apps.entity.Basket;
import com.patria.apps.helper.SecurityHelperService;
import com.patria.apps.transaction.request.BasketListRequest;
import jakarta.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class BasketFilterService {

    public Specification<Basket> specification(BasketListRequest request) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (Objects.nonNull(request.getProductName())) {
                if (!request.getProductName().isBlank()) {
                    predicates.add(builder.like(builder.lower(root.get("product").get("name")), "%" + request.getProductName().toLowerCase() + "%"));
                }
            }

            if (Objects.nonNull(request.getQty())) {
                predicates.add(builder.equal(root.get("qty"), request.getQty()));
            }

            if (Objects.nonNull(request.getDate())) {
                LocalDate localDate = request.getDate();
                LocalDateTime startOfDay = localDate.atStartOfDay();
                LocalDateTime endOfDay = localDate.plusDays(1).atStartOfDay();

                predicates.add(builder.between(root.get("date"), startOfDay, endOfDay));
            }

            predicates.add(builder.equal(root.get("users").get("id"), SecurityHelperService.getPrincipal().getId()));
            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };
    }
}
