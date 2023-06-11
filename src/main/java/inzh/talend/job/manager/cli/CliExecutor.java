package inzh.talend.job.manager.cli;

import inzh.lang.SimpleProcess;
import inzh.lang.SimpleProcessBuilder;
import inzh.lang.value.system.ErrChangeListener;
import inzh.lang.value.system.OutChangeListener;
import inzh.talend.job.manager.Job;
import inzh.talend.job.manager.parameter.ContextParameters;
import java.io.IOException;
import java.util.List;

/**
 * Cli execution for {@link CliPreparator}.
 *
 * @author Jean-Raffi Nazareth
 */
public class CliExecutor {

    private CliPreparator preparator = null;

    public CliExecutor(CliPreparator preparator) {
        this.preparator = preparator;
    }

    public SimpleProcess execute(Job job, ContextParameters contextParameters, Boolean async, SimpleProcessBuilder builder) throws IOException, Exception {

        List<String> cmd = preparator.createCommand(job, contextParameters);
        System.out.println(cmd);

        ProcessBuilder processBuilder = new ProcessBuilder(cmd);
        if (builder == null) {
            builder = new SimpleProcessBuilder()
                    .outputRowListener(new OutChangeListener<>())
                    .errorRowListener(new ErrChangeListener<>());
        }
        builder.processBuilder(processBuilder);

        SimpleProcess sp = builder.build();

        if (async) {
            sp.startAsync();
        } else {
            sp.start();
            if(sp.process().exitValue() > 0){
                throw new Exception("Failed to execute job with command : '" + cmd + '"');
            }
        }

        return sp;
    }
}
