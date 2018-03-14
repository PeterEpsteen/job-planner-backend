package api.repository;

import api.models.Job;
import api.models.JobContact;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
@Repository
public interface ContactRepository extends CrudRepository<JobContact, Long>{
    Set<JobContact> findByJob(Job job);

    JobContact findById(Long id);
}
