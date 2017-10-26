package PlayerWarpGUI.config;

import java.io.File;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import PlayerWarpGUI.PlayerWarpGUI;


abstract class ConfigLoader {
    protected static final PlayerWarpGUI plugin = PlayerWarpGUI.p;
    protected String fileName;
    private File configFile;
    protected FileConfiguration config;

    ConfigLoader(String relativePath, String fileName) {
        this.fileName = fileName;
        configFile = new File(plugin.getDataFolder(), relativePath + File.separator + fileName);
        loadFile();
    }

    ConfigLoader(String fileName) {
        this.fileName = fileName;
        configFile = new File(plugin.getDataFolder(), fileName);
        loadFile();
    }

    protected void loadFile() {
        if (!configFile.exists()) {
          // Bukkit.getConsoleSender().sendMessage(LocaleLoader.getString("CONSOLE_MSG_PREFIX") + LocaleLoader.getString("CONSOLE_MSG_CREATE_CONFIG", configFile.getName()));

            try {
                plugin.saveResource(fileName, false); // Normal files
            }
            catch (IllegalArgumentException ex) {
                plugin.saveResource(configFile.getParentFile().getName() + File.separator + fileName, false); // Mod files
            }
        }
        else {
          //  plugin.debug("Loading mcMMO " + fileName + " File...");
        }

        config = YamlConfiguration.loadConfiguration(configFile);
    }

    protected abstract void loadKeys();

    protected boolean validateKeys() {
        return true;
    }

    protected boolean noErrorsInConfig(List<String> issues) {
        for (String issue : issues) {
            plugin.getLogger().warning(issue);
        }

        return issues.isEmpty();
    }

    protected void validate() {
        if (validateKeys()) {
         //   plugin.debug("No errors found in " + fileName + "!");
        }
        else {
            plugin.getLogger().warning("Errors were found in " + fileName + "! mcMMO was disabled!");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
            plugin.setNoErrorsInConfigFiles(false);
        }
    }

    public File getFile() {
        return configFile;
    }

    public void backup() {
        plugin.getLogger().warning("You are using an old version of the " + fileName + " file.");
        plugin.getLogger().warning("Your old file has been renamed to " + fileName + ".old and has been replaced by an updated version.");

        configFile.renameTo(new File(configFile.getPath() + ".old"));

        if (plugin.getResource(fileName) != null) {
            plugin.saveResource(fileName, true);
        }

        plugin.getLogger().warning("Reloading " + fileName + " with new values...");
        loadFile();
        loadKeys();
    }
}