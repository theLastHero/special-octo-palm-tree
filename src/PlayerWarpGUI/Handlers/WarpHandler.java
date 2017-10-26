package PlayerWarpGUI.Handlers;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import PlayerWarpGUI.config.Config;
import PlayerWarpGUI.locale.LocaleLoader;
import PlayerWarpGUI.PlayerWarpGUI;
import PlayerWarpGUI.Utils.StringUtils;

public class WarpHandler {

	private PlayerWarpGUI pl;

	/**
	 * @param pl
	 */
	public WarpHandler(PlayerWarpGUI pl) {
		this.pl = pl;
	}

	/**
	 * @param p
	 * @param location
	 * @return
	 */
	public String canTeleport(Player p, Location location) {

		if (location == null) {
			return LocaleLoader.getString("TELEPORT_CANCEL_INVALID_LOCATION");
		}
		
		if (!isSafeLocation(location)) {
			return LocaleLoader.getString("TELEPORT_CANCEL_UNSAFE_LOCATION");
		}
		
		if (isBlockedWorld(location)) {
			return LocaleLoader.getString("TELEPORT_CANCEL_WORLD_BLOCKED");
		}


		return null;

	}
	
	/**
	 * @param location
	 * @return
	 */
	
	public boolean isBlockedWorld(Location location) {
		
		for (int i = 0; i < pl.getConfig().getStringList("teleport.blocked-world").size(); i++) {
			if (pl.getConfig().getStringList("teleport.blocked-world").get(i).equalsIgnoreCase(location.getWorld().getName().toString())) {
				return true;
			}
		}
		return false;
		
		
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
