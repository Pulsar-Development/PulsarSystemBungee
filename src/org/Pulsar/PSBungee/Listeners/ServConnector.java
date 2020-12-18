package org.Pulsar.PSBungee.Listeners;

import java.util.concurrent.TimeUnit;

import org.Pulsar.PSBungee.Main;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ServConnector implements Listener {

	private Main main;

	public ServConnector(Main main) {
		this.main = main;
		main.getProxy().getPluginManager().registerListener(main, this);
	}

	@EventHandler
	public void onConnect(ServerConnectedEvent e) {
		ProxiedPlayer p = e.getPlayer();
		if (main.getInfo().containsKey(p.getUniqueId())) {
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("playerinfo");
			out.writeUTF(main.getInfo().get(p.getUniqueId())[0]);
			out.writeUTF(main.getInfo().get(p.getUniqueId())[1]);
			out.writeUTF(main.getInfo().get(p.getUniqueId())[2]);
			Runnable r = new Runnable() {
				@Override
				public void run() {
					if (main.getOnlineServers().contains(e.getServer().getInfo().getName())) {
						e.getServer().sendData("pulsar:system", out.toByteArray());
					}
				}
			};
			main.getProxy().getScheduler().schedule(main, r, 10l, TimeUnit.SECONDS);
		}
	}

}
