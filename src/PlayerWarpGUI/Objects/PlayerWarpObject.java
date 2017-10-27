package PlayerWarpGUI.Objects;

import java.util.ArrayList;
import java.util.UUID;

import PlayerWarpGUI.PlayerWarpGUI;

/**
 * Class Object of the actuall warp <br>
 * 
 * @author Judgetread
 * @version 1.0
 */
public class PlayerWarpObject {

	/** Plugin reference */
	public PlayerWarpGUI pl;

	
	/**
	 * Generate unique id as creating object.<br>
	 * This becomes the warpID.
	 */
	static int UNIQUE_ID = 0;
	int uid = ++UNIQUE_ID;

	private UUID playerUUID;
	private String warpName;
	private String warpLocation;
	private String title;
	private String icon;
	private ArrayList<String> loreList;
	private ArrayList<String> banList;

	/**
	 * Constructor
	 * 
	 * @param playerUUID
	 * @param warpName
	 * @param warpLocation
	 * @param title
	 * @param icon
	 * @param loreList
	 * @param banList
	 * @param pl
	 */
	public PlayerWarpObject(UUID playerUUID,String warpName, String warpLocation, String title, String icon, ArrayList<String> loreList, ArrayList<String> banList, PlayerWarpGUI pl) {
		
		this.pl = pl;
		this.setPlayerUUID(playerUUID);
		this.setWarpName(warpName);
		this.setWarpLocation(warpLocation);
		this.setTitle(title);
		this.setIcon(icon);
		this.setLoreList(loreList);
		this.setBanList(banList);
		
		PlayerWarpGUI.getPwoList().add(this);
		
		

	}
	
	
	/**
	 * Remove this object from pwolist, means will no long show in GUI.
	 */
	public void removePlayerWarpObject() {
		PlayerWarpGUI.getPwoList().remove(this);
}

	/**
	 * Returns this objects ID
	 * 
	 * @return integer
	 */
	public static int getUNIQUE_ID() {
		return UNIQUE_ID;
	}

	/**
	 * Set this objects ID
	 * 
	 * @param uNIQUE_ID
	 */
	public static void setUNIQUE_ID(int uNIQUE_ID) {
		UNIQUE_ID = uNIQUE_ID;
	}

	/**
	 * get this objects warp name.
	 * 
	 * @return String
	 */
	public String getWarpName() {
		return warpName;
	}

	/**
	 * Set this objects warp name.
	 * 
	 * @param warpName
	 */
	public void setWarpName(String warpName) {
		this.warpName = warpName;
	}

	/**
	 * Set this warps uid.
	 * 
	 * @param uid
	 */
	public void setUid(int uid) {
		this.uid = uid;
	}

	/**
	 * Get this objects uid.
	 * 
	 * @return the uid
	 */
	public int getUid() {
		return uid;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return uid;
	}

	/**
	 * Get this objects PlayerUUID
	 * 
	 * @return UUID
	 */
	public UUID getPlayerUUID() {
		return playerUUID;
	}

	/**
	 * Set this objects PlayerUUID
	 * 
	 * @param playerUUID
	 */
	public void setPlayerUUID(UUID playerUUID) {
		this.playerUUID = playerUUID;
	}

	/**
	 * Get this objects warp location.
	 * 
	 * @return String
	 */
	public String getWarpLocation() {
		return warpLocation;
	}

	/**
	 * Set this objects warp location
	 * 
	 * @param warpLocation
	 */
	public void setWarpLocation(String warpLocation) {
		this.warpLocation = warpLocation;
	}

	/**
	 * Get this objects title.
	 * 
	 * @return String
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Set this objects title
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Get this objects icon.
	 * 
	 * @return String
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * Set this objects icon
	 * 
	 * @param icon
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}


	/**
	 * Get this object lorelist
	 * 
	 * @return ArrayList<String>
	 */
	public ArrayList<String> getLoreList() {
		return loreList;
	}


	/**
	 * Set this objects lorelist
	 * 
	 * @param loreList
	 */
	public void setLoreList(ArrayList<String> loreList) {
		this.loreList = loreList;
	}

	/**
	 * Get this objects ban list.
	 * 
	 * @return ArrayList<String>
	 */
	public ArrayList<String> getBanList() {
		return banList;
	}

	/**
	 * Set this objects ban list.
	 * 
	 * @param banList
	 */
	public void setBanList(ArrayList<String> banList) {
		this.banList = banList;
	}

}
