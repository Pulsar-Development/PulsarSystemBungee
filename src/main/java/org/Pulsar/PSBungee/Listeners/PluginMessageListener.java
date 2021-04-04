package org.Pulsar.PSBungee.Listeners;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.Pulsar.PSBungee.Main;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.logging.Logger;

public class PluginMessageListener implements Listener {

	private Main main;

	public PluginMessageListener(Main main) {
		this.main = main;
		main.getProxy().getPluginManager().registerListener(main, this);
	}


	@EventHandler
	public void onReceive(PluginMessageEvent event) {
		if (event.getTag().equalsIgnoreCase("pulsar:system")) {
			Server s = (Server) event.getSender();
			String name = s.getInfo().getName();
			ByteArrayDataInput in = ByteStreams.newDataInput(event.getData());
			String question = in.readUTF();
			switch (question) {
			case "connect":
				if (main.getConfig().getSection("space-servers").getKeys().contains(name)) {
					main.send("Space server " + name + " connected!");
					ByteArrayDataOutput out = ByteStreams.newDataOutput();
					out.writeUTF("space");
					out.writeUTF(main.getCelestials().getServerStar(name));
					out.writeUTF(main.getCelestials().getServerPlanetsString(name));
					out.writeUTF(main.getCelestials().getServerMoonsString(name));
					s.sendData("pulsar:system", out.toByteArray());
				} else {
					main.send("Non-Space server " + name + " connected!");
					ByteArrayDataOutput out = ByteStreams.newDataOutput();
					out.writeUTF("non-space");
					out.writeUTF(main.getCelestials().getServerWorlds(name));
					s.sendData("pulsar:system", out.toByteArray());
				}
				break;
			case "not-loaded":
				String type = in.readUTF();
				String celestial = in.readUTF();
				if (type.equalsIgnoreCase("planet")) {
					main.send("&cPlanet &f" + celestial + " &cis not loaded on server &f" + name + "&c!");
				} else if (type.equalsIgnoreCase("star")) {
					main.send("&cStar &f" + celestial + " &cis not loaded on server &f" + name + "&c!");
				} else {
					main.send("&cMoon &f" + celestial + " &cis not loaded on server &f" + name + "&c!");
				}
				break;
			case "unload":
				String cname = in.readUTF();
				String ctype = in.readUTF();
				if (ctype.equalsIgnoreCase("planet")) {
					main.send("&cPlanet &f" + cname + " &cis unloaded on server &f" + name + "&c!");
				} else if (ctype.equalsIgnoreCase("star")) {
					main.send("&cStar &f" + cname + " &cis unloaded on server &f" + name + "&c!");
				} else {
					main.send("&cMoon &f" + cname + " &cis unloaded on server &f" + name + "&c!");
				}
				break;
			case "sendto":
				String to = in.readUTF();
				String inv = in.readUTF();
				ProxiedPlayer p = (ProxiedPlayer) event.getReceiver();
				main.getInfo().put(p.getUniqueId(), new String[] { inv, in.readUTF(), to });
				p.connect(main.getProxy().getServers().get(main.getCelestials().getHostServer(to)));
				break;
			case "sendfrom":
				String from = in.readUTF();
				String invent = in.readUTF();
				ProxiedPlayer pp = (ProxiedPlayer) event.getReceiver();
				main.getInfo().put(pp.getUniqueId(), new String[] { invent, in.readUTF(), "space" });
				pp.connect(main.getProxy().getServers().get(main.getCelestials().getServer(from)));
				break;
			}
		} else {
			if (event.getTag().equalsIgnoreCase("minecraft:register")) {
				if (event.getSender() instanceof Server) {
					Server server = (Server) event.getSender();
					main.getOnlineServers().add(server.getInfo().getName());
					ByteArrayDataOutput out = ByteStreams.newDataOutput();
					out.writeUTF("online");
					out.writeUTF(server.getInfo().getName());
					main.getOnlineServers().add(server.getInfo().getName());
					for (String string : main.getOnlineServers()) {
						ByteArrayDataOutput o = ByteStreams.newDataOutput();
						o.writeUTF("online");
						o.writeUTF(main.getProxy().getServerInfo(string).getName());
						server.sendData("pulsar:system", o.toByteArray());
						main.getProxy().getServerInfo(string).sendData("pulsar:system", out.toByteArray());
					}
				}
			}
		}
	}

}
