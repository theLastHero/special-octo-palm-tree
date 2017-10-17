package Objects;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import PlayerWarpGUI.PlayerWarpGUI;

public class GUIObject {

	public static PlayerWarpGUI pl;

	// +-----------------------------------------------------------------------------------
	// | Constructor
	// +-----------------------------------------------------------------------------------
	public GUIObject(PlayerWarpGUI playerWarpGUI) {
		pl = playerWarpGUI;
	}

	// -------------------------------------------------------------------------------------
	//
	// -------------------------------------------------------------------------------------
	@SuppressWarnings({ "unused", "deprecation" })
	public void openGUI(Player player, int page) {

		int pageNumber = page;
		int chestSize = pl.getConfig().getInt("gui.rows") * 9;

		String chestTitle = pl.getOtherFunctions().replaceColorVariables(pl.getConfig().getString("gui.title"));

		int pageSize = (chestSize - 1);
		int startNum = 0; // decalre variable
		int pageNum = page; // what page to start from
		boolean showNext = true; // to show next page icon or not

		boolean enabled = true;
		String icon = null;

		// set next page icon

		// create inventory
		Inventory inv = Bukkit.createInventory(null, chestSize, chestTitle);

		// create array list of warps by name from hashmap
		ArrayList<PlayerWarpObject> playerWarpObjects = pl.getPlayerWarpObjects();

		// set start
		startNum = pageNum * pageSize;

		// calculate loop size
		int loopSize = startNum + pageSize;

		// check if page size is smaller then max pageSize
		// if not then set to actual size, and set showNext to false
		if (loopSize > (playerWarpObjects.size())) {
			loopSize = ((playerWarpObjects.size()) - startNum);
			showNext = false;
		}

		// loop through all for the page
		for (int i = 0; i < loopSize; i++) {
			int objNum = startNum + i;
			
			// warp title
			// ============================
			String playerWarpTitle = getWarpTitle(playerWarpObjects.get(objNum));
			
			// warp icon
			// ============================
			ItemStack playerWarpItemStack = getWarpIcon(playerWarpObjects.get(objNum));

			// warp lore
			// ===========================
			ArrayList<String> lore = getWarpLore(playerWarpObjects.get(objNum));

			// meta
			// ===========================
			playerWarpItemStack.setItemMeta(getWarpMeta(playerWarpTitle, lore));
			
			inv.setItem(i, playerWarpItemStack);
			// i++;

		}

		// if going to show nextPage icon or not
		if (showNext) {
			ItemStack nextPageItemStack = getNextPageItemStack(page + 1);
			inv.setItem(pageSize, nextPageItemStack);
		}

		player.openInventory(inv);
	}

	// -------------------------------------------------------------------------------------
	//
	// -------------------------------------------------------------------------------------
	public static ItemMeta getWarpMeta(String playerWarpTitle, ArrayList<String> lore) {
		ItemMeta playerWarpMeta = new ItemStack(Material.SKULL_ITEM, 1, (short) 3).getItemMeta();
		playerWarpMeta.setDisplayName(playerWarpTitle);
		playerWarpMeta.setLore(lore);
		playerWarpMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		
		return playerWarpMeta;
	}

	// -------------------------------------------------------------------------------------
	//
	// -------------------------------------------------------------------------------------
	public static ArrayList<String> getWarpLore(PlayerWarpObject pwo) {
		
		ArrayList<String> lore = new ArrayList<String>();
		ArrayList<String> loreList = pwo.getLoreList();
		lore.add(pl.getOtherFunctions().replaceColorVariables(loreList.get(0)));
		lore.add(pl.getOtherFunctions().replaceColorVariables(loreList.get(1)));
		lore.add(pl.getOtherFunctions().replaceColorVariables(loreList.get(2)));
		lore.add(ChatColor.DARK_GRAY + "Warp ID: " + pwo.getUid());
		return lore;

	}

	// -------------------------------------------------------------------------------------
	//
	// -------------------------------------------------------------------------------------
	public static String getWarpTitle(PlayerWarpObject pwo) {
		if (!(pwo.getTitle() == null) && !(pwo.getTitle().length() == 0)) {
			return pl.getOtherFunctions().replaceColorVariables(pwo.getTitle());
		}
		
		return  pl.getOtherFunctions().replaceHolders(
				pl.getOtherFunctions().replaceColorVariables(pl.getConfig().getString("gui.default-playerwarp-title")),
				Bukkit.getOfflinePlayer(pwo.getPlayerUUID()).getName());

	}

	// -------------------------------------------------------------------------------------
	//
	// -------------------------------------------------------------------------------------
	public static ItemStack getWarpIcon(PlayerWarpObject pwo) {
		if (pl.getConfig().getBoolean("gui.use-playerheads-as-icons")) {
			return getPlayerSkullItem(pwo.getPlayerUUID());
		}

		if (!(pwo.getIcon() == null) && !(pwo.getIcon().length() == 0)) {
			return pl.getOtherFunctions().parseItemStackFromString(pwo.getIcon());
		}

		return pl.getOtherFunctions().parseItemStackFromString(pl.getConfig().getString("gui.default-playerwarp-icon"));

	}

	// -------------------------------------------------------------------------------------
	//
	// -------------------------------------------------------------------------------------
	@SuppressWarnings("deprecation")
	public static ItemStack getPlayerSkullItem(UUID uuid) {

		ItemStack Item_Skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		SkullMeta Meta_Skull = (SkullMeta) Item_Skull.getItemMeta();
		ArrayList<String> Lore_Skull = new ArrayList<>();

		Meta_Skull.setOwner(Bukkit.getOfflinePlayer(uuid).getName());
		Lore_Skull.clear();
		Lore_Skull.add(null);
		Item_Skull.setItemMeta(Meta_Skull);

		return Item_Skull;
	}

	// -------------------------------------------------------------------------------------
	//
	// -------------------------------------------------------------------------------------
	public static ItemStack getNextPageItemStack(int pageNum) {

		ItemStack nextPageItemstack = pl.getOtherFunctions()
				.parseItemStackFromString(pl.getConfig().getString("gui.nextpage-icon"));
		ItemMeta nextPageMeta = nextPageItemstack.getItemMeta();
		nextPageMeta.setDisplayName(pl.getLanguageHandler().getMessage("TEXT_NEXTPAGE") + pageNum);
		nextPageItemstack.setItemMeta(nextPageMeta);

		return nextPageItemstack;
	}
}
