package PlayerWarpGUI.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;

/**
* Handles all calls to get config data. Verifys data.
*  
* @author Judgetread
* @version 1.0
*/
public class Config extends AutoUpdateConfigLoader {
    private static Config instance;

    /**
     * Constructor
     */
    private Config() {
        super("config.yml");
        validate();
    }

    /**
     * @return Config instance
     */
    public static Config getInstance() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }

    /**
     * check key KEYS are valid type and/or size.
     * 
     * @Return true/false id keys are valid 
     * @see PlayerWarpGUI.config.ConfigLoader#validateKeys()
     */
    @Override
    protected boolean validateKeys() {
        // Validate all the settings!
        List<String> reason = new ArrayList<String>();
/*
         General Settings 
        if (getSaveInterval() <= 0) {
            reason.add("General.Save_Interval should be greater than 0!");
        }
         Mob Healthbar 
        if (getMobHealthbarTime() == 0) {
            reason.add("Mob_Healthbar.Display_Time cannot be 0! Set to -1 to disable or set a valid value.");
        }

         Scoreboards 
        if (getRankScoreboardTime() != -1 && getRankScoreboardTime() <= 0) {
            reason.add("Scoreboard.Types.Rank.Display_Time should be greater than 0, or -1!");
        }

        if (getStatsScoreboardTime() != -1 && getStatsScoreboardTime() <= 0) {
            reason.add("Scoreboard.Types.Stats.Display_Time should be greater than 0, or -1!");
        }

        if (getTopScoreboardTime() != -1 && getTopScoreboardTime() <= 0) {
            reason.add("Scoreboard.Types.Top.Display_Time should be greater than 0, or -1!");
        }

        if (getInspectScoreboardTime() != -1 && getInspectScoreboardTime() <= 0) {
            reason.add("Scoreboard.Types.Inspect.Display_Time should be greater than 0, or -1!");
        }

        if (getSkillScoreboardTime() != -1 && getSkillScoreboardTime() <= 0) {
            reason.add("Scoreboard.Types.Skill.Display_Time should be greater than 0, or -1!");
        }

        if (getSkillLevelUpTime() != -1 && getSkillScoreboardTime() <= 0) {
            reason.add("Scoreboard.Types.Skill.Display_Time should be greater than 0, or -1!");
        }

        if (!(getRankUseChat() || getRankUseBoard())) {
            reason.add("Either Board or Print in Scoreboard.Types.Rank must be true!");
        }

        if (!(getTopUseChat() || getTopUseBoard())) {
            reason.add("Either Board or Print in Scoreboard.Types.Top must be true!");
        }

        if (!(getStatsUseChat() || getStatsUseBoard())) {
            reason.add("Either Board or Print in Scoreboard.Types.Stats must be true!");
        }

        if (!(getInspectUseChat() || getInspectUseBoard())) {
            reason.add("Either Board or Print in Scoreboard.Types.Inspect must be true!");
        }

         Database Purging 
        if (getPurgeInterval() < -1) {
            reason.add("Database_Purging.Purge_Interval should be greater than, or equal to -1!");
        }

        if (getOldUsersCutoff() != -1 && getOldUsersCutoff() <= 0) {
            reason.add("Database_Purging.Old_User_Cutoff should be greater than 0 or -1!");
        }

  

         Items 
        if (getChimaeraUseCost() < 1 || getChimaeraUseCost() > 64) {
            reason.add("Items.Chimaera_Wing.Use_Cost only accepts values from 1 to 64!");
        }

        if (getChimaeraRecipeCost() < 1 || getChimaeraRecipeCost() > 9) {
            reason.add("Items.Chimaera_Wing.Recipe_Cost only accepts values from 1 to 9!");
        }

        if (getChimaeraItem() == null) {
            reason.add("Items.Chimaera_Wing.Item_Name is invalid!");
        }

         Particles 
        if (getLevelUpEffectsTier() < 1) {
            reason.add("Particles.LevelUp_Tier should be at least 1!");
        }

         PARTY SETTINGS 
        if (getAutoPartyKickInterval() < -1) {
            reason.add("Party.AutoKick_Interval should be at least -1!");
        }

        if (getAutoPartyKickTime() < 0) {
            reason.add("Party.Old_Party_Member_Cutoff should be at least 0!");
        }

        if (getPartyShareBonusBase() <= 0) {
            reason.add("Party.Sharing.ExpShare_bonus_base should be greater than 0!");
        }

        if (getPartyShareBonusIncrease() < 0) {
            reason.add("Party.Sharing.ExpShare_bonus_increase should be at least 0!");
        }

        if (getPartyShareBonusCap() <= 0) {
            reason.add("Party.Sharing.ExpShare_bonus_cap should be greater than 0!");
        }

        if (getPartyShareRange() <= 0) {
            reason.add("Party.Sharing.Range should be greater than 0!");
        }

        if (getPartyXpCurveMultiplier() < 1) {
            reason.add("Party.Leveling.Xp_Curve_Modifier should be at least 1!");
        }*/
        
        return noErrorsInConfig(reason);
    }

    
    /**
     * Overwritten abstract method. Load keys grab config data.
     * @see PlayerWarpGUI.config.ConfigLoader#loadKeys()
     */
    @Override
    protected void loadKeys() {}

    /*
     * GENERAL SETTINGS
     */

    /* General Settings */
    public String getLocale() { return config.getString("general.Locale", "en_us"); }
    
    /* Teleport */
    public boolean getTeleportCancel() { return config.getBoolean("teleport.cancel-on-movement", true); }
    public int getTeleportCancelCooldown() { return config.getInt("teleport.cancel-on-movement", 5); }
    public boolean getUseSafeLocation() { return config.getBoolean("teleport.use-safelocation", true); }
    public List<?> getUnsafeBlocks() { return config.getList("teleport.unsafe-blocks", Arrays.asList(new String[] { "8","9","10","11","70","71","147","148" })); }
    public List<?> getBlockedWorlds() { return config.getList("teleport.blocked-world", Arrays.asList(new String[] { "8","9","10","11","70","71","147","148" })); }
 
    /* Metrics */
    public boolean getMetricsEnabled() { return config.getBoolean("metrics.enabled", true); }  
    
    /* GUI */
    public int getGuiRows() { return config.getInt("gui.rows", 9); }
    public String getGuiTitle() { return cc(config.getString("gui.title", "&2PlayerWarps")); }
    public String getDefaultIcon() { return config.getString("gui.default-playerwarp-icon", "35:9"); }
    public String getDefaultTitle() { return cc(config.getString("gui.default-playerwarp-title", "Go to &6[playername]s &fwarp")); }
    public boolean getUsePlayerHeads() { return config.getBoolean("gui.use-playerheads-as-icons", true); }  
    public String getNextpageIcon() { return config.getString("gui.nextpage-icon", "35:8"); }
    
    /* Settings */
    public int getSetWarpCost() { return config.getInt("settings.set-warp-cost", 400); }
    public int getMaxTitleSize() { return config.getInt("settings.max-title-text-size", 40); }
    public int getMaxLoreSize() { return config.getInt("settings.max-lore-text-size", 40); }
    
    /* GreifPrevention*/
    public boolean getGPEnabled() { return config.getBoolean("GriefPrevention.enabled", true); }  
    public boolean getGPOwnerCan() { return config.getBoolean("GriefPrevention.owner-can-set-warp", true); }  
    public boolean getGPTrustedCan() { return config.getBoolean("GriefPrevention.trusted-player-can-set-warp", true); }  
    public boolean getGPRemoveOnDelete() { return config.getBoolean("GriefPrevention.delete-warps-if-claim-is-deleted", true); } 

    /* RedProtect*/
    public boolean getRPEnabled() { return config.getBoolean("RedProtect.enabled", true); }  
    public boolean getRPLeaderCan() { return config.getBoolean("RedProtect.leader-player-can-set-warp", true); }  
    public boolean getRPAdminCan() { return config.getBoolean("RedProtect.admin-player-can-set-warp", true); }  
    public boolean getRPMemberCan() { return config.getBoolean("RedProtect.member-player-can-set-warp", true); }  
    public boolean getRPRemoveOnDelete() { return config.getBoolean("RedProtect.delete-warps-if-region-is-deleted", true); }  
    
    /* Residence */
    public boolean getRSEnabled() { return config.getBoolean("Residence.enabled", true); }  
    public boolean getRSOwnerCan() { return config.getBoolean("Residence.owner-player-can-set-warp", true); }  
    public boolean getRSBuilderCan() { return config.getBoolean("Residence.build-permission-player-can-set-warp", true); }  
    public boolean getRSemoveOnDelete() { return config.getBoolean("Residence.delete-warps-if-region-is-deleted", true); }  

    /* Factions */
    public boolean getFAEnabled() { return config.getBoolean("Factions.enabled", true); }  
    public boolean getFALeaderCan() { return config.getBoolean("Factions.leader-can-set-warp", true); }  
    public boolean getFAOwnTerritoryCan() { return config.getBoolean("Factions.in-own-territory-can-set-warp", true); }  
    public boolean getFAWildernessrCan() { return config.getBoolean("Factions.in-wilderness-can-set-warp", true); }  
    public boolean getFARemoveOnDelete() { return config.getBoolean("Factions.delete-warps-if-zone-is-deleted", true); }  

    /* WorldGuard */
    public boolean getWGEnabled() { return config.getBoolean("WorldGuard.enabled", true); }  
    public boolean getWGOwnerCan() { return config.getBoolean("WorldGuard.owner-can-set-warp", true); }  
    public boolean getWGMemberCan() { return config.getBoolean("WorldGuard.member-player-can-set-warp", true); }

    /* Essentials */
    public boolean getESEnabled() { return config.getBoolean("Essentials.enabled", true); }  
    public boolean getESBack() { return config.getBoolean("Essentials.use-back-command", true); }  
    

	/** Translate color codes from config data
	 *  TODO change to StringUtils calls
	 * */
	public String cc(String str) {
		return ChatColor.translateAlternateColorCodes('&',str);
	}
    
}