package fr.thesmyler.bungee2forge.substitute;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class FakeServerInfo implements ServerInfo {
	
	private String name;
	private String motd;
	private InetSocketAddress address = InetSocketAddress.createUnresolved("localhost", 25566);
	private boolean restricted = false;
	private List<ProxiedPlayer> players = new ArrayList<>();
	
	
	public FakeServerInfo(String name) {
		this.name = name;
		this.motd = name + " is a server";
	}
	
	public FakeServerInfo(String name, InetSocketAddress address, boolean restricted) {
		this(name);
		this.address = address;
		this.restricted = restricted;
	}
	
	public FakeServerInfo(String name, InetSocketAddress address, String motd, boolean restricted) {
		this(name);
		this.motd = motd;
	}

	@Override
	public String getName() {
		return this.name;
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
	public Collection<ProxiedPlayer> getPlayers() {
		return this.players;
	}

	@Override
	public String getMotd() {
		return this.motd;
	}

	@Override
	public boolean isRestricted() {
		return this.restricted;
	}

	@Override
	public String getPermission() {
		return "server." + this.name;
	}

	@Override
	public boolean canAccess(CommandSender sender) {
		return this.restricted;
	}

	@Override
	public void sendData(String channel, byte[] data) {
		this.sendData(channel, data, false);
	}

	@Override
	public boolean sendData(String channel, byte[] data, boolean queue) {
		ProxyServer.getInstance().getLogger().info("send " + data.length + " bytes to server " + this.name + " through channel " + channel);
		return false;
	}

	@Override
	public void ping(Callback<ServerPing> callback) {
		callback.done(new ServerPing(), null);
	}

}
