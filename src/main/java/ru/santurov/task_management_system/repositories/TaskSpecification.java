package ru.santurov.task_management_system.repositories;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import ru.santurov.task_management_system.models.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskSpecification {

    public static Specification<Task> withFilters(String status, String priority) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            if (priority != null) {
                predicates.add(criteriaBuilder.equal(root.get("priority"), priority));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
