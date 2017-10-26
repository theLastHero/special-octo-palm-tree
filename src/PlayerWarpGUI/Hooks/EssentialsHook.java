package PlayerWarpGUI.Hooks;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.earth2me.essentials.Essentials;

import PlayerWarpGUI.config.Config;
import br.net.fabiozumbi12.RedProtect.Region;
import br.net.fabiozumbi12.RedProtect.API.RedProtectAPI;

public class EssentialsHook extends HookManager<Object>{

	private static String plName = "Essentials";
	private static boolean configEnabled = Config.getInstance().getESEnabled();
	public static Essentials ess3 = null;
	
	public EssentialsHook() {
		super(plName, configEnabled);
	}

	public void setBackUser(Player player) {
		ess3 = (Essentials) getpHook();
		ess3.getUser(player).setLastLocation();
	}
	
	public boolean useBack() {
		
		if(this.isEnabled) {
			if(Config.getInstance().getESEnabled()) {
				if(Config.getInstance().getESBack()) {
					return true;
				}
			}
		}
		
		return false;
	}

	@Override
	public String warpHookCheck(Player player) {
		return hookedPluginName;
			}

	@Override
	public Region getLocationData(Location location) {
		return RedProtectAPI.getRegion(location);
	}

}