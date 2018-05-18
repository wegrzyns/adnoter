package cea.Util;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

public class ConfigurationUtil {
    private static final Configurations configurations = new Configurations();
    private static final String configurationPath = "config.properties";
    private static Configuration conf;

    static {
        try {
            conf = configurations.properties(configurationPath);
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }

    }

    public static Configuration configuration() {
        return conf;
    }

}
