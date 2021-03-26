package fr.thesmyler.bungee2forge.substitute;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.Server;

public class FakeServer implements Server {
	
	private ServerInfo info;
	
	public FakeServer(ServerInfo info) {
		this.info = info;
	}

	@SuppressWarnings("deprecation")
	@Override
	public InetSocketAddress getAddress() {
		return this.info.getAddress();
	}

	@Override
	public SocketAddress getSocketAddress() {
		return this.info.getSocketAddress();
	}

	@Override
	public void disconnect(String reason) {
		ProxyServer.getInstance().getLogger().info("disconnecting server " + this.info.getName() + ": " + reason);
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
	public ServerInfo getInfo() {
		return this.info;
	}

	@Override
	public void sendData(String channel, byte[] data) {
		this.info.sendData(channel, data);
	}

}
