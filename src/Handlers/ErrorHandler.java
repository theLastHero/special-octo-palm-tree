package Handlers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import PlayerWarpGUI.PlayerWarpGUI;

public class ErrorHandler {

	public static PlayerWarpGUI pl;

	// +-----------------------------------------------------------------------------------
	// | Constructor
	// +-----------------------------------------------------------------------------------
	public ErrorHandler(PlayerWarpGUI playerWarpGUI) {
		pl = playerWarpGUI;
	}

	// +-----------------------------------------------------------------------------------
	// | displayConsoleTitle
	// +-----------------------------------------------------------------------------------
	public void displayConsoleTitle() {

		String status = pl.getMessageHandler().getMessage("LOAD_PASSED");
		String loadFailed = pl.getMessageHandler().getMessage("LOAD_FAILED");

		if (checkEnableErrorSize()) {
			status = loadFailed;
		}

		Bukkit.getLogger().info(".-. .   .-. . . .-. .-. . . . .-. .-. .-. .-. . . .-. ");
		Bukkit.getLogger().info("|-' |   |-|  |  |-  |(  | | | |-| |(  |-' |.. | |  |  ");
		Bukkit.getLogger().info("'   `-' ` '  `  `-' ' ' `.'.' ` ' ' ' '   `-' `-' `-' ");
		Bukkit.getLogger().info("[PlayerWarpGUI] v" + pl.PlayerWarpGUIVersion + " " + status);

		displayConsoleErrors();

	}

	// +-----------------------------------------------------------------------------------
	// | checkEnableErrorSize
	// +-----------------------------------------------------------------------------------
	public boolean checkEnableErrorSize() {
		if (pl.EnableErrors.size() > 0) {
			return true;
		}
		return false;
	}

	// +-----------------------------------------------------------------------------------
	// | displayConsoleErrors
	// +-----------------------------------------------------------------------------------
	public void displayConsoleErrors() {
		if (checkEnableErrorSize()) {
			for (String error : pl.EnableErrors) {
				Bukkit.getLogger().info(pl.getMessageHandler().getMessage("LOAD_FAILED") + error);
			}
			Bukkit.getPluginManager().disablePlugin(pl);
		}
	}

	// +-----------------------------------------------------------------------------------
	// | addError
	// +-----------------------------------------------------------------------------------
	public void addError(String error) {
		pl.EnableErrors.add(error);
	}
}
