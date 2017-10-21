package Handlers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.InvalidConfigurationException;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import PlayerWarpGUI.PlayerWarpGUI;

public class ConfigHandler {

	public static PlayerWarpGUI pl;

	private HashMap<String, String> comments;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ConfigHandler(PlayerWarpGUI playerWarpGUI) {
		pl = playerWarpGUI;
		this.comments = new HashMap();
	}
	
	public void msg() {
		if(pl.getConfig() != null) {
		pl.getMessageHandler().sendConsoleMessage(pl.getLanguageHandler().getMessage("CONSOLE_MSG_CONFIG_FILE", pl.getConfigName()));
		} else {
			pl.getCriticalErrors().add(pl.getLanguageHandler().getMessage("CONSOLE_ERROR_CRITIAL_FILENOTFOUND", pl.getConfigName()));
			pl.getMessageHandler().sendConsoleMessage(pl.getLanguageHandler().getMessage("CONSOLE_MSG_CONFIG_FILE", pl.getLanguageHandler().getMessage("FAILED")));	
		}
	}

	public void setUp() {
		File config = new File(pl.getPathConfig());
		if (!config.exists()) {
			config.getParentFile().mkdirs();
		}

		setupDefaults();
		saveConfig();
		setupDefaults();
	}

	public void setupDefaults() {
		File config = new File(pl.getPathConfig());
		if (config.exists()) {
			try {
				pl.getConfig().load(config);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		
		setDefaultValues("language", "en_US", "Available: en_US, en_AU");

		setDefaultValues("metrics", null, "Metrics");
		setDefaultValues("metrics.enabled", Boolean.valueOf(true), null);
		
		setDefaultValues("gui", Integer.valueOf(9), "GUI");
		setDefaultValues("gui.rows", Integer.valueOf(9), null);
		setDefaultValues("gui.title", "&2PlayerWarps", null);
		setDefaultValues("gui.default-playerwarp-icon", "35:9", null);
		setDefaultValues("gui.default-playerwarp-title", "Go to &6[playername]s &fwarp", null);
		setDefaultValues("gui.use-playerheads-as-icons", Boolean.valueOf(true), null);
		setDefaultValues("gui.nextpage-icon", "35:8", null);

		setDefaultValues("settings", null, null);
		setDefaultValues("settings.set-warp-cost", Integer.valueOf(400), null);
		setDefaultValues("settings.max-title-text-size", Integer.valueOf(40), null);
		setDefaultValues("settings.max-lore-text-size", Integer.valueOf(40), null);
		
		setDefaultValues("teleport", Boolean.valueOf(true), "Teleport");
		setDefaultValues("teleport.cancel-on-movement", Boolean.valueOf(true), null);
		setDefaultValues("teleport.movement-cooldown", Integer.valueOf(9), null);
		setDefaultValues("teleport.use-safelocation", Boolean.valueOf(true), null);
		setDefaultValues("teleport.unsafe-blocks", Arrays.asList(new String[] { "8","9","10","11","70","71","147","148" }), null);
		setDefaultValues("teleport.blocked-world", Arrays.asList(new String[] { "world_nether","world_end" }), null);
		
		setDefaultValues("GriefPrevention", null, "GreifProtection");
		setDefaultValues("GriefPrevention.enabled", Boolean.valueOf(false), null);
		setDefaultValues("GriefPrevention.owner-can-set-warp", Boolean.valueOf(true) , null);
		setDefaultValues("GriefPrevention.trusted-player-can-set-warp", Boolean.valueOf(true) , null);
		setDefaultValues("GriefPrevention.delete-warps-if-claim-is-deleted", Boolean.valueOf(true) , null);
		
		setDefaultValues("RedProtect", null, "RedProtect");
		setDefaultValues("RedProtect.enabled", Boolean.valueOf(false), null);
		setDefaultValues("RedProtect.leader-player-can-set-warp", Boolean.valueOf(true) , null);
		setDefaultValues("RedProtect.admin-player-can-set-warp", Boolean.valueOf(true) , null);
		setDefaultValues("RedProtect.member-player-can-set-warp", Boolean.valueOf(true) , null);
		setDefaultValues("RedProtect.delete-warps-if-region-is-deleted", Boolean.valueOf(true) , null);
		
		setDefaultValues("Residence", null, "RedProtect");
		setDefaultValues("Residence.enabled", Boolean.valueOf(false), null);
		setDefaultValues("Residence.owner-player-can-set-warp", Boolean.valueOf(true) , null);
		setDefaultValues("Residence.build-permission-player-can-set-warp", Boolean.valueOf(true) , null);
		setDefaultValues("Residence.delete-warps-if-region-is-deleted", Boolean.valueOf(true) , null);

		setDefaultValues("WorldGuard", null, "WorldGuard");
		setDefaultValues("WorldGuard.enabled", Boolean.valueOf(false), null);
		setDefaultValues("WorldGuard.owner-can-set-warp", Boolean.valueOf(true) , null);
		setDefaultValues("WorldGuard.member-player-can-set-warp", Boolean.valueOf(true) , null);
		setDefaultValues("Residence.delete-warps-if-region-is-deleted", Boolean.valueOf(true) , "Not working :(");

		setDefaultValues("Factions", null, "Factions");
		setDefaultValues("Factions.enabled", Boolean.valueOf(false), null);
		setDefaultValues("Factions.leader-can-set-warp", Boolean.valueOf(true) , null);
		setDefaultValues("Factions.in-own-territory-can-set-warp", Boolean.valueOf(true) , null);
		setDefaultValues("Factions.in-wilderness-can-set-warp", Boolean.valueOf(false) , null);
		setDefaultValues("Factions.delete-warps-if-zone-is-deleted", Boolean.valueOf(true) , null);
		
		setDefaultValues("config-version", Double.valueOf(1.1D), "Config.yml compatability version. \nDO NOT MODIFY");
		
		/*
		 * setDefault("flat-file", null, "If file-type: yml, configuration:");
		 * setDefault("flat-file.region-per-file", Boolean.valueOf(false),
		 * "Want to save the regions in your ow files?");
		 * setDefault("flat-file.auto-save-interval-seconds", Integer.valueOf(3600),
		 * null); setDefault("flat-file.backup", Boolean.valueOf(true), null);
		 * setDefault("flat-file.max-backups", Integer.valueOf(10), null);
		 * 
		 * setDefault("mysql", null, "If file-type: mysql, configuration:");
		 * setDefault("mysql.db-name", "redprotect", null);
		 * setDefault("mysql.table-prefix", "rp_", null); setDefault("mysql.user-name",
		 * "root", null); setDefault("mysql.user-pass", "redprotect", null);
		 * setDefault("mysql.host", "localhost", null);
		 * 
		 * setDefault("region-settings", null, "General settings about regions.");
		 * setDefault("region-settings.claim-type", "BLOCK",
		 * "claim-type: Claim types allowed for normal players (without permission 'redprotect.admin.claim'). Options: BLOCK, WAND or BOTH.\n-> If BLOCK, the players needs to surround your house with the block type in configuration, and place a sign under this fence with [rp] on first line.\n-> If WAND, the players will need a wand (default glass_bottle), click on two point of your region, and then use /rp claim [name of region] to claim te region.\n-> If BOTH, will allow both claim type protections."
		 * );
		 * 
		 * setDefault("region-settings.default-leader", "#server#",
		 * "The name of leader for regions created with /rp define or regions without leaders."
		 * ); setDefault("region-settings.world-colors", new ArrayList(),
		 * "Colors of world to show on /rp info and /rp list.");
		 * setDefault("region-settings.border.material", "GLOWSTONE",
		 * "Border block type when use /rp border.");
		 * setDefault("region-settings.border.time-showing", "BLOCK", null);
		 * setDefault("region-settings.region-list.simple-listing",
		 * Boolean.valueOf(true),
		 * "Show simple list with only name of region or all region info.");
		 * setDefault("region-settings.region-list.hover-and-click-teleport",
		 * Boolean.valueOf(true),
		 * "If running server 1.8+ enable hover and teleport click on simple list.");
		 * setDefault("region-settings.region-list.show-area", Boolean.valueOf(true),
		 * "Show region areas on list?");
		 * setDefault("region-settings.autoexpandvert-ondefine", Boolean.valueOf(true),
		 * "Automatically set max y to 256 and min y to 0 (sky to bedrock).");
		 * setDefault("region-settings.anti-hopper", Boolean.valueOf(true),
		 * "Deny break/place blocks under chests.");
		 * setDefault("region-settings.claim-modes.mode", "keep",
		 * "Default modes for claim regions. Modes available: keep, drop, remove or give.\n-> keep: Nothing happens\n-> drop: Will drop all protection blocks\n-> remove: Will remove all protection blocks\n-> give: Give back the protection blocks to player, and drop(on player location) if players's inventory is full."
		 * );
		 * 
		 * setDefault("region-settings.claim-modes.allow-player-decide",
		 * Boolean.valueOf(false),
		 * "Allow players to decide what mode to use? If true, the player need to set the line 4 of the sign with [keep], [drop], [remove], [give] or a translation you is using on 'lang.ini'."
		 * ); setDefault("region-settings.claim-modes.use-perm", Boolean.valueOf(false),
		 * "If 'allow-player-decide' is true, player need to have the permission 'redprotect.use-claim-modes' to use modes on signs."
		 * ); setDefault("region-settings.limit-amount", Integer.valueOf(8000),
		 * "Limit of blocks until the player have other block permission.");
		 * setDefault("region-settings.claim-amount", Integer.valueOf(20),
		 * "Limit of claims a player can have until have other permission for claims.");
		 * setDefault("region-settings.block-id", "FENCE",
		 * "Block used to protect regions."); setDefault("region-settings.max-scan",
		 * Integer.valueOf(600),
		 * "Ammount of blocks to scan on place sign to claim a region. Consider this the max area."
		 * ); setDefault("region-settings.date-format", "dd/MM/yyyy",
		 * "Time format to use with data and time infos.");
		 * setDefault("region-settings.record-player-visit-method", "ON-LOGIN",
		 * "Register player visits on... Available: ON-LOGIN, ON-REGION-ENTER.");
		 * setDefault("region-settings.allow-sign-interact-tags", Arrays.asList(new
		 * String[] { "Admin Shop", "{membername}" }),
		 * "Allow players without permissions to interact with signs starting with this tags."
		 * ); setDefault("region-settings.leadership-request-time", Integer.valueOf(20),
		 * "Time in seconds to wait player accept leadership request.");
		 * setDefault("region-settings.enable-flag-sign", Boolean.valueOf(true),
		 * "This wiil allow players to create flag signs to change flag states using [flag] on first line and the flag name on second line."
		 * ); setDefault("region-settings.deny-build-near", Integer.valueOf(0),
		 * "Deny players to build near other regions. Distance in blocks. 0 to disable and > 0 to enable."
		 * ); setDefault("region-settings.rent.default-level", "admin",
		 * "Set the default rent level of players renting regions. Options: member, admin, leader."
		 * ); setDefault("region-settings.rent.add-player", Boolean.valueOf(false),
		 * "Allow who pay for rent to add more players in rent regions?");
		 * setDefault("region-settings.rent.command-renew-adds", "1:MONTH",
		 * "The amount of days or mounts the command to renew will be added. The sintax is: <number>:<type>. The types are: DAY or MONTH."
		 * ); setDefault("region-settings.rent.renew-anytime", Boolean.valueOf(false),
		 * "Renew in anytime or only on renew date?");
		 * setDefault("region-settings.first-home.can-delete-after-claims",
		 * Integer.valueOf(10),
		 * "Player can remove the protection of first home after this amount of claims. Use -1 to do not allow to delete."
		 * ); setDefault("region-settings.delay-after-kick-region", Integer.valueOf(60),
		 * "Delay before a kicked player can back to a region (in seconds).");
		 * setDefault("region-settings.claimlimit-per-world", Boolean.valueOf(true),
		 * "Use claim limit per worlds?");
		 * setDefault("region-settings.blocklimit-per-world", Boolean.valueOf(true),
		 * "Use block limit per worlds?");
		 * 
		 * setDefault("allowed-claim-worlds", Arrays.asList(new String[] {
		 * "example_world" }), "World where players can claim regions.");
		 * 
		 * setDefault("needed-claim-to-build", null,
		 * "Worlds where players can't build without claim.");
		 * setDefault("needed-claim-to-build.worlds", Arrays.asList(new String[] {
		 * "example_world" }), null);
		 * setDefault("needed-claim-to-build.allow-only-protections-blocks",
		 * Boolean.valueOf(true),
		 * "Allow player to place only protection blocks, like fences and sign.");
		 * 
		 * setDefault("wands", null, "Wands configurations");
		 * setDefault("wands.adminWandID", Integer.valueOf(374),
		 * "Item used to define and redefine regions."); setDefault("wands.infoWandID",
		 * Integer.valueOf(339), "Item used to check regions.");
		 * 
		 * setDefault("private", null, "Private options"); setDefault("private.use",
		 * Boolean.valueOf(true), "Enable private signs?");
		 * setDefault("private.allow-outside", Boolean.valueOf(false),
		 * "Allow private signs outside regions");
		 * setDefault("private.allowed-blocks-use-ids", Boolean.valueOf(false),
		 * "Use number IDs instead item names?"); setDefault("private.allowed-blocks",
		 * Arrays.asList(new String[] { "DISPENSER", "NOTE_BLOCK", "BED_BLOCK", "CHEST",
		 * "WORKBENCH", "FURNACE", "JUKEBOX", "ENCHANTMENT_TABLE", "BREWING_STAND",
		 * "CAULDRON", "ENDER_CHEST", "BEACON", "TRAPPED_CHEST", "HOPPER", "DROPPER" }),
		 * "Blocks allowed to be locked with private signs.");
		 * 
		 * setDefault("notify", null, "Notifications configs");
		 * setDefault("notify.region-exit", Boolean.valueOf(true),
		 * "Show region info(or wilderness message) when exit a region.");
		 * setDefault("notify.region-enter-mode", "BOSSBAR",
		 * "How to show the messages? Available: BOSSBAR, CHAT. If plugin BoobarApi not installed, will show on chat."
		 * ); setDefault("notify.welcome-mode", "BOSSBAR",
		 * "Where to show the welcome message (/rp wel <message>)? Available: BOSSBAR, CHAT."
		 * );
		 * 
		 * setDefault("netherProtection", null, "Deny players to go to nether roof.");
		 * setDefault("netherProtection.maxYsize", Integer.valueOf(128),
		 * "Max size of your world nether."); setDefault("netherProtection.execute-cmd",
		 * Arrays.asList(new String[] { "spawn {player}" }),
		 * "Execute this if player go up to maxYsize of nether.");
		 * 
		 * setDefault("server-protection", null, "General server protections options.");
		 * setDefault("server-protection.deny-potions", Arrays.asList(new String[] {
		 * "INVISIBILITY" }),
		 * "List of potions the player cant use on server. Here the PotioTypes: https://hub.spigotmc.org/javadocs/spigot/org/bukkit/potion/PotionType.html"
		 * ); setDefault("server-protection.deny-playerdeath-by", Arrays.asList(new
		 * String[] { "SUFFOCATION" }),
		 * " List of causes the player cant die/take damage for. here the list of DamageCauses: https://hub.spigotmc.org/javadocs/spigot/org/bukkit/event/entity/EntityDamageEvent.DamageCause.html"
		 * ); setDefault("server-protection.deny-commands-on-worlds.world",
		 * Arrays.asList(new String[] { "command" }),
		 * "Deny certain commands on specific worlds.");
		 * setDefault("server-protection.nickname-cap-filter.enable",
		 * Boolean.valueOf(false),
		 * "Deny players with same nick but cap char diferences to join on server (most used on offline severs)."
		 * ); setDefault("server-protection.sign-spy.enable", Boolean.valueOf(false),
		 * "Show all lines of a sign when player place signs in any world.");
		 * setDefault("server-protection.sign-spy.only-console", Boolean.valueOf(true),
		 * "Show only on console or in-game too?");
		 * setDefault("server-protection.teleport-player.on-join.enable",
		 * Boolean.valueOf(false), "Teleport player on join the server."); setDefault(
		 * "server-protection.teleport-player.on-join.need-world-to-teleport", "none",
		 * "The player need to be in this world to be teleported? Use 'none' for all worlds."
		 * ); setDefault("server-protection.teleport-player.on-join.location",
		 * "world, 0, 90, 0", "The location, using as world, x, y, z.");
		 * setDefault("server-protection.teleport-player.on-leave.enable",
		 * Boolean.valueOf(false), "Teleport player on leave the server."); setDefault(
		 * "server-protection.teleport-player.on-leave.need-world-to-teleport", "none",
		 * "The player need to be in this world to be teleported? Use 'none' for all worlds."
		 * ); setDefault("server-protection.teleport-player.on-leave.location",
		 * "world, 0, 90, 0", "The location, using as world, x, y, z.");
		 * setDefault("server-protection.deny-structure-bypass-regions",
		 * Boolean.valueOf(true),
		 * "Deny structures like trees to bypass region borders?");
		 * setDefault("server-protection.check-killaura-freekill.enable",
		 * Boolean.valueOf(false), "Enable kill aura or freekill checker?");
		 * setDefault("server-protection.check-killaura-freekill.check-rate",
		 * Integer.valueOf(30),
		 * "This will count every block the player wall without fail to aim on player."
		 * ); setDefault("server-protection.check-killaura-freekill.rate-multiples",
		 * Integer.valueOf(5), "What multiples of check-rate is considered kh or fk?");
		 * setDefault("server-protection.check-killaura-freekill.time-between-trys",
		 * Integer.valueOf(3), "Time to reset checks between attacker hits.");
		 * setDefault("server-protection.check-killaura-freekill.debug-trys",
		 * Boolean.valueOf(false),
		 * "Debug everu try? Used to see the try count on every block the player walk. Will be sequential if the player is using kill aura and will go to more than 60, 80 more than 100 if is free kill."
		 * ); setDefault("server-protection.check-player-client",
		 * Boolean.valueOf(false), "Test client hack (beta)");
		 * 
		 * setDefault("flags", null, "Default flag values for new regions.");
		 * setDefault("flags.pvp", Boolean.valueOf(false), null);
		 * setDefault("flags.chest", Boolean.valueOf(false), null);
		 * setDefault("flags.lever", Boolean.valueOf(false), null);
		 * setDefault("flags.button", Boolean.valueOf(false), null);
		 * setDefault("flags.door", Boolean.valueOf(false), null);
		 * setDefault("flags.smart-door", Boolean.valueOf(true), null);
		 * setDefault("flags.spawn-monsters", Boolean.valueOf(true), null);
		 * setDefault("flags.spawn-animals", Boolean.valueOf(true), null);
		 * setDefault("flags.passives", Boolean.valueOf(false), null);
		 * setDefault("flags.flow", Boolean.valueOf(true), null);
		 * setDefault("flags.fire", Boolean.valueOf(true), null);
		 * setDefault("flags.minecart", Boolean.valueOf(false), null);
		 * setDefault("flags.allow-home", Boolean.valueOf(false), null);
		 * setDefault("flags.allow-magiccarpet", Boolean.valueOf(true), null);
		 * setDefault("flags.mob-loot", Boolean.valueOf(false), null);
		 * setDefault("flags.flow-damage", Boolean.valueOf(false), null);
		 * setDefault("flags.iceform-player", Boolean.valueOf(true), null);
		 * setDefault("flags.iceform-world", Boolean.valueOf(true), null);
		 * setDefault("flags.allow-fly", Boolean.valueOf(false), null);
		 * setDefault("flags.teleport", Boolean.valueOf(false), null);
		 * setDefault("flags.clan", "", null); setDefault("flags.ender-chest",
		 * Boolean.valueOf(true), null); setDefault("flags.can-grow",
		 * Boolean.valueOf(true), null); setDefault("flags.use-potions",
		 * Boolean.valueOf(true), null); setDefault("flags.allow-effects",
		 * Boolean.valueOf(true), null); setDefault("flags.allow-spawner",
		 * Boolean.valueOf(false), null); setDefault("flags.leaves-decay",
		 * Boolean.valueOf(false), null); setDefault("flags.build",
		 * Boolean.valueOf(false), null);
		 * 
		 * setDefault("flags-configuration", null,
		 * "effects-duration: Duration for timed flags like potions effects, jump, etc.\nenabled-flags: Flags enabled to players use with commands and flag Gui.\npvparena-nopvp-kick-cmd: Command to use if players with pvp off enter in a region with 'pvparena' enabled.\nchange-flag-delay: Delay the player can change a flag after last change.\nflags: List of flags the player will need to wait to change."
		 * );
		 * 
		 * setDefault("flags-configuration.effects-duration", Integer.valueOf(5),
		 * "Duration for timed flags like potions effects, jump, etc.");
		 * setDefault("flags-configuration.enabled-flags", Arrays.asList(new String[] {
		 * "pvp", "chest", "lever", "button", "door", "smart-door", "spawn-monsters",
		 * "spawn-animals", "passives", "flow", "fire", "minecart", "allow-potions",
		 * "allow-home", "allow-magiccarpet", "mob-loot", "flow-damage",
		 * "iceform-player", "iceform-world", "allow-fly", "teleport", "clan",
		 * "ender-chest", "leaves-decay", "build" }),
		 * "Flags enabled to players use with commands and flag Gui.");
		 * 
		 * setDefault("flags-configuration.pvparena-nopvp-kick-cmd", "spawn {player}",
		 * "Command to use if players with pvp off enter in a region with 'pvparena' enabled."
		 * ); setDefault("flags-configuration.change-flag-delay.enable",
		 * Boolean.valueOf(true), "Enable delay to change flags.");
		 * setDefault("flags-configuration.change-flag-delay.seconds",
		 * Integer.valueOf(10),
		 * "Delay the player can change a flag after last change.");
		 * setDefault("flags-configuration.change-flag-delay.flags", Arrays.asList(new
		 * String[] { "pvp" }),
		 * "List of flags the player will need to wait to change.");
		 * 
		 * setDefault("purge", null, null); setDefault("purge.enabled",
		 * Boolean.valueOf(false), null); setDefault("purge.remove-oldest",
		 * Integer.valueOf(90), null); setDefault("purge.regen.enable",
		 * Boolean.valueOf(false),
		 * "Hook with WorldEdit, will regen only the region areas to bedrock to sky. Theres no undo for this action!"
		 * ); setDefault("purge.regen.max-area-regen", Integer.valueOf(500),
		 * "Max area size to automatic regen the region.");
		 * setDefault("purge.regen.awe-logs", Boolean.valueOf(false),
		 * "Show regen logs if using AsyncWorldEdit.");
		 * setDefault("purge.ignore-regions-from-players", new ArrayList(), null);
		 * 
		 * setDefault("sell", null, null); setDefault("sell.enabled",
		 * Boolean.valueOf(false),
		 * "Put regions to sell after x time the player dont came online.");
		 * setDefault("sell.sell-oldest", Integer.valueOf(90), null);
		 * setDefault("sell.ignore-regions-from-players", new ArrayList(), null);
		 * 
		 * setDefault("performance", null, null);
		 * setDefault("performance.disable-onPlayerMoveEvent-handler",
		 * Boolean.valueOf(false),
		 * "Disable player move event to improve performance? Note: Disabling this will make some flags do not work, like deny enter, execute commands and effects."
		 * ); setDefault("performance.piston.disable-PistonEvent-handler",
		 * Boolean.valueOf(false),
		 * "Disable piston listener? Disabling this will allow players to get blocks from protected regions to unprotected using pistons."
		 * ); setDefault("performance.piston.use-piston-restricter",
		 * Boolean.valueOf(false), null);
		 * setDefault("performance.piston.restrict-piston-event", Integer.valueOf(10),
		 * "Fire the piston extract/retract every x ticks. Server default is 1 tick/event. Value in ticks (20 ticks = 1 sec)"
		 * );
		 * 
		 * setDefault("schematics", null,
		 * "This is the schematics configs for RedProtect.\n");
		 * setDefault("schematics.first-house-file", "house1.schematic",
		 * "Schematic file name to use with /rp start.");
		 * 
		 * setDefault("hooks", null, null); setDefault("hooks.check-uuid-names-onstart",
		 * Boolean.valueOf(false),
		 * "Convert/check names if need to update to/from UUID/names on server start? Disable for Bungeecoord."
		 * ); setDefault("hooks.essentials.import-lastvisits", Boolean.valueOf(false),
		 * "Import last visits from Essentials to RedProtect Regions.");
		 * setDefault("hooks.dynmap.enable", Boolean.valueOf(true),
		 * "Enable hook to show all regions on dynmap plugin?");
		 * setDefault("hooks.dynmap.hide-by-default", Boolean.valueOf(true),
		 * "Hide the Redprotect tab group by default?");
		 * setDefault("hooks.dynmap.marks-groupname", "RedProtect",
		 * "Group name to show on hide/show tab map.");
		 * setDefault("hooks.dynmap.layer-priority", Integer.valueOf(10),
		 * "If you use another region mark plugin.");
		 * setDefault("hooks.dynmap.show-label", Boolean.valueOf(true),
		 * "Show names under regions."); setDefault("hooks.dynmap.show-icon",
		 * Boolean.valueOf(true), "Show icons under regions.");
		 * setDefault("hooks.dynmap.marker-icon", "shield",
		 * "Icon name to show under regions. All icons are available here: http://i.imgur.com/f61GPoE.png"
		 * ); setDefault("hooks.dynmap.show-leaders-admins", Boolean.valueOf(false),
		 * "Show leaders and admins on hover?");
		 * setDefault("hooks.dynmap.cuboid-region.enable", Boolean.valueOf(true),
		 * "Cuboid region config.");
		 * setDefault("hooks.dynmap.cuboid-region.if-disable-set-center",
		 * Integer.valueOf(60), null); setDefault("hooks.dynmap.min-zoom",
		 * Integer.valueOf(0), null);
		 * setDefault("hooks.magiccarpet.fix-piston-getblocks", Boolean.valueOf(true),
		 * "Fix pistons allow get mc blocks.");
		 * setDefault("hooks.armor-stands.spawn-arms", Boolean.valueOf(true), null);
		 * setDefault("hooks.mcmmo.fix-acrobatics-fireoff-leveling",
		 * Boolean.valueOf(true),
		 * "Fix players leveling with creeper explosions on flag fire disabled.");
		 * setDefault("hooks.mcmmo.fix-berserk-invisibility", Boolean.valueOf(true),
		 * "Fix the ability berserk making players and mobs invisible around who activated the ability."
		 * ); setDefault("hooks.worldedit.use-for-schematics", Boolean.valueOf(true),
		 * "Use WorldEdit to paste newbie home-schematics (/rp start)? *RedProtect already can paste schematics without WorldEdit, but dont support NBT tags like chest contents and sign messages."
		 * ); setDefault("hooks.asyncworldedit.use-for-regen", Boolean.valueOf(false),
		 * "Use AsyncWorldEdit for regen regions? (need WorldEdit).");
		 * setDefault("hooks.factions.claim-over-rps", Boolean.valueOf(false),
		 * "Allow players claim Factions chunks under RedProtect regions?");
		 * setDefault("hooks.simpleclans.use-war", Boolean.valueOf(false),
		 * "Enable Clan Wars from SimleClans.");
		 * setDefault("hooks.simpleclans.war-on-server-regions", Boolean.valueOf(false),
		 * "Allow war clans to pvp on #server# regions?");
		 */

	}

	private void setDefaultValues(String key, Object def, String comment) {
		if (def != null) {
			pl.getConfig().set(key, pl.getConfig().get(key, def));
		}
		if (comment != null) {
			setComment(key, comment);
		}
	}

	private void setComment(String key, String comment) {
		this.comments.put(key, comment);
	}

	public void saveConfig() {
		StringBuilder sb = new StringBuilder();
		pl.getConfig().options().header(null);

		sb.append("# +--------------------------------------------------------------------+ #\n"
				+ "# <                 PlayerWarpGUI configuration File                   > #\n"
				+ "# <--------------------------------------------------------------------> #\n"
				+ "# <       This is the configuration file, feel free to edit it.        > #\n"
				+ "# <        For more info about cmds and flags, check our Wiki:         > #\n"
				+ "# <         https://github.com/FabioZumbi12/RedProtect/wiki            > #\n"
				+ "# +--------------------------------------------------------------------+ #\n" + "#\n");

		for (String configLine : pl.getConfig().getKeys(true)) {
			String[] configKey = configLine.split("\\" + pl.getConfig().options().pathSeparator());
			String spaces = new String();
			for (int i = 0; i < configKey.length; i++) {
				if (i != 0) {
					spaces = spaces + " ";
				}
			}
			if (this.comments.containsKey(configLine)) {
				if (spaces.isEmpty()) {
					sb.append("\n# " + ((String) this.comments.get(configLine)).replace("\n", "\n# ")).append('\n');
				} else {
					sb.append(spaces + "# "
							+ ((String) this.comments.get(configLine)).replace("\n",
									new StringBuilder().append("\n").append(spaces).append("# ").toString()))
							.append('\n');
				}
			}
			Object configValue = pl.getConfig().get(configLine);
			if (!pl.getConfig().isConfigurationSection(configLine)) {
				if ((configValue instanceof String)) {
					sb.append(spaces + configKey[(configKey.length - 1)] + ": '" + configValue + "'\n");
				} else if ((configValue instanceof List)) {
					sb.append(spaces + configKey[(configKey.length - 1)] + ":\n");
					for (Object lineCfg : (List<?>) configValue) {
						if ((lineCfg instanceof String)) {
							sb.append(spaces + "- '" + lineCfg + "'\n");
						} else {
							sb.append(spaces + "- " + lineCfg + "\n");
						}
					}
				} else {
					sb.append(spaces + configKey[(configKey.length - 1)] + ": " + configValue + "\n");
				}
			} else {
				sb.append(spaces + configKey[(configKey.length - 1)] + ":\n");
			}
		}
		try {
			Files.write(sb, new File(pl.getPathMain(), "config.yml"), Charsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
