package api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

@Entity
public class Job implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "job_id")
    private Long id;
    @NotNull
    private String title;
    @NotNull
    private String company;

    private String description;

    private String companyDomain;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL)
    private Set<JobContact> contacts;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL)
    private Set<Event> events;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL)
    private Set<Todo> todos;

    private boolean applied;

    private boolean interviewed;

    private boolean jobOffer;

    public Job() {}

    public String getCompanyDomain() {
        return companyDomain;
    }

    public void setCompanyDomain(String companyDomain) {
        this.companyDomain = companyDomain;
    }

    public boolean isApplied() {
        return applied;
    }

    public void setApplied(boolean applied) {
        this.applied = applied;
    }

    public boolean isInterviewed() {
        return interviewed;
    }

    public void setInterviewed(boolean interviewed) {
        this.interviewed = interviewed;
    }

    public boolean isJobOffer() {
        return jobOffer;
    }

    public void setJobOffer(boolean jobOffer) {
        this.jobOffer = jobOffer;
    }

    public Long getId() {
        return id;
    }

    public Set<Todo> getTodos() {
        return todos;
    }

    public void setTodos(Set<Todo> todos) {
        this.todos = todos;
    }

    public Set<JobContact> getContacts() {
        return contacts;
    }

    public void setContacts(Set<JobContact> contacts) {
        this.contacts = contacts;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCompany() {
        return company;
    }

    public String getTitle() {
        return title;
    }
    @JsonIgnore
    public User getUser() {
        return user;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    @JsonProperty
    public void setUser(User user) {
        this.user = user;
    }
}
