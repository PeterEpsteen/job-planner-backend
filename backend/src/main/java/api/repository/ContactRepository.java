package api.repository;

import api.models.Job;
import api.models.JobContact;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface ContactRepository extends CrudRepository<JobContact, Long>{
    Set<JobContact> findByJob(Job job);

    JobContact findById(Long id);
}
