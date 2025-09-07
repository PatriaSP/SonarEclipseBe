package com.patria.apps.product.filter;

import com.patria.apps.entity.Product;
import com.patria.apps.product.request.ProductListRequest;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class ProductFilterService {

    public Specification<Product> specification(ProductListRequest request) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (Objects.nonNull(request.getName())) {
                if (!request.getName().isBlank()) {
                    predicates.add(builder.like(builder.lower(root.get("name")), "%" + request.getName().toLowerCase() + "%"));
                }
            }

            if (Objects.nonNull(request.getPrice())) {
                predicates.add(builder.equal(root.get("price"), request.getPrice()));
            }
            
            if (Objects.nonNull(request.getIsAvailable())) {
                predicates.add(builder.notEqual(root.get("stock"), 0));
            }
            
            if (Objects.nonNull(request.getIsActive())) {
                predicates.add(
                        builder.equal(root.get("isActive"), request.getIsActive())
                );
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
