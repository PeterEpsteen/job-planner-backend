package api.repository;

import api.models.Job;
import api.models.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface TodoRepository extends CrudRepository<Todo, Long> {
        Set<Todo> findByJob(Job job);
        Todo findById(Long id);
}