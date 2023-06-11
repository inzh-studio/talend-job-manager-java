package inzh.talend.job.manager;

import java.nio.file.Path;

/**
 * Builder of {@link Job}
 *
 * @author Jean-Raffi Nazareth
 */
public class JobBuilder {

    public Job create(Path path) {
        String fileName = path.getFileName().toString();
        return create(fileName);
    }

    public Job create(String name, String sversion) {

        Double version = Double.parseDouble(sversion);
        return create(name, version);
    }

    public Job create(String name, Double version) {
        Job job = new Job(name, null, version);
        return job;
    }

    public Job create(String fileName) {
        String name;
        Double version = null;

        fileName = fileName.endsWith(".zip") ? fileName.replace(".zip", "") : fileName;
        String[] sp = fileName.split("_");
        name = sp[0];

        if (sp.length > 1) {
            version = Double.parseDouble(sp[1]);
        }

        Job job = new Job(name, null, version);
        return job;
    }
}
