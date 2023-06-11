package inzh.talend.job.manager.executor;

import inzh.lang.SimpleProcess;
import inzh.lang.SimpleProcessBuilder;
import inzh.talend.job.manager.Job;
import inzh.talend.job.manager.cli.CliExecutor;
import inzh.talend.job.manager.cli.CliPreparator;
import inzh.talend.job.manager.parameter.ContextParameters;
import inzh.talend.job.manager.repository.JobRepository;
import inzh.talend.job.manager.repository.JobRepositoryFinder;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

/**
 * Simple executor for Talend job in repository
 *
 * @author Jean-Raffi Nazareth
 */
public class SimpleJobExecutor {
    
    /**
     * Put job in repository from zip file
     *
     * @param path path Zip file path
     * @param repositoryPath $repositoryPath Repository path
     * @return Job Job updated
     * 
     * @throws java.io.IOException
     */
    public static Job put(Path path, Path repositoryPath) throws IOException {
        if (repositoryPath == null) {
            throw new IOException("Repository path missing");
        }

        JobRepository repository = new JobRepository(repositoryPath);

        JobRepositoryFinder jf = new JobRepositoryFinder(repository);
        return jf.put(path);
    }

    /**
     * Execute specific job with parameters
     *
     * @param jobName Job name
     * @param parameters Map of contexts parameters passed on job, null if no parameters
     * @param repositoryPath Repository path
     * @return SimpleProcess Current process
     * 
     * @throws java.lang.Exception
     */
    public static SimpleProcess execute(String jobName, Map parameters, Path repositoryPath) throws Exception {
        return execute(jobName, parameters, repositoryPath, null, null);
    }

    /**
     * Execute specific job with parameters
     *
     * @param jobName Job name
     * @param parameters Map of contexts parameters passed on job, null if no parameters
     * @param repositoryPath Repository path
     * @param pBuilder SimpleProcessBuilder instance for custom process preparation
     * @return SimpleProcess Current process
     * 
     * @throws java.lang.Exception
     */
    public static SimpleProcess execute(String jobName, Map parameters, Path repositoryPath, SimpleProcessBuilder pBuilder) throws Exception {
        return execute(jobName, parameters, repositoryPath, null, pBuilder);
    }

    /**
     * Execute specific job with parameters
     *
     * @param jobName Job name
     * @param parameters Map of contexts parameters passed on job, null if no parameters
     * @param repositoryPath Repository path
     * @param version Version of job, if null last version selected
     * @return SimpleProcess Current process
     * 
     * @throws java.lang.Exception
     */
    public static SimpleProcess execute(String jobName, Map parameters, Path repositoryPath, Double version) throws Exception {
        return execute(jobName, parameters, repositoryPath, version, null);
    }

    /**
     * Execute specific job with parameters
     *
     * @param jobName Job name
     * @param parameters Map of contexts parameters passed on job, null if no parameters
     * @param repositoryPath Repository path
     * @param version Version of job, if null last version selected
     * @param pBuilder SimpleProcessBuilder instance for custom process preparation
     * @return SimpleProcess Current process
     * 
     * @throws java.lang.Exception
     */
    public static SimpleProcess execute(String jobName, Map parameters, Path repositoryPath, Double version, SimpleProcessBuilder pBuilder) throws Exception {
        if (repositoryPath == null) {
            throw new IOException("Repository path missing");
        }

        JobRepository repository = new JobRepository(repositoryPath);

        JobRepositoryFinder jf = new JobRepositoryFinder(repository);
        Job job = jf.get(jobName, version);

        if (job == null) {
            StringBuilder builder = new StringBuilder("The job '")
                    .append(jobName)
                    .append("'");
            if (version != null) {
                builder.append(" on version ")
                        .append(version);
            }
            builder.append(" not exist in '")
                    .append(repositoryPath)
                    .append("'");

            throw new IOException(builder.toString());
        }

        ContextParameters cps = new ContextParameters();
        if (parameters != null) {
            cps.fromMap(parameters);
        }

        CliPreparator preparator = new CliPreparator(repository);
        CliExecutor executor = new CliExecutor(preparator);

        return executor.execute(job, cps, Boolean.FALSE, pBuilder);
    }
}
