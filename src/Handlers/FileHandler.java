package Handlers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.bukkit.configuration.file.FileConfiguration;

import PlayerWarpGUI.PlayerWarpGUI;

public class FileHandler {
	
	public static PlayerWarpGUI pl;
	FileConfiguration config;


	// +-------------------------------------------------------------------------------------
	// | Constructor
	// +-----------------------------------------------------------------------------------
	public FileHandler(PlayerWarpGUI playerWarpGUI) {
		pl = playerWarpGUI;
	}
	
	// +-----------------------------------------------------------------------------------
	// | check
	// +-----------------------------------------------------------------------------------
	public void checkFileExsists(String loc, String file, String res) {
		File configFile = new File(loc+file);

		// Check it exsists
		if (!configFile.exists()) {
			createFile(loc,file, res);
		}
	}
	
	public void createFile(String loc, String file, String res) {
		File newFile = new File(loc, file);
		newFile.getParentFile().mkdirs();
		copy(pl.getResource(res), newFile);
	}
	
	// +-----------------------------------------------------------------------------------
	// | copy
	// +-----------------------------------------------------------------------------------
	public void copy(InputStream in, File file) {

		try {
			OutputStream out = new FileOutputStream(file);
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			out.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
