package org.Pulsar.PSBungee.Utils;

import java.util.ArrayList;
import java.util.List;
import org.Pulsar.PSBungee.Main;

public class Celestials {

	private Main main;

	public Celestials(Main main) {
		this.main = main;
	}

	public List<String> getServerCelestials(String name) {
		List<String> celestials = new ArrayList<String>();
		celestials.add(main.getConfig().getSection("space-servers." + name).getString("star"));
		for (String planet : main.getConfig().getSection("space-servers." + name).getSection("planets").getKeys()) {
			celestials.add(planet);
			if (main.getConfig().getSection("space-servers." + name + ".planets." + planet).contains("moons")) {
				for (String moon : main.getConfig()
						.getSection("space-servers." + name + ".planets." + planet + ".moons").getKeys()) {
					celestials.add(moon);
				}
			}
		}
		return celestials;
	}

	public List<String> getServerPlanets(String server) {
		List<String> planets = new ArrayList<String>();
		for (String planet : main.getConfig().getSection("space-servers." + server).getSection("planets").getKeys()) {
			planets.add(planet);
		}
		return planets;
	}

	public String getServerPlanetsString(String server) {
		String s = "";
		for (String planet : getServerPlanets(server)) {
			if (main.getProxy().getServers().containsKey(getHostServer(planet))) {
				if (main.getOnlineServers().contains(getHostServer(planet))) {
					s += ";" + planet + "_" + getNumber(server, planet, true) + "_true_" + getHostServer(planet);
				} else {
					s += ";" + planet + "_" + getNumber(server, planet, true) + "_false_" + getHostServer(planet);
				}
			} else {
				s += ";" + planet + "_" + getNumber(server, planet, true) + "_false_" + getHostServer(planet);
			}
		}
		return s.replaceFirst(";", "");
	}

	private byte getNumber(String server, String celestial, boolean isPlanet) {
		if (isPlanet) {
			return main.getConfig().getByte("space-servers." + server + ".planets." + celestial + ".number");
		} else {
			return main.getConfig().getByte("space-servers." + server + ".planets." + celestial.split(",")[0]
					+ ".moons." + celestial.split(",")[1] + ".number");
		}
	}

	public List<String> getServerMoons(String server, boolean withPlanet) {
		List<String> moons = new ArrayList<String>();
		for (String planet : main.getConfig().getSection("space-servers." + server).getSection("planets").getKeys()) {
			if (main.getConfig().getSection("space-servers." + server + ".planets." + planet).contains("moons")) {
				for (String moon : main.getConfig()
						.getSection("space-servers." + server + ".planets." + planet + ".moons").getKeys()) {
					moons.add(withPlanet ? planet + "," + moon : moon);
				}
			}
		}
		return moons;
	}

	public String getServerStar(String server) {
		String star = "";
		if (main.getConfig().contains("space-servers." + server + ".star")) {
			star = main.getConfig().getString("space-servers." + server + ".star");
		}
		return star;
	}

	public String getServerMoonsString(String server) {
		String s = "";
		for (String moon : getServerMoons(server, true)) {
			if (main.getProxy().getServers().containsKey(getHostServer(moon))) {
				if (main.getOnlineServers().contains(getHostServer(moon))) {
					s += ";" + moon + "_" + getNumber(server, moon, false) + "_true_" + getHostServer(moon);
				} else {
					s += ";" + moon + "_" + getNumber(server, moon, false) + "_false_" + getHostServer(moon);
				}
			} else {
				s += ";" + moon + "_" + getNumber(server, moon, false) + "_false_" + getHostServer(moon);
			}
		}
		return s.replaceFirst(";", "");
	}

	public String getServer(String from) {
		for (String server : main.getConfig().getSection("space-servers").getKeys()) {
			for (String planet : main.getConfig().getSection("space-servers." + server + ".planets").getKeys()) {
				if (planet.equalsIgnoreCase(from)) {
					return server;
				}
				if (main.getConfig().getSection("space-servers." + server + ".planets." + planet).contains("moons")) {
					for (String moon : main.getConfig()
							.getSection("space-servers." + server + ".planets." + planet + ".moons").getKeys()) {
						if (moon.equalsIgnoreCase(from.split(",")[1])) {
							return server;
						}
					}
				}
			}
		}
		return "";
	}

	public String getHostServer(String from) {
		for (String server : main.getConfig().getSection("space-servers").getKeys()) {
			for (String planet : main.getConfig().getSection("space-servers." + server + ".planets").getKeys()) {
				if (planet.equalsIgnoreCase(from)) {
					return main.getConfig().getString("space-servers." + server + ".planets." + planet + ".server");
				}
				if (main.getConfig().getSection("space-servers." + server + ".planets." + planet).contains("moons")) {
					for (String moon : main.getConfig()
							.getSection("space-servers." + server + ".planets." + planet + ".moons").getKeys()) {
						if (from.split(",").length > 1) {
							if (moon.equalsIgnoreCase(from.split(",")[1])) {
								return main.getConfig().getString("space-servers." + server + ".planets." + planet
										+ ".moons." + moon + ".server");
							}
						} else {
							if (moon.equalsIgnoreCase(from)) {
								return main.getConfig().getString("space-servers." + server + ".planets." + planet
										+ ".moons." + moon + ".server");
							}
						}
					}
				}
			}
		}
		return "";
	}

	public String getServerWorlds(String name) {
		String s = "";
		for (String server : main.getConfig().getSection("space-servers").getKeys()) {
			for (String planet : main.getConfig().getSection("space-servers." + server + ".planets").getKeys()) {
				if (name.equalsIgnoreCase(
						main.getConfig().getString("space-servers." + server + ".planets." + planet + ".server"))) {
					s += ";" + planet;
				}
				if (main.getConfig().getSection("space-servers." + server + ".planets." + planet).contains("moons")) {
					for (String moon : main.getConfig()
							.getSection("space-servers." + server + ".planets." + planet + ".moons").getKeys()) {
						if (name.equalsIgnoreCase(main.getConfig().getString(
								"space-servers." + server + ".planets." + planet + ".moons." + moon + ".server"))) {
							s += ";" + moon;
						}
					}
				}
			}
		}
		return s.replaceFirst(";", "");
	}

}
