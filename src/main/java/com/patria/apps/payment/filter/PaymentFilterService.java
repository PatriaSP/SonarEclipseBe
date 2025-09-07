package com.patria.apps.payment.filter;

import com.patria.apps.entity.Payment;
import com.patria.apps.payment.request.PaymentListRequest;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class PaymentFilterService {

    public Specification<Payment> specification(PaymentListRequest request) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (Objects.nonNull(request.getMethod())) {
                if (!request.getMethod().isBlank()) {
                    predicates.add(builder.like(builder.lower(root.get("method")), "%" + request.getMethod().toLowerCase() + "%"));
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
