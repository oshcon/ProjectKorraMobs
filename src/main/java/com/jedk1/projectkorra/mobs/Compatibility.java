package com.jedk1.projectkorra.mobs;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;

public class Compatibility {

    public static HashMap<String, Plugin> hooked;

    /**
     * Returns a Plugin object for usage of its methods.
     *
     * @param   name    the name of the requested plugin
     * @return  Plugin  the plugin with the specified name
     */
    public static Plugin getPlugin(String name) {
        return hooked.get(name);
    }

    /**
     * Checks compatibility with other loaded plugins using
     * their versions then stores them for future manipulation in a HashMap
     */
    public static void checkHooks() {
        hooked = new HashMap<>();
        hookPlugin("LibsDisguises", "9.2.4", "9.2.4");
    }

    /**
     * Attempts to hook the specified plugin with kore by comparing the plugin's versions
     * with ones known to be compatible, then hooks the plugin if the version falls
     * on or between the minimum and maximum.
     * <p>
     * If the plugin's version falls outside of the compatible range, PKM will attempt to
     * hook the specified plugin anyways, with no guarantees.
     *
     * @param   name      the name of the plugin to hook with kore
     * @param   min       the minimum known compatible version with kore
     * @param   max       the maximum known compatible version with kore
     * @return  boolean   whether or not the plugin successfully hooked with kore
     */
    public static boolean hookPlugin(String name, String min, String max) {
        Plugin hook = Bukkit.getPluginManager().getPlugin(name);

        if (hook != null) {
            String rawVersion = hook.getDescription().getVersion();
            String[] versionPart = rawVersion.split("\\-");
            String version = versionPart[0];

            if (isSupported(version, min, max)) {
                if (!hooked.containsKey(name)) {
                    hooked.put(name, hook);
                    ProjectKorraMobs.log("Hooked into " + name + " v" + hook.getDescription().getVersion() + ".");
                    return true;
                }

                return true;
            } else {
                try {
                    if (!hooked.containsKey(name)) {
                        hooked.put(name, hook);
                        ProjectKorraMobs.log("Hooked into " + name + " v" + hook.getDescription().getVersion() + ". However, this version is not officially supported by PKM.");
                        return true;
                    }

                    return true;
                } catch (Exception ex) {
                    ProjectKorraMobs.printError("Failed to hook into " + name + " v" + hook.getDescription().getVersion() + " due to an unknown error!", ex);
                    return false;
                }
            }
        }

        ProjectKorraMobs.log(name + " was not detected.");
        return false;
    }

    /**
     * Compares the specified version string with a known compatible range of versions.
     *
     * @param   version     the plugin's current version
     * @param   min         the minimum known compatible version with kore
     * @param   max         the maximum known compatible version with kore
     * @return  boolean     whether or not the plugin's version falls inside the compatible range
     */
    public static boolean isSupported(String version, String min, String max) {
        return compareVersions(version, min) >= 0 && compareVersions(version, max) <= 0;
    }

    /**
     *
     * @param   version     the version string in question
     * @param   compareTo   the version string being compared to version
     * @return  Integer     returns the difference of the specified strings
     *                      used to tell if version is newer or older than compareTo
     */
    public static Integer compareVersions(String version, String compareTo) {
        String[] versionString = version.split("\\.");
        String[] compareToString = compareTo.split("\\.");

        int i = 0;

        while (i < versionString.length && i < compareToString.length && versionString[i].equals(compareToString[i])) {
            i++;
        }

        if (i < versionString.length && i < compareToString.length) {

            int diff = Integer.valueOf(versionString[i]).compareTo(Integer.valueOf(compareToString[i]));

            return Integer.signum(diff);
        } else {
            return Integer.signum(versionString.length - compareToString.length);
        }
    }

    /**
     * Checks the hooked HashMap for the existence of the specified plugin using it's name.
     *
     * @param   name      the specified plugin's name
     * @return  boolean   whether or not the specified plugin exists inside the hooked HashMap
     */
    public static boolean isHooked(String name) {
        return hooked.get(name) != null;
    }
}
