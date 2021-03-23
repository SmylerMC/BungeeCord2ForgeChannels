package fr.thesmyler.bungee2forge.imp;

import java.util.HashMap;
import java.util.Map;

import fr.thesmyler.bungee2forge.api.ForgeChannel;
import fr.thesmyler.bungee2forge.api.ForgeChannelRegistry;

public class ForgeChannelRegistryImplementation extends ForgeChannelRegistry {
	
	private final Map<String, ForgeChannel> channels = new HashMap<>();

	@Override
	public ForgeChannel get(String name) {
		ForgeChannel channel = this.getExisting(name);
		if(channel == null) channel = new ForgeChannelImplementation(name, this);
		return channel;
	}

	@Override
	public ForgeChannel getExisting(String name) {
		return this.channels.get(name);
	}

	@Override
	public boolean exists(String name) {
		return this.getExisting(name) != null;
	}

	@Override
	public void deregister(String name) {
		this.channels.remove(name);		
	}

	@Override
	public void deregister(ForgeChannel channel) {
		ForgeChannel known = this.getExisting(channel.name());
		if(known != channel) throw new IllegalArgumentException("The ForgeChannel instance is not registered to this registry");
		this.deregister(channel.name());
	}

}
