package mbarix4j.util.prefs;

import java.util.prefs.AbstractPreferences;
import java.util.prefs.BackingStoreException;

/**
 * Disable to preferences when they cause problems. From
 * <a href="http://www.allaboutbalance.com/articles/disableprefs/">http://www.allaboutbalance.com/articles/disableprefs/</a>
 *
 */
public class DisabledPreferences extends AbstractPreferences {

    public DisabledPreferences() {
        super(null, "");
    }

    protected void putSpi(String key, String value) {

    }

    protected String getSpi(String key) {
        return null;
    }

    protected void removeSpi(String key) {

    }

    protected void removeNodeSpi() throws BackingStoreException {

    }

    protected String[] keysSpi() throws BackingStoreException {
        return new String[0];
    }

    protected String[] childrenNamesSpi()
            throws BackingStoreException {
        return new String[0];
    }

    protected AbstractPreferences childSpi(String name) {
        return null;
    }

    protected void syncSpi() throws BackingStoreException {

    }

    protected void flushSpi() throws BackingStoreException {

    }
}
