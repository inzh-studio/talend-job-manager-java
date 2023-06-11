package inzh.talend.job.manager.parameter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Container for all {@link ContextParameter}.
 *
 * @author Jean-Raffi Nazareth
 */
public class ContextParameters {

    private final List<ContextParameter> contextParameters = new ArrayList<>();

    public ContextParameter add(String key, Object value) {
        ContextParameter p = new ContextParameter(key, value);
        ContextParameter cp = get(key);
        if (cp != null) {
            getAll().remove(cp);
        }
        getAll().add(p);
        return p;
    }

    public ContextParameters addAll(List<ContextParameter> contextParameters) {

        for (ContextParameter cp : contextParameters) {
            add(cp.getKey(), cp.getValue());
        }
        return this;
    }

    public List<ContextParameter> getAll() {
        return contextParameters;
    }

    public ContextParameter get(String key) {
        for (ContextParameter cp : getAll()) {
            if (cp.getKey().equals(key)) {
                return cp;
            }
        }
        return null;
    }

    public ContextParameters fromMap(Map parameters) {
        Set<Map.Entry> es = parameters.entrySet();
        for(Map.Entry e : es){
            add(e.getKey().toString(), e.getValue());
        }
        return this;
    }
}