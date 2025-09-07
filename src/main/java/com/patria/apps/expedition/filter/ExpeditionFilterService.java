package com.patria.apps.expedition.filter;

import com.patria.apps.entity.Expedition;
import com.patria.apps.expedition.request.ExpeditionListRequest;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class ExpeditionFilterService {

    public Specification<Expedition> specification(ExpeditionListRequest request) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (Objects.nonNull(request.getExpeditionName())) {
                if (!request.getExpeditionName().isBlank()) {
                    predicates.add(builder.like(builder.lower(root.get("expeditionName")), "%" + request.getExpeditionName().toLowerCase() + "%"));
                }
            }

            if (Objects.nonNull(request.getPrice())) {
                if (request.getPrice() != 0) {
                    predicates.add(builder.equal(root.get("price"), request.getPrice()));
                }
            }

            // Check soft delete
            if (Objects.isNull(request.getIsTrash())) {
                predicates.add(
                        builder.isNull(root.get("deletedAt"))
                );
            } else if (request.getIsTrash()) {
                predicates.add(
                        builder.isNotNull(root.get("deletedAt"))
                );
            } else {
                predicates.add(
                        builder.isNull(root.get("deletedAt"))
                );
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };
    }

}
