package fr.thesmyler.bungee2forge.api;

import fr.thesmyler.bungee2forge.imp.ForgeChannelRegistryImplementation;

/**
 * Publicly facing Forge channel registry
 * 
 * @author SmylerMC
 *
 */
public abstract class ForgeChannelRegistry {
	
	private static ForgeChannelRegistry instance;
	
	/**
	 * 
	 * @return the forge registry singleton
	 */
	public static ForgeChannelRegistry instance() {
		if(instance == null) instance = new ForgeChannelRegistryImplementation();
		return instance;
	}
	
	/**
	 * Returns a channel with the given name, either by creating a new one or by returning the one from the registry if it already exists
	 * 
	 * @param name - Channel name, e.g. "pluginname:channelname" (bungee style) or "channelname" (forge style)
	 * 
	 * @return A channel with the desired name
	 */
	public abstract ForgeChannel get(String name);
	
	/**
	 * Returns an existing channel with the given name, or null if none exist already
	 * 
	 * @param name - Channel name, e.g. "pluginname:channelname" (bungee style) or "channelname" (forge style)
	 * 
	 * @return The channel with the desired name, if it exists, or null
	 */
	public abstract ForgeChannel getExisting(String name);
	
	/**
	 * Indicated if a given channel exists in this registry
	 * 
	 * @param name - Name of the channel to look for
	 * 
	 * @return true if the channel exists and is usable
	 */
	public abstract boolean exists(String name);
	
	/**
	 * Deregisters the channel with the given name. Does nothing if there is not such channel in this registry
	 * 
	 * @param name - The name of the channel to deregister
	 */
	public abstract void deregister(String name);
	
	/**
	 * Deregisters the given channel. Does nothing if there is not such channel in this registry
	 * 
	 * @throws IllegalArgumentException if channel is null or not registered
	 * 
	 * @param channel - A channel instance to deregister
	 */
	public abstract void deregister(ForgeChannel channel);
}
