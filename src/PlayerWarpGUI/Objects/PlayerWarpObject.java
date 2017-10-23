package PlayerWarpGUI.Objects;

import java.util.ArrayList;
import java.util.UUID;

import PlayerWarpGUI.PlayerWarpGUI;

public class PlayerWarpObject {

	public PlayerWarpGUI pl;

	static int UNIQUE_ID = 0;
	int uid = ++UNIQUE_ID;

	private UUID playerUUID;
	private String warpName;
	private String warpLocation;
	private String title;
	private String icon;
	private ArrayList<String> loreList;
	private ArrayList<String> banList;

	public PlayerWarpObject(UUID playerUUID,String warpName, String warpLocation, String title, String icon, ArrayList<String> loreList, ArrayList<String> banList, PlayerWarpGUI pl) {

		this.pl = pl;
		this.setPlayerUUID(playerUUID);
		this.setWarpName(warpName);
		this.setWarpLocation(warpLocation);
		this.setTitle(title);
		this.setIcon(icon);
		this.setLoreList(loreList);
		this.setBanList(banList);
		
		pl.pwoList.add(this);

	}
	
	public void removePlayerWarpObject() {
		pl.pwoList.remove(this);
}

	public static int getUNIQUE_ID() {
		return UNIQUE_ID;
	}

	public static void setUNIQUE_ID(int uNIQUE_ID) {
		UNIQUE_ID = uNIQUE_ID;
	}

	public String getWarpName() {
		return warpName;
	}

	public void setWarpName(String warpName) {
		this.warpName = warpName;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	/**
	 * @return the uid
	 */
	public int getUid() {
		return uid;
	}

	@Override
	public int hashCode() {
		return uid;
	}

	public UUID getPlayerUUID() {
		return playerUUID;
	}

	public void setPlayerUUID(UUID playerUUID) {
		this.playerUUID = playerUUID;
	}

	public String getWarpLocation() {
		return warpLocation;
	}

	public void setWarpLocation(String warpLocation) {
		this.warpLocation = warpLocation;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public ArrayList<String> getLoreList() {
		return loreList;
	}

	public void setLoreList(ArrayList<String> loreList) {
		this.loreList = loreList;
	}

	public ArrayList<String> getBanList() {
		return banList;
	}

	public void setBanList(ArrayList<String> banList) {
		this.banList = banList;
	}

}
