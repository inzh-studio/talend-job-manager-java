package inzh.talend.job.manager.parameter;

/**
 * Context Parameter for Talend job.
 *
 * @author Jean-Raffi Nazareth
 */
public class ContextParameter {

    private String key;
    private Object value;

    public ContextParameter() {
    }

    public ContextParameter(String key) {
        this.key = key;
    }

    public ContextParameter(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }
}
