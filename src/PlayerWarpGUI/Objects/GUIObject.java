package PlayerWarpGUI.Objects;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import PlayerWarpGUI.PlayerWarpGUI;
import config.Config;
import locale.LocaleLoader;

public class GUIObject {

	private PlayerWarpGUI pl;
	
	/**
	 * @param pl
	 */
	public GUIObject(PlayerWarpGUI pl) {
		this.pl = pl;
	}

	// -------------------------------------------------------------------------------------
	//
	// -------------------------------------------------------------------------------------
	@SuppressWarnings({ "unused" })
	public void openGUI(Player player, int page) {

		int pageNumber = page;
		int chestSize = Config.getInstance().getGuiRows() * 9;

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

			// warp icon
			// ============================
			ItemStack playerWarpItemStack = getWarpIcon(playerWarpObjects.get(objNum));

			// warp title
			// ============================
			String playerWarpTitle = getWarpTitle(playerWarpObjects.get(objNum));

			// warp lore
			// ===========================
			ArrayList<String> lore = getWarpLore(playerWarpObjects.get(objNum));

			// meta
			// ===========================
			playerWarpItemStack.setItemMeta(getWarpMeta(playerWarpItemStack, playerWarpTitle, lore));

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
	public static ItemMeta getWarpMeta(ItemStack playerWarpItemStack, String playerWarpTitle, ArrayList<String> lore) {
		ItemMeta playerWarpMeta = playerWarpItemStack.getItemMeta();
		playerWarpMeta.setDisplayName(playerWarpTitle);
		playerWarpMeta.setLore(lore);
		playerWarpMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

		return playerWarpMeta;
	}

	// -------------------------------------------------------------------------------------
	//
	// -------------------------------------------------------------------------------------
	public ArrayList<String> getWarpLore(PlayerWarpObject pwo) {

		ArrayList<String> lore = new ArrayList<String>();
		ArrayList<String> loreList = pwo.getLoreList();
		for (int i = 0; i < loreList.size(); i++) {
			lore.add(pl.getOtherFunctions().replaceColorVariables(loreList.get(i)));
		}
		lore.add(pl.getOtherFunctions()
				.replaceColorVariables(LocaleLoader.getString("WARP_ID_TEXT") + pwo.getUid()));
		lore.add(pl.getOtherFunctions().replaceColorVariables(LocaleLoader.getString("WARP_OWNER_TEXT")
				+ Bukkit.getOfflinePlayer(pwo.getPlayerUUID()).getName()));
		return lore;

	}

	// -------------------------------------------------------------------------------------
	//
	// -------------------------------------------------------------------------------------
	public String getWarpTitle(PlayerWarpObject pwo) {
		if (!(pwo.getTitle() == null) && !(pwo.getTitle().length() == 0)) {
			return pl.getOtherFunctions().replaceColorVariables(pwo.getTitle());
		}

		return pl.getOtherFunctions().replaceHolders(Config.getInstance().getDefaultTitle(),
				Bukkit.getOfflinePlayer(pwo.getPlayerUUID()).getName());

	}

	// -------------------------------------------------------------------------------------
	//
	// -------------------------------------------------------------------------------------
	public ItemStack getWarpIcon(PlayerWarpObject pwo) {
		if (Config.getInstance().getUsePlayerHeads()) {
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
	public ItemStack getNextPageItemStack(int pageNum) {

		ItemStack nextPageItemstack = pl.getOtherFunctions()
				.parseItemStackFromString(Config.getInstance().getNextpageIcon());
		ItemMeta nextPageMeta = nextPageItemstack.getItemMeta();
		nextPageMeta.setDisplayName(pl.localeLoader.getString("TEXT_NEXTPAGE") + pageNum);
		nextPageItemstack.setItemMeta(nextPageMeta);

		return nextPageItemstack;
	}
}