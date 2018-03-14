package api.repository;

import api.models.Event;
import api.models.Job;
import api.models.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
@Repository
public interface EventRepository extends CrudRepository<Event, Long> {
    Set<Event> findByJob(Job job);
    Event findById(Long id);

}