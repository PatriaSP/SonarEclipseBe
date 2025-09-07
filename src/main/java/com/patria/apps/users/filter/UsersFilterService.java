package com.patria.apps.users.filter;

import com.patria.apps.entity.Users;
import com.patria.apps.users.request.UserListRequest;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class UsersFilterService {

    public Specification<Users> specification(UserListRequest request) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (Objects.nonNull(request.getName())) {
                if (!request.getName().isBlank()) {
                    Expression<String> fullName = builder.concat(
                            builder.concat(root.get("firstName"), " "),
                            root.get("lastName")
                    );
                    predicates.add(builder.like(builder.lower(fullName), "%" + request.getName().toLowerCase() + "%"));
                }
            }

            if (Objects.nonNull(request.getRole())) {
                if (!request.getRole().isBlank()) {
                    predicates.add(builder.like(builder.lower(root.get("role").get("roleName")), "%" + request.getRole().toLowerCase() + "%"));
                }
            }
            
            if (Objects.nonNull(request.getEmail())) {
                if (!request.getEmail().isBlank()) {
                    predicates.add(builder.like(builder.lower(root.get("email")), "%" + request.getEmail() + "%"));
                }
            }

            if (Objects.nonNull(request.getCountry())) {
                if (!request.getCountry().isBlank()) {
                    predicates.add(builder.equal(root.get("country"), request.getCountry()));
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
