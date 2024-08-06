package ru.santurov.task_management_system.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.santurov.task_management_system.models.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
    Page<Task> findTasksByAuthor_Id(Specification<Task> spec, Long authorId, Pageable pageable);
    Page<Task> findTasksByPerformers_Id(Specification<Task> spec, Long performerId, Pageable pageable);
}