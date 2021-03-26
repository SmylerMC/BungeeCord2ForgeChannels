package fr.thesmyler.bungee2forge.substitute;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerConnectRequest;
import net.md_5.bungee.api.SkinConfiguration;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.ServerConnectEvent.Reason;
import net.md_5.bungee.api.score.Scoreboard;

public class FakeProxiedPlayer implements ProxiedPlayer {
	
	private String name, displayName;
	private InetSocketAddress address = InetSocketAddress.createUnresolved("localhost", 25566);
	private List<String> groups = new ArrayList<>();
	private Server server;
	private UUID uuid;
	
	public FakeProxiedPlayer(UUID uuid, String name, Server server) {
		this.name = this.displayName = name;
		this.server = server;
		this.uuid = uuid;
	}

	@Override
	public InetSocketAddress getAddress() {
		return this.address;
	}

	@Override
	public SocketAddress getSocketAddress() {
		return this.address;
	}

	@Override
	public void disconnect(String reason) {
		ProxyServer.getInstance().getLogger().info(this.name + " disconnect: " + reason);
	}

	@Override
	public void disconnect(BaseComponent... reason) {
		String r = "";
		for(BaseComponent re: reason) r += re.toPlainText();
		this.disconnect(r);
	}

	@Override
	public void disconnect(BaseComponent reason) {
		this.disconnect(reason.toPlainText());
	}

	@Override
	public boolean isConnected() {
		return true;
	}

	@Override
	public Unsafe unsafe() {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void sendMessage(String message) {
		ProxyServer.getInstance().getLogger().info("message to " + this.name + ": " + message);
	}

	@Override
	public void sendMessages(String... messages) {
		this.sendMessage(String.join("", messages));
	}

	@Override
	public void sendMessage(BaseComponent... message) {
		String m = "";
		for(BaseComponent re: message) m += re.toPlainText();
		this.sendMessage(m);
	}

	@Override
	public void sendMessage(BaseComponent message) {
		this.sendMessage(message.toPlainText());
	}

	@Override
	public Collection<String> getGroups() {
		return this.groups;
	}

	@Override
	public void addGroups(String... groups) {
		for(String group: groups) this.groups.add(group);
	}

	@Override
	public void removeGroups(String... groups) {
		for(String group: groups) this.groups.remove(group);		
	}

	@Override
	public boolean hasPermission(String permission) {
		return true;
	}

	@Override
	public void setPermission(String permission, boolean value) {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public Collection<String> getPermissions() {
		return Collections.emptyList();
	}

	@Override
	public String getDisplayName() {
		return this.displayName;
	}

	@Override
	public void setDisplayName(String name) {
		this.displayName = name;
	}

	@Override
	public void sendMessage(ChatMessageType position, BaseComponent... message) {
		this.sendMessage(message);
	}

	@Override
	public void sendMessage(ChatMessageType position, BaseComponent message) {
		this.sendMessage(message);
	}

	@Override
	public void sendMessage(UUID sender, BaseComponent... message) {
		this.sendMessage(message);
	}

	@Override
	public void sendMessage(UUID sender, BaseComponent message) {
		this.sendMessage(message);
	}

	@Override
	public void connect(ServerInfo target) {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public void connect(ServerInfo target, Reason reason) {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public void connect(ServerInfo target, Callback<Boolean> callback) {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public void connect(ServerInfo target, Callback<Boolean> callback, Reason reason) {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public void connect(ServerConnectRequest request) {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public Server getServer() {
		return this.server;
	}

	@Override
	public int getPing() {
		return 0;
	}

	@Override
	public void sendData(String channel, byte[] data) {
		ProxyServer.getInstance().getLogger().info("sending " + data.length + " bytes to " + this.name);
	}

	@Override
	public PendingConnection getPendingConnection() {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public void chat(String message) {
		ProxyServer.getInstance().getLogger().info(this.name + " says " + message);
	}

	@Override
	public ServerInfo getReconnectServer() {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public void setReconnectServer(ServerInfo server) {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public String getUUID() {
		return this.uuid.toString();
	}

	@Override
	public UUID getUniqueId() {
		return this.uuid;
	}

	@Override
	public Locale getLocale() {
		return Locale.getDefault();
	}

	@Override
	public byte getViewDistance() {
		return 8;
	}

	@Override
	public ChatMode getChatMode() {
		return ChatMode.SHOWN;
	}

	@Override
	public boolean hasChatColors() {
		return false;
	}

	@Override
	public SkinConfiguration getSkinParts() {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public MainHand getMainHand() {
		return MainHand.RIGHT;
	}

	@Override
	public void setTabHeader(BaseComponent header, BaseComponent footer) {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public void setTabHeader(BaseComponent[] header, BaseComponent[] footer) {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public void resetTabHeader() {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public void sendTitle(Title title) {
		ProxyServer.getInstance().getLogger().info("set title for " + this.name + ": " + title.toString());
	}

	@Override
	public boolean isForgeUser() {
		// This is unreliable, let's not use it at all
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public Map<String, String> getModList() {
		return Collections.emptyMap();
	}

	@Override
	public Scoreboard getScoreboard() {
		throw new UnsupportedOperationException("Not implemented");
	}

}
