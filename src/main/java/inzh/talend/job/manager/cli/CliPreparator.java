package inzh.talend.job.manager.cli;

import inzh.talend.job.manager.Job;
import inzh.talend.job.manager.parameter.ContextParameter;
import inzh.talend.job.manager.parameter.ContextParameters;
import inzh.talend.job.manager.repository.JobRepository;
import inzh.talend.job.manager.tool.Env;
import inzh.talend.job.manager.tool.EnvDetector;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Cli preparator for {@link Job} execution.
 *
 * @author Jean-Raffi Nazareth
 */
public class CliPreparator {

    final private JobRepository repository;

    protected Env env;

    public CliPreparator(JobRepository repository) {
        this(repository, null);
        this.env = EnvDetector.getDetected();
    }

    public CliPreparator(JobRepository repository, Env env) {
        this.repository = repository;
        this.env = env;
    }

    public Env getEnv() {
        return env;
    }

    public JobRepository getJobRepository() {
        return repository;
    }

    protected Path getJobLauncherDirectoryPath(Job job) throws IOException {
        Path script = getLauncherScript(job);
        return script.getParent();
    }

    public List<String> createCommand(Job job, ContextParameters contextParameters) throws IOException {
        Path script = getLauncherScript(job);

        List<String> cmd = new ArrayList<>();
        if(env == Env.DOS){
            cmd.add("cmd");
            cmd.add("/c");
        }
        if(env == Env.UNIX){
            cmd.add("bash");
        }
        cmd.add(script.toString());
        cmd.addAll(createParameterArgument(contextParameters));
        return cmd;
    }

    protected String getLauncherExt() {
        switch (getEnv()) {
            case DOS:
                return "bat";

            case UNIX:
                return "sh";
        }
        return null;
    }

    protected Path getLauncherScript(Job job) throws IOException {
        String lext = getLauncherExt();
        Path nwk = getJobRepository().resolve(job).resolve(job.getName());
        DirectoryStream<Path> ds = Files.newDirectoryStream(nwk);
        for (Path p : ds) {
            if (p.getFileName().toString().endsWith(lext)) {
                return p;
            }
        }
        return null;
    }

    protected List<String> createParameterArgument(ContextParameters contextParameters) {

        List<String> ls = new ArrayList<>();

        if (contextParameters == null) {
            return ls;
        }

        String model = "{key}={value}";

        for (ContextParameter p : contextParameters.getAll()) {
            ls.add("--context_param");
            String argument = model
                    .replace("{key}", p.getKey())
                    .replace("{value}", formatValue(p.getValue()));
            ls.add(argument);
        }

        return ls;
    }

    protected String formatValue(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Number) {
            return "" + value;
        }

        String str = value.toString();
        str = str.replaceAll("\"", "\\\\\"");
        return str;
    }
}
