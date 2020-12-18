package org.Pulsar.PSBungee;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.Pulsar.PSBungee.Listeners.PluginMessageListener;
import org.Pulsar.PSBungee.Listeners.ServConnector;
import org.Pulsar.PSBungee.Utils.Celestials;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class Main extends Plugin {

	private File f;
	private Configuration config;
	private Celestials celestials;
	private HashMap<UUID, String[]> info = new HashMap<UUID, String[]>();
	private List<String> onlineServers = new ArrayList<String>();

	public void onEnable() {
		if (!getDataFolder().exists()) {
			getDataFolder().mkdir();
		}
		f = new File(getDataFolder(), "config.yml");
		saveDefaultConfig();
		getProxy().registerChannel("pulsar:system");
		new PluginMessageListener(this);
		celestials = new Celestials(this);
		new ServConnector(this);
		send("&aPlugin enabled!");
	}

	public void onDisable() {
		reloadConfig();
		send("&cPlugin disabled!");
	}

	public void send(String s) {
		getLogger().info(ChatColor.translateAlternateColorCodes('&', s));
	}

	private void saveDefaultConfig() {
		if (!f.exists()) {
			try {
				InputStream in = getResourceAsStream("config.yml");
				Files.copy(in, f.toPath());
			} catch (IOException e) {}
		}
		try {
			config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(f);
		} catch (IOException e) {}
	}

	private void reloadConfig() {
		try {
			config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(f);
		} catch (IOException e) {}
	}

	public void saveConfig() {
		try {
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, f);
		} catch (IOException e) {}
	}

	public Configuration getConfig() {
		return config;
	}

	public Celestials getCelestials() {
		return celestials;
	}

	public HashMap<UUID, String[]> getInfo() {
		return info;
	}

	public List<String> getOnlineServers() {
		return onlineServers;
	}

}
