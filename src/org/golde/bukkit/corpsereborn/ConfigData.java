package org.golde.bukkit.corpsereborn;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.golde.bukkit.corpsereborn.dump.ReportError;

public class ConfigData {

	private static int corpseTime;
	private static boolean onDeath;
	private static boolean lootingInventory;
	private static boolean showTags;
	private static String worldName;
	private static ArrayList<World> world;
	private static String guiName;
	private static String username;
	private static boolean autoDespawn;
	private static String finishLootingMessage;
	private static boolean newHitbox;
	private static boolean checkForUpdate;
	private static boolean shouldRenderArmor;
	private static boolean shouldSaveCorpses;
	private static boolean shouldSendDataToEric;

	public static boolean shouldCheckForUpdates(){return checkForUpdate;}
	public static int getCorpseTime() {return corpseTime;}
	public static boolean isOnDeath() {return onDeath;}
	public static boolean hasLootingInventory() {return lootingInventory;}
	public static boolean showTags() {return showTags;}
	public static ArrayList<World> getWorld(){	return world;}
	public static boolean getNewHitbox(){return newHitbox;}
	public static boolean shouldSaveCorpses(){return shouldSaveCorpses;}
	public static boolean shouldRenderArmor() {return shouldRenderArmor;}
	public static boolean shouldSendDataToEric(){return shouldSendDataToEric;}
	public static String getInventoryName(Player p){return guiName.replaceAll("%corpse%", p.getName()).replaceAll("&", "§");}
	
	@Deprecated
	public static String getUsername(Player p, String overrideUsername){
		return getUsername(p.getName(), overrideUsername);
	}
	
	public static String getUsername(String pUsername, String overrideUsername){
		if(overrideUsername == null){
			overrideUsername = username.replaceAll("%corpse%", pUsername).replaceAll("&", "§");
		}
		
		if(overrideUsername.length() > 16) {
			overrideUsername = overrideUsername.substring(0, 16);
		}
		return overrideUsername;
	}
	public static String finishLootingMessage(String name){
		if(finishLootingMessage.equalsIgnoreCase("none")){
			return null;
		}
		return finishLootingMessage.replaceAll("%corpse%", name).replaceAll("&", "§");
	}

	public static boolean shouldDespawnAfterLoot(){
		if(lootingInventory && autoDespawn){
			return true;
		}
		return false;
	}

	public static void checkConfigForMissingOptions()
	{
		FileConfiguration config = Main.getPlugin().getConfig();

		if (! config.isSet("enable-update-checker")) {
			Main.getPlugin().getLogger().info("did not find enable-update-checker");
			appendConfig("#Enable checking for new versions of the plugin?",
					"enable-update-checker: true");
		}

		if (! config.isSet("corpse-time")) {
			appendConfig("#Time in seconds until the corpse despawns. Set to -1 for the copse to never despawn.",
					"corpse-time: 120");
		}

		if (! config.isSet("on-death")) {
			appendConfig("#Spawn a corpse when the player dies?",
					"on-death: true");
		}

		if (! config.isSet("looting-inventory")) {
			appendConfig("#Put the players items into a GUI when they die? Click the the corpse to open the GUI.",
					"looting-inventory: true");
		}

		if (! config.isSet("despawn-after-looted")) {
			appendConfig("#Should the corpse automaticly despawn after it has been looted? (Only has effect is looting-inventory is true!)",
					"despawn-after-looted: true");
		}

		if (! config.isSet("show-tags")) {
			appendConfig("#Show the username of the player? If you set this to false ' ' will be used for the username.",
					"#The skin will still show even if you choose not do display the username.",
					"show-tags: true");
		}

		if (! config.isSet("world")) {
			appendConfig("#This is used to specify what world(s) corpses should spawn when somebody dies.",
					"#Set world to 'all' for every world.",
					"#Use '|' to add multiple worlds.",
					"world: all");
		}

		if (! config.isSet("gui-title")) {
			appendConfig("#Title of inventory created when you click the corpses head to loot it. ",
					"#%corpse% gets replaced with the corpses name.",
					"#Color codes and unicode characters work, but might be glitchy.",
					"#Minecraft has a 32 character limit on GUI lengths. If your title is more then that you will get a error on the console.",
					"gui-title: \"%corpse%'s Items\"");
		}

		if (! config.isSet("username-format")) {
			appendConfig("#This is the username of the corpse.",
					"#%corpse% gets replaced with the corpses name.",
					"#Color codes and unicode characters work, but might be glitchy.",
					"#Minecraft has a 16 character limit on name lengths. If your title is more then that you will get a error on the console.",
					"username-format: \"%corpse%\"");
		}

		if (! config.isSet("finish-looting-message")) {
			appendConfig("#This is the message sent when you finish looting the corpse. Set the message to \"none\" to disable.",
					"#%corpse% gets replaced with the corpses name.",
					"#Color codes and unicode characters work, but might be glitchy.",
					"finish-looting-message: \"&bYou have finished looting %corpse%'s corpse.\"");
		}

		if (! config.isSet("new-hitboxes")) {
			appendConfig("#Due to a Minecraft quirk with how the corpses work, the hitboxes were not normal player hitboxes.",
					"#I have now made it so a invisible Cows spawn that covers the whole corpse.",
					"#When you click on the invisible Cow it does the same thing as clicking on the corpse.",
					"#Set to false to use the old glitchy hitboxes.",
					"new-hitboxes: true");
		}

		if (! config.isSet("render-armour")) {
			appendConfig("#Should we render player armour and items on the corpses?",
					"#Set to false to not render player armour and items on the corpses",
					"render-armour: true");
		}
		
		if (! config.isSet("save-corpses")) {
			appendConfig("#Should we save corpses on restart?",
					"#Note: v1_8_R1 and below will not save. (1.8.8+ will save)",
					"save-corpses: true");
		}
		
		if (! config.isSet("send-data")) {
			appendConfig("#Should we send anonymous data to Eric?",
					"#The only data sent to my web server is what Minecraft version and what type of server you're running.",
					"#My goal with this data is to see which is the most popular version so I can improve this plugin as best as I can.",
					"#I totally understand if you do not want to send my server data, hence why this option is here.",
					"#The public static are located here if you wish to see them: http://web2.golde.org/files/spigot/CorpseReborn/stats/",
					"send-data: true");
		}

	}

