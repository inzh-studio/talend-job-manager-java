package inzh.talend.job.manager;

import java.util.Objects;

/**
 * One version of Talend job.
 *
 * @author Jean-Raffi Nazareth
 */
public class Job {

    private String name;
    private String description;
    private Double version;

    public Job() {
    }

    public Job(String name, String description, Double version) {
        this.name = name;
        this.description = description;
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getVersion() {
        return version;
    }

    public void setVersion(Double version) {
        this.version = version;
    }

    public String getVirtualPath() {
        return this.name + "/" + this.version;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Job other = (Job) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.version, other.version)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Job{" + "name=" + name + ", description=" + description + ", version=" + version + "}";
    }
}
