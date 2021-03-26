package fr.thesmyler.bungee2forge;

import fr.thesmyler.bungee2forge.substitute.FakeProxyServer;
import net.md_5.bungee.api.ProxyServer;

public class TestSetup {
	
	private static FakeProxyServer proxy;
	
	public static FakeProxyServer ensureTestEnvironementSetup() {
		if(proxy == null) {
			proxy = new FakeProxyServer();
			ProxyServer.setInstance(proxy);
		}
		return proxy;
	}

}