	private static String newLine = "\r\n";

	private static void appendConfig(String... lines)
	{
		File dataDir = Main.getPlugin().getDataFolder();
		if (!dataDir.exists())
			dataDir.mkdirs();

		File file = new File(dataDir, "config.yml");

		try {
			FileWriter writer = new FileWriter(file, true);
			writer.append(newLine);
			for (String line: lines) {

				writer.append(line);
				writer.append(newLine);
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void load() {
		try {
			FileConfiguration config = Main.getPlugin().getConfig();
			corpseTime = config.getInt("corpse-time", 120);
			onDeath = config.getBoolean("on-death", true);
			lootingInventory = config.getBoolean("looting-inventory", true);
			showTags = config.getBoolean("show-tags", true);
			worldName = config.getString("world", "all");
			guiName = config.getString("gui-title", "%corpse%'s Items");
			username = config.getString("username-format", "%corpse%");
			autoDespawn = config.getBoolean("despawn-after-looted", true);
			finishLootingMessage = config.getString("finish-looting-message", "&bYou have finished looting %corpse%'s corpse.");
			newHitbox = config.getBoolean("new-hitboxes", true);
			checkForUpdate = config.getBoolean("enable-update-checker", true);
			shouldRenderArmor = config.getBoolean("render-armour", true);
			shouldSaveCorpses = config.getBoolean("save-corpses", true);
			shouldSendDataToEric = config.getBoolean("send-data", true);

			if(Main.serverVersion.compareTo(ServerVersion.v1_8) < 0 && newHitbox){
				Util.cinfo("&cNew hitboxes and finish-looting-message are disabled because your version ("+Main.serverVersion.name()+") does not support it. Please use 1.8+ for these things to work correctly");
				newHitbox = false;
			}
			if(worldName.equalsIgnoreCase("all")){
				world = null;
			}else{
				String[] worldNames = worldName.split("\\|");
				world = new ArrayList<World>();
				for(String name:worldNames){
					if(Bukkit.getWorld(name) != null){
						world.add(Bukkit.getWorld(name));
					}else{
						Util.severe("================================");
						Util.severe("Could not find the world: " + worldName);
						Util.severe("Please edit your config file.");
						Util.severe("================================");
					}
				}

			}
			Util.info("Config successfully loaded.");

		} catch (Exception e) {
			Util.severe("================================");
			Util.severe("Could not load config!");
			Util.severe("Is it configured properly?");
			Util.severe("Have you deleted old configs?");
			Util.severe("================================");
			new ReportError(e);
			Main.getPlugin().cont = false;
			Bukkit.getServer().getPluginManager().disablePlugin(Main.getPlugin());
		}
	}

}
