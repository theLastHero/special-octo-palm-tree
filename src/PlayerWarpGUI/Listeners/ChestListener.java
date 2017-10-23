package PlayerWarpGUI.Listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
import PlayerWarpGUI.Objects.PlayerWarpObject;

public class ChestListener implements Listener {

	private PlayerWarpGUI p;
	
	/**
	 * @param pl
	 */
	public ChestListener(PlayerWarpGUI pl) {
		this.p= pl;
	}

	/**
	 * @param e
	 */
	@EventHandler(priority = EventPriority.HIGH)
	public void onWarpItemClick(final InventoryClickEvent e) {

		// was it a player
		if (e.getWhoClicked() instanceof Player) {

			Player player = (Player) e.getWhoClicked();
			// does it match the right inventory name
			if (e.getInventory().getName()
					.contains(p.getOtherFunctions().replaceColorVariables(p.getConfig().getString("gui.title")))) {

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
					p.getGuiObject().openGUI(player,
							getNextPageNumber(e.getCurrentItem().getItemMeta().getDisplayName()));
					// exit
					return;
				}

				// get warp Object
				PlayerWarpObject pwo = p.getPlayerWarpObjects().get(getWarpID(e.getCurrentItem()) - 1);

				// check ban list
				if (p.getPlayerWarpObjectHandler().isPlayerOnBannedList(pwo.getBanList(),
						player.getUniqueId().toString())) {
					p.getMsgSend().send(player,
							p.localeLoader.getString("COMMAND_BANNED_PLAYER"));
					closeInv(player);
					return;
				}

				// do safeWarp checking
				String errorMsg = p.getWarpHandler().canTeleport(player,
						p.getOtherFunctions().str2loc(pwo.getWarpLocation()));
				if (errorMsg != null) {
					p.getMsgSend().send(player, errorMsg);
					closeInv(player);
					return;
				}

				// start teleport
				p.getTeleportHandler().startTeleport(player, p.getOtherFunctions().str2loc(pwo.getWarpLocation()));

				// close inventory
				closeInv(player);
			}
		}

	}

	/**
	 * @param s
	 * @return
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
	 * @param s
	 * @return
	 */
	public boolean isNextPageIcon(String s) {
		if (s.contains(p.localeLoader.getString("NEXTPAGE_TEXT"))) {
			return true;
		}
		return false;
	}

	/**
	 * @param player
	 */
	public void closeInv(Player player) {
		player.closeInventory();
	}

	/**
	 * @param itemStack
	 * @return
	 */
	public int getWarpID(ItemStack itemStack) {
		int warpID = 0;
		List<String> loreList = new ArrayList<String>();
		loreList = itemStack.getItemMeta().getLore();

		for (int i = 0; i < loreList.size(); i++) {
			if (loreList.get(i).contains(ChatColor.stripColor(p.getOtherFunctions()
					.replaceColorVariables(p.localeLoader.getString("WARP_ID_TEXT"))))) {
				warpID = Integer.parseInt(ChatColor.stripColor(loreList.get(3).replace(p.getOtherFunctions()
						.replaceColorVariables(p.localeLoader.getString("WARP_ID_TEXT")), "")));
			}
		}

		return warpID;
	}

	/**
	 * @param s
	 * @return
	 */
	public boolean isSLotValid(int s) {
		if (s < 0) {
			return false;
		}
		return true;
	}

	/**
	 * @param im
	 * @return
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
	 * @param it
	 * @return
	 */
	public boolean isChestTypeValid(InventoryType it) {
		if (it != InventoryType.CHEST) {
			return false;
		}
		return true;
	}

}