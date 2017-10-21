package Listeners;

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

import Objects.PlayerWarpObject;
import PlayerWarpGUI.PlayerWarpGUI;

public class ChestListener implements Listener {

	public static PlayerWarpGUI pl;

	// -------------------------------------------------------------------------------------
	// Constructor
	// -------------------------------------------------------------------------------------
	public ChestListener(PlayerWarpGUI playerWarpGUI) {
		pl = playerWarpGUI;
	}

	@SuppressWarnings("resource")
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onInventoryClick(final InventoryClickEvent e) {

		// was it a player
		if (e.getWhoClicked() instanceof Player) {

			Player player = (Player) e.getWhoClicked();
			// does it match the right inventory name
			if (e.getInventory().getName()
					.contains(pl.getOtherFunctions().replaceColorVariables(pl.getConfig().getString("gui.title")))) {

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
					pl.getGuiObject().openGUI(player, getNextPageNumber(e.getCurrentItem().getItemMeta().getDisplayName()));
					// exit
					return;
				}

				// get warp Object
				PlayerWarpObject pwo = pl.getPlayerWarpObjects().get(getWarpID(e.getCurrentItem())-1);

				//check ban list
				if(pl.getPlayerWarpObjectHandler().isPlayerOnBannedList(pwo.getBanList(), player.getUniqueId().toString())) {
					pl.getMessageHandler().sendPlayerMessage(player, pl.getLanguageHandler().getMessage("COMMAND_BANNED_PLAYER"));
					closeInv(player);
					return;
				}
				
				// do safeWarp checking
				String errorMsg = pl.getWarpHandler().canTeleport(player, pl.getOtherFunctions().str2loc(pwo.getWarpLocation()));
				if (errorMsg != null) {
					pl.getMessageHandler().sendPlayerMessage(player, errorMsg);
					closeInv(player);
					return;
				}

				//start teleport
				pl.getTeleportHandler().startTeleport(player, pl.getOtherFunctions().str2loc(pwo.getWarpLocation()));

				// close inventory
				closeInv(player);

			}
		}

	}

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

	public boolean isNextPageIcon(String s) {
		if (s.contains(pl.getLanguageHandler().getMessage("NEXTPAGE_TEXT"))) {
			return true;
		}
		return false;
	}

	public void closeInv(Player player) {
		player.closeInventory();
	}

	public static int getWarpID(ItemStack itemStack) {
		int warpID = 0;
		List<String> loreList = new ArrayList<String>();
		loreList = itemStack.getItemMeta().getLore();
		
		for(int i = 0; i < loreList.size(); i++) {
			if(loreList.get(i).contains(ChatColor.stripColor(pl.getOtherFunctions().replaceColorVariables(pl.getLanguageHandler().getMessage("WARP_ID_TEXT"))))){
				warpID = Integer.parseInt(ChatColor.stripColor(loreList.get(3).replace(pl.getOtherFunctions().replaceColorVariables(pl.getLanguageHandler().getMessage("WARP_ID_TEXT")), "")));	
			}
		}
		
		return warpID;
	}

	// -------------------------------------------------------------------------------------
	//
	// -------------------------------------------------------------------------------------
	public boolean isSLotValid(int s) {
		if (s < 0) {
			return false;
		}
		return true;
	}

	// -------------------------------------------------------------------------------------
	//
	// -------------------------------------------------------------------------------------
	public boolean isMetaValid(ItemMeta im) {
		if (im == null) {
			return false;
		}
		if (im.getDisplayName() == null) {
			return false;
		}
		return true;

	}

	// -------------------------------------------------------------------------------------
	//
	// -------------------------------------------------------------------------------------
	public boolean isChestTypeValid(InventoryType it) {
		if (it != InventoryType.CHEST) {
			return false;
		}
		return true;
	}

}
