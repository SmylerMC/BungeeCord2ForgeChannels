package fr.thesmyler.bungee2forge.substitute;

import java.io.File;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyConfig;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ReconnectHandler;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.config.ConfigurationAdapter;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.api.scheduler.TaskScheduler;

@SuppressWarnings("deprecation")
public class FakeProxyServer extends ProxyServer {
	
	private Logger logger = Logger.getLogger("Mock proxy");
	private Map<UUID, ProxiedPlayer> playersByUUID = new HashMap<>();
	private Map<String, ProxiedPlayer> playersByName = new HashMap<>();
	private List<String> channels = new ArrayList<>();
	private Map<String, ServerInfo> servers = new HashMap<>();
	private PluginManager pluginManager = new PluginManager(this);
	
	@Override
	public String getName() {
		return "Fake test proxy";
	}

	@Override
	public String getVersion() {
		return "0";
	}

	@Override
	public String getTranslation(String name, Object... args) {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public Logger getLogger() {
		return this.logger;
	}

	@Override
	public Collection<ProxiedPlayer> getPlayers() {
		return this.playersByName.values();
	}

	@Override
	public ProxiedPlayer getPlayer(String name) {
		return this.playersByName.get(name);
	}

	@Override
	public ProxiedPlayer getPlayer(UUID uuid) {
		return this.playersByUUID.get(uuid);
	}

	@Override
	public Map<String, ServerInfo> getServers() {
		return this.servers;
	}

	@Override
	public ServerInfo getServerInfo(String name) {
		return this.servers.get(name);
	}

	@Override
	public PluginManager getPluginManager() {
		return this.pluginManager;
	}

	@Override
	public ConfigurationAdapter getConfigurationAdapter() {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public void setConfigurationAdapter(ConfigurationAdapter adapter) {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public ReconnectHandler getReconnectHandler() {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public void setReconnectHandler(ReconnectHandler handler) {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public void stop() {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public void stop(String reason) {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public void registerChannel(String channel) {
		this.channels.add(channel);
	}

	@Override
	public void unregisterChannel(String channel) {
		this.channels.remove(channel);
	}

	@Override
	public Collection<String> getChannels() {
		return this.channels;
	}

	@Override
	public String getGameVersion() {
		return "fake-test-server";
	}

	@Override
	public int getProtocolVersion() {
		return 0;
	}

	@Override
	public ServerInfo constructServerInfo(String name, InetSocketAddress address, String motd, boolean restricted) {
		return new FakeServerInfo(name, address, motd, restricted);
	}

	@Override
	public ServerInfo constructServerInfo(String name, SocketAddress address, String motd, boolean restricted) {
		return this.constructServerInfo(name, address, motd, restricted);
	}

	@Override
	public CommandSender getConsole() {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public File getPluginsFolder() {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public TaskScheduler getScheduler() {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public int getOnlineCount() {
		return this.playersByName.size();
	}

	@Override
	public void broadcast(String message) {
		this.logger.info("broadcast: " + message);
	}

	@Override
	public void broadcast(BaseComponent... message) {
		String m = "";
		for(BaseComponent me: message) m += me.toPlainText();
		this.broadcast(m);
	}

	@Override
	public void broadcast(BaseComponent message) {
		this.broadcast(message.toPlainText());
	}

	@Override
	public Collection<String> getDisabledCommands() {
		return Collections.emptyList();
	}

	@Override
	public ProxyConfig getConfig() {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public Collection<ProxiedPlayer> matchPlayer(String match) {
		List<ProxiedPlayer> l = new ArrayList<>();
		this.playersByName.values().forEach(p -> {
			if(p.getName().startsWith(match)) l.add(p);
		});
		return l;
	}

	@Override
	public Title createTitle() {
		throw new UnsupportedOperationException("Not implemented");
	}
	
	void addPlayer(ProxiedPlayer player) {
		this.playersByName.put(player.getName(), player);
		this.playersByUUID.put(player.getUniqueId(), player);
	}
	
	void removePlayer(ProxiedPlayer player) {
		this.playersByName.remove(player.getName());
		this.playersByUUID.remove(player.getUniqueId());
	}

}
