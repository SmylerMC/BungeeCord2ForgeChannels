package fr.thesmyler.bungee2forge.imp;

import java.util.HashMap;
import java.util.Map;

import fr.thesmyler.bungee2forge.api.ForgeChannel;
import fr.thesmyler.bungee2forge.api.ForgeChannelRegistry;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public class ForgeChannelRegistryImplementation extends ForgeChannelRegistry {
	
	public static final ForgeChannelRegistryImplementation INSTANCE = new ForgeChannelRegistryImplementation();
	
	private final Map<String, ForgeChannelImplementation> channels = new HashMap<>();

	@Override
	public synchronized ForgeChannel get(String name) {
		ForgeChannel channel = this.getExisting(name);
		if(channel == null) {
			channel = new ForgeChannelImplementation(name, this);
			ProxyServer.getInstance().registerChannel(name);
		}
		return channel;
	}

	@Override
	public synchronized ForgeChannel getExisting(String name) {
		return this.channels.get(name);
	}

	@Override
	public synchronized boolean exists(String name) {
		return this.getExisting(name) != null;
	}

	@Override
	public synchronized void deregister(String name) {
		ProxyServer.getInstance().unregisterChannel(name);
		this.channels.remove(name);		
	}

	@Override
	public synchronized void deregister(ForgeChannel channel) {
		String name = channel.name();
		ForgeChannel known = this.getExisting(name);
		if(known != channel) throw new IllegalArgumentException("The ForgeChannel instance is not registered to this registry");
		ProxyServer.getInstance().unregisterChannel(name);
		this.deregister(name);
	}
	
	@EventHandler
	public void onPluginMessage(PluginMessageEvent event) {
		ForgeChannelImplementation channel = this.channels.get(event.getTag());
		if(channel != null && channel.process(event.getData(), event.getSender(), event.getReceiver())) event.setCancelled(true);
	}
	
	public void registerChannelsToBungee(Plugin plugin) {
		for(String channel: this.channels.keySet()) plugin.getProxy().registerChannel(channel);
	}
	
	public void unregisterChannelsToBungee(Plugin plugin) {
		for(String channel: this.channels.keySet()) plugin.getProxy().unregisterChannel(channel);
	}

}
