package com.patria.apps.transaction.filter;

import com.patria.apps.entity.Transactions;
import com.patria.apps.helper.SecurityHelperService;
import com.patria.apps.transaction.request.TransactionsListRequest;
import jakarta.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class TransactionsFilterService {

    public Specification<Transactions> specification(TransactionsListRequest request) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (Objects.nonNull(request.getInvoiceNum())) {
                if (!request.getInvoiceNum().isBlank()) {
                    predicates.add(builder.like(builder.lower(root.get("invoiceNum")), "%" + request.getInvoiceNum().toLowerCase() + "%"));
                }
            }

            if (Objects.nonNull(request.getStatus())) {
                predicates.add(builder.like(builder.lower(root.get("status")), "%" + request.getStatus() + "%"));
            }

            if (Objects.nonNull(request.getDate())) {
                LocalDate localDate = request.getDate();
                LocalDateTime startOfDay = localDate.atStartOfDay();
                LocalDateTime endOfDay = localDate.plusDays(1).atStartOfDay();

                predicates.add(builder.between(root.get("createdAt"), startOfDay, endOfDay));
            }

            if(!SecurityHelperService.getPrincipal().getRole().getRoleName().equalsIgnoreCase("admin")){
                predicates.add(builder.equal(root.get("users").get("id"), SecurityHelperService.getPrincipal().getId()));
            }
            
            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };
    }
}
