package inzh.talend.job.manager.repository;

import inzh.talend.job.manager.JobBuilder;
import inzh.talend.job.manager.Job;
import inzh.talend.job.manager.tool.Env;
import inzh.talend.job.manager.tool.EnvDetector;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.lingala.zip4j.ZipFile;

/**
 * Finder for {@link JobRepository}, use for manage {@link JobRepository}
 * content.
 *
 * @author Jean-Raffi Nazareth
 */
public class JobRepositoryFinder {

    final private JobRepository repository;

    private JobBuilder jobBuilder;
    private List<Job> jobs;

    public JobRepositoryFinder(JobRepository repository) {
        this.repository = repository;
    }

    public JobRepository getJobRepository() {
        return repository;
    }

    public List<Job> getJobs() throws IOException {
        if (jobs == null) {
            jobs = read();
        }
        return jobs;
    }

    public JobBuilder getJobBuilder() {
        if (jobBuilder == null) {
            jobBuilder = new JobBuilder();
        }
        return jobBuilder;
    }

    public int rcompareVersion(Job job1, Job job2) {
        int re = job1.getName().compareTo(job2.getName());
        if (re == 0) {
            Double v1 = job1.getVersion();
            Double v2 = job2.getVersion();
            if (v1.equals(v2)) {
                return 0;
            }
            return (v1 > v2) ? -1 : 1;
        }
        return re;
    }

    private List<Job> read() throws IOException {
        DirectoryStream<Path> ds = Files.newDirectoryStream(getJobRepository().getPath());

        List<Job> jobs = new ArrayList<>();

        for (Path p : ds) {
            if (p.getFileName().toString().equals("_run")) {
                continue;
            }
            List<Job> sJobs = readVersion(p);
            jobs.addAll(sJobs);
        }

        Collections.sort(jobs, (Job o1, Job o2) -> rcompareVersion(o1, o2));
        return jobs;
    }

    private List<Job> readVersion(Path versionPath) throws IOException {
        String name = versionPath.getFileName().toString();
        DirectoryStream<Path> ds = Files.newDirectoryStream(versionPath);

        List<Job> jobs = new ArrayList<>();

        for (Path p : ds) {
            String version = p.getFileName().toString();
            Job j = getJobBuilder().create(name, version);
            jobs.add(j);
        }

        return jobs;
    }

    public Job put(Path path) throws IOException {
        JobBuilder builder = new JobBuilder();
        Job job = builder.create(path.getFileName().toString());

        Path target = repository.resolve(job);
        if (!Files.isDirectory(target)) {
            FileAttribute[] attributes;
            if (EnvDetector.getDetected() == Env.UNIX) {
                attributes = new FileAttribute[1];
                attributes[1] = PosixFilePermissions.asFileAttribute(
                        PosixFilePermissions.fromString("rwxrwxr-x")
                );
            } else {
                attributes = new FileAttribute[0];
            }
            Files.createDirectories(target, attributes);
        }

        // Extract to repository
        new ZipFile(path.toFile())
                .extractAll(target.toString());

        // Purge cache
        jobs = null;
        
        return job;
    }

    protected Boolean isEquals(Job job, String name, Double version) {
        if (job.getName().equals(name)) {
            if (version != null) {
                return job.getVersion().equals(version);
            }
            return true;
        }
        return false;
    }

    public Job get(String name, Double version) throws IOException {
        for (Job job : getJobs()) {
            if (isEquals(job, name, version)) {
                return job;
            }
        }
        return null;
    }
}
