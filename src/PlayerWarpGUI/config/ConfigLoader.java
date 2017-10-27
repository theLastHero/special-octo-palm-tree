package PlayerWarpGUI.config;

import java.io.File;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import PlayerWarpGUI.PlayerWarpGUI;

/**
 * Config abstract class.<br>
 * 
 * @author Judgetread
 * @version 1.0
 */
abstract class ConfigLoader {
	protected static final PlayerWarpGUI plugin = PlayerWarpGUI.p;
	protected String fileName;
	private File configFile;
	protected FileConfiguration config;

	/**
	 * @param relativePath
	 * @param fileName
	 */
	ConfigLoader(String relativePath, String fileName) {
		this.fileName = fileName;
		configFile = new File(plugin.getDataFolder(), relativePath + File.separator + fileName);
		loadFile();
	}

	/**
	 * @param fileName
	 */
	ConfigLoader(String fileName) {
		this.fileName = fileName;
		configFile = new File(plugin.getDataFolder(), fileName);
		loadFile();
	}

	/**
	 * Try load config file for this instance. If not try create it from a resource.
	 */
	protected void loadFile() {
		if (!configFile.exists()) {
			try {
				plugin.saveResource(fileName, false); // Normal files
			} catch (IllegalArgumentException ex) {
				plugin.saveResource(configFile.getParentFile().getName() + File.separator + fileName, false); // Mod
																												// files
			}
		}
		config = YamlConfiguration.loadConfiguration(configFile);
	}

	/**
	 * loadKeys, method to overwrite from abstract.<br>
	 * {@link Config#loadKeys()}
	 */
	protected abstract void loadKeys();

	/**
	 * validateKeys, method to overwrite from abstract.<br>
	 * {@link Config#validateKeys()}
	 */
	protected boolean validateKeys() {
		return true;
	}

	/**
	 * Checks for saved error in config. Print errors to console.
	 * 
	 * @param issues
	 * @return boolean of true/false.
	 */
	protected boolean noErrorsInConfig(List<String> issues) {
		for (String issue : issues) {
			plugin.getLogger().warning(issue);
		}

		return issues.isEmpty();
	}

	
	/**
	 * Check {@link #validateKeys()}, if errors shut plugin down.
	 */
	protected void validate() {
		if (validateKeys()) {
			Bukkit.getConsoleSender().sendMessage("No errors found in " + fileName + "!");
		} else {
			Bukkit.getConsoleSender().sendMessage("Errors were found in " + fileName + "! mcMMO was disabled!");
			PlayerWarpGUI.killPlugin();
			PlayerWarpGUI.p.setNoErrorsInConfigFiles(false);
		}
	}

	
	/**
	 * @return File
	 */
	public File getFile() {
		return configFile;
	}

	/**
	 * Create backup copy of old config. Create new updated config.
	 */
	public void backup() {
		plugin.getLogger().warning("You are using an old version of the " + fileName + " file.");
		plugin.getLogger().warning(
				"Your old file has been renamed to " + fileName + ".old and has been replaced by an updated version.");

		configFile.renameTo(new File(configFile.getPath() + ".old"));

		if (plugin.getResource(fileName) != null) {
			plugin.saveResource(fileName, true);
		}

		plugin.getLogger().warning("Reloading " + fileName + " with new values...");
		loadFile();
		loadKeys();
	}
}