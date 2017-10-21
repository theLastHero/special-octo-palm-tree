package PlayerWarpGUI;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Remember javadocs  >:o
 */
public class LinkedProperties extends Properties {

    protected LinkedProperties linkedDefaults;
    protected Set<Object> linkedKeys = new LinkedHashSet<>();

    public LinkedProperties() { super(); }

    public LinkedProperties(LinkedProperties defaultProps) {
        super(defaultProps); // super.defaults = defaultProps;
        this.linkedDefaults = defaultProps;
    }

    @Override
    public synchronized Enumeration<?> propertyNames() {
        return keys();
    }

    @Override
    public Enumeration<Object> keys() {
        Set<Object> allKeys = new LinkedHashSet<>();
        if (null != defaults) {
            allKeys.addAll(linkedDefaults.linkedKeys);
        }
        allKeys.addAll(this.linkedKeys);
        return Collections.enumeration(allKeys);
    }

    @Override
    public synchronized Object put(Object key, Object value) {
        linkedKeys.add(key);
        return super.put(key, value);
    }

    @Override
    public synchronized Object remove(Object key) {
        linkedKeys.remove(key);
        return super.remove(key);
    }

    @Override
    public synchronized void putAll(Map<?, ?> values) {
        for (Object key : values.keySet()) {
            linkedKeys.add(key);
        }
        super.putAll(values);
    }

    @Override
    public synchronized void clear() {
        super.clear();
        linkedKeys.clear();
    }

    private static final long serialVersionUID = 0xC00L;
}
