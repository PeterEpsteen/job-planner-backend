package api.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Entity
public class Event implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String title;
    private String description;
    @NotNull
    private String date;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    public Event() {}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
