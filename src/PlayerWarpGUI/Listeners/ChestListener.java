package PlayerWarpGUI.Listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import PlayerWarpGUI.PlayerWarpGUI;
import PlayerWarpGUI.Objects.GUIObject;
import PlayerWarpGUI.Objects.PlayerWarpObject;
import PlayerWarpGUI.Utils.StringUtils;
import PlayerWarpGUI.Utils.Warp.ObjectUtils;
import PlayerWarpGUI.config.Config;
import PlayerWarpGUI.locale.LocaleLoader;

public class ChestListener implements Listener {

	//private PlayerWarpGUI p;
	
	/**
	 * @param pl
	 */
	public ChestListener() {
		//this.p= pl;
	}

	/**
	 * When a item in a inventoryChest is clicked. Check if it in our GUI chest.<br>
	 * If it is a player warp item, then teleport player to that warps location.
	 * If its a next page icon, show player the next page of warp icons.
	 * 
	 * Perform safeLocation/ blocked worlds checks etc.
	 * TODO move some section into there own methods for better explanation/readability
	 * 
	 * @param e
	 */
	@EventHandler(priority = EventPriority.HIGH)
	public void onWarpItemClick(final InventoryClickEvent e) {

		// was it a player
		if (e.getWhoClicked() instanceof Player) {

			Player player = (Player) e.getWhoClicked();
			
			// does it match the right inventory name
			if (e.getInventory().getName()
					.contains(StringUtils.getInstance().replaceColorVariables(Config.getInstance().getGuiTitle()))) {

				// cancel event, prevent player from removing the item
				e.setCancelled(true);

				// check item was clicked, not outside window etc
				if (!isSLotValid(e.getSlot()) || !isMetaValid(e.getCurrentItem().getItemMeta())
						|| !isChestTypeValid(e.getInventory().getType())) {
					return;
				}

				// was it a next page icon clicked
				if (isNextPageIcon(e.getCurrentItem().getItemMeta().getDisplayName())
						&& (getNextPageNumber(e.getCurrentItem().getItemMeta().getDisplayName()) > 0)) {

					closeInv(player);
					// open new inventory from next page
					GUIObject.getInstance().openGUI(player,
							getNextPageNumber(e.getCurrentItem().getItemMeta().getDisplayName()));
					// exit
					return;
				}

				// get warp Object
				//PlayerWarpObject pwo = PlayerWarpGUI.pwoList.get(getWarpID(e.getCurrentItem()) - 1);
				
				ObjectUtils.getInstance();
				PlayerWarpObject pwo = ObjectUtils.getPlayerWarpObject(getWarpID(e.getCurrentItem()));

				// check ban list
				if (ObjectUtils.getInstance().isPlayerOnBannedList(pwo.getBanList(),
						player.getUniqueId().toString())) {
					player.sendMessage(
							LocaleLoader.getString("COMMAND_BANNED_PLAYER"));
					closeInv(player);
					return;
				}

				// do safeWarp checking
				String errorMsg = PlayerWarpGUI.p.getWarpHandler().canTeleport(StringUtils.getInstance().str2loc(pwo.getWarpLocation()));
				if (errorMsg != null) {
					player.sendMessage(errorMsg);
					closeInv(player);
					return;
				}

				// start teleport
				PlayerWarpGUI.p.getTpHandler().startTeleport(player, StringUtils.getInstance().str2loc(pwo.getWarpLocation()));

				// close inventory
				closeInv(player);
			}
		}

	}

	/**
	 * Return the next page number for the GUI to show to player.
	 * 
	 * @param s
	 * @return integer
	 */
	@SuppressWarnings("resource")
	public int getNextPageNumber(String s) {
		// get next page number
		Scanner scanner = null;
		int nextPageNum = 0;
		try {
			scanner = new Scanner(s).useDelimiter("[^0-9]+");
			nextPageNum = scanner.nextInt();
		} finally {
			scanner.close();
		}
		return nextPageNum;
	}

	/**
	 * Checks if item clicked is next page icon.
	 * 
	 * @param s
	 * @return true/false
	 */
	public boolean isNextPageIcon(String s) {
		if (s.contains(LocaleLoader.getString("NEXTPAGE_TEXT"))) {
			return true;
		}
		return false;
	}

	/**
	 * Close the inventory for Player
	 * 
	 * @param player
	 */
	
	public void closeInv(Player player) {
		player.closeInventory();
	}

	/**
	 * Get the warp id from the item that was clicked.
	 * 
	 * @param itemStack
	 * @return integer
	 */
	public int getWarpID(ItemStack itemStack) {
		int warpID = 0;
		List<String> loreList = new ArrayList<String>();
		loreList = itemStack.getItemMeta().getLore();

		for (int i = 0; i < loreList.size(); i++) {
			if (loreList.get(i).contains(ChatColor.stripColor(StringUtils.getInstance()
					.replaceColorVariables(LocaleLoader.getString("WARP_ID_TEXT"))))) {
				Bukkit.getConsoleSender().sendMessage("dddddd " + loreList.get(i));
				warpID = Integer.parseInt(ChatColor.stripColor(loreList.get(i).replace(StringUtils.getInstance()
						.replaceColorVariables(LocaleLoader.getString("WARP_ID_TEXT")), "")));
			}
		}

		return warpID;
	}

	/**
	 * Make sure a slot was clicked. Throws console error otherwise.
	 * 
	 * @param s
	 * @return true/false
	 */
	public boolean isSLotValid(int s) {
		if (s < 0) {
			return false;
		}
		return true;
	}

	/**
	 * Make sure a Meta Data is valid. Throws console error otherwise.
	 * 
	 * @param im
	 * @return true/false
	 */
	public boolean isMetaValid(ItemMeta im) {
		if (im == null) {
			return false;
		}
		if (im.getDisplayName() == null) {
			return false;
		}
		return true;

	}

	/**
	 * Check inventory type matches ours.
	 * 
	 * @param it
	 * @return true/false
	 */
	public boolean isChestTypeValid(InventoryType it) {
		if (it != InventoryType.CHEST) {
			return false;
		}
		return true;
	}

}
