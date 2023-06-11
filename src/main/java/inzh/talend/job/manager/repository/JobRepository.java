package inzh.talend.job.manager.repository;

import inzh.talend.job.manager.Job;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Storage for {@link Job}, contains all jobs by version.
 *
 * @author Jean-Raffi Nazareth
 */
public class JobRepository {

    private final Path path;

    public JobRepository(String path) {
        this.path = Paths.get(path);
    }

    public JobRepository(Path path) {
        this.path = path;
    }

    public Path getPath() {
        return path;
    }

    public Path resolve(Job job) {
        Path r = getPath().resolve(job.getVirtualPath());
        return r;
    }
}
