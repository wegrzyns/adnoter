package cea.Util;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

public class ConfigurationUtil {
    private static final Configurations configurations = new Configurations();
    private static final String configurationPath = "config.properties";

    public static Configuration configuration() {
        try {
            return configurations.properties(configurationPath);
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }

}
