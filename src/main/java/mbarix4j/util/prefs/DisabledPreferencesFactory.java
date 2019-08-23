package mbarix4j.util.prefs;

import java.util.prefs.Preferences;
import java.util.prefs.PreferencesFactory;

/**
 * Disable to preferences when they cause problems. From
 * <a href="http://www.allaboutbalance.com/articles/disableprefs/">http://www.allaboutbalance.com/articles/disableprefs/</a>
 *
 * Use as:
 *
 * java -Djava.util.prefs.PreferencesFactory=mbarix4j.util.prefs.DisabledPreferencesFactory
 *
 * or add the following near the start of you application:
 * <pre>System.setProperty("java.util.prefs.PreferencesFactory", "DisabledPreferencesFactory");</pre>
 *
 */
public class DisabledPreferencesFactory implements PreferencesFactory {

    public Preferences systemRoot() {
        return new DisabledPreferences();
    }

    public Preferences userRoot() {
        return new DisabledPreferences();
    }
}
