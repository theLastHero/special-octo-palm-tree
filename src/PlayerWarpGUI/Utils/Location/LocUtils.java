package PlayerWarpGUI.Utils.Location;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import PlayerWarpGUI.PlayerWarpGUI;
import PlayerWarpGUI.Utils.StringUtils;
import PlayerWarpGUI.config.Config;

/**
 * Holds a bunch of methods related to Location processing. <br>
 * 
 * @author Judgetread
 * @version 1.0
 */
public class LocUtils {
	
	private static PlayerWarpGUI p;
	private static LocUtils instance;

	/**
	 * @param p
	 */
	
	public LocUtils(PlayerWarpGUI p) {
		LocUtils.p = p;
	}

	/**
	 * @return LocUtils
	 */
	public static LocUtils getInstance() {
		if (instance == null) {
			instance = new LocUtils(p);
		}

		return instance;
	}
	
	
	/**
	 * Checks if a location is safe (solid ground with 2 breathable blocks)
	 *
	 * @param location
	 *            Location to check
	 * @return True if location is safe
	 */
	@SuppressWarnings("unlikely-arg-type")
	public boolean isSafeLocation(Location location) {
		if (!Config.getInstance().getUseSafeLocation()) {
			return true;
		}
		Block feet = location.getBlock();
		if (!feet.getType().isTransparent() && !feet.getLocation().add(0, 1, 0).getBlock().getType().isTransparent()) {
			return false; // not transparent (will suffocate)
		}
		Block head = feet.getRelative(BlockFace.UP);
		if (!head.getType().isTransparent()) {
			return false; // not transparent (will suffocate)
		}
		Block ground = feet.getRelative(BlockFace.DOWN);
		if (!ground.getType().isSolid()) {
			return false; // not solid
		}

		// here check blocks to land on
		for (int i = 0; i < Config.getInstance().getUnsafeBlocks().size(); i++) {
			
			if (feet.getType().equals(StringUtils.getInstance().parseItemStackFromString(Config.getInstance().getUnsafeBlocks().get(i).toString()))) {
				return false; // not solid
			}
		
			if (feet.getLocation().add(0, -1, 0).getBlock().getRelative(BlockFace.UP).getType().equals(StringUtils.getInstance().parseItemStackFromString(Config.getInstance().getUnsafeBlocks().get(i).toString()))) {
				return false; // not solid
			}

			if (feet.getRelative(BlockFace.DOWN).getType().equals(StringUtils.getInstance().parseItemStackFromString(Config.getInstance().getUnsafeBlocks().get(i).toString()))) {
				return false; // not solid
			}
			
			if (feet.getLocation().add(0, 1, 0).getBlock().getType().equals(StringUtils.getInstance().parseItemStackFromString(Config.getInstance().getUnsafeBlocks().get(i).toString()))) {
				return false; // not solid
			}

		}

		return true;
	}

}
