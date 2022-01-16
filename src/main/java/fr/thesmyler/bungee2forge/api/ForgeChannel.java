package fr.thesmyler.bungee2forge.api;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;


/**
 * Implements a Forge plugin channel. Encodes, decodes, receives and dispatches {@link ForgePacket}.
 * Takes care of adding discriminators at the beginning of packets.
 *
 * @author SmylerMC
 */
public interface ForgeChannel {
	
	/**
	 * Get the channel's name
	 * 
	 * @return the channel's name
	 */
    String name();
    
    /**
     * Indicates whether this channel is registered.
     * <br>
     * A channel that is not registered has been removed from the registry and using it will throw {@link IllegalStateException}
     * 
     * @return - true if this channel can be safely used
     */
    boolean isRegistered();
	
	/**
     * Sends the given packet to the given player
     * <br>
     * Logs an error if the packet class is not registered to this channel
     * 
     * @param pkt Packet to send
     * @param to  Player to send the packet to
     */
    void send(ForgePacket pkt, ProxiedPlayer to);
    
    /**
     * Sends the given packet to the given players
     * <br>
     * Logs an error if the packet class is not registered to this channel
     *
     * @param pkt Packet to send
     * @param to  Players to send the packet to
     */
    void send(ForgePacket pkt, ProxiedPlayer... to);
    
    /**
     * Sends the given packet to the given server
     * <br>
     * Logs an error if the packet class is not registered to this channel
     * 
     * @param pkt Packet to send
     * @param to  Server to send the packet to
     */
    void send(ForgePacket pkt, Server to);
    
    /**
     * Registers a packet class, overriding any existing registration with the same discriminator
     *
     * @param discriminator The discriminator to use when sending this packet
     * @param clazz         the {@link ForgePacket} implementing class
     */
    void registerPacket(int discriminator, Class<? extends ForgePacket> clazz);
    
    /**
     * Deregisters the packet class with the given discriminator
     *
     * @param discriminator The packet discriminator
     */
    void deregisterPacket(int discriminator);
    
    /**
     * Deregisters a packet class
     *
     * @param clazz The packet class
     */
    void deregisterPacket(Class<? extends ForgePacket> clazz);
    
    /**
     * Deregisters all packet classes
     */
    void deregisterAllPackets();
    
    /**
     * Indicates if the given discriminator has a packet class registered
     * 
     * @param discriminator a packet discriminator to check
     * @return true if a packet class has been registered for this discriminator
     */
    boolean isDiscriminatorRegistered(int discriminator);
    
    /**
     * Indicates if a given class is registered as a packet
     * 
     * @param clazz a class to check
     * @return true if this class is registered
     */
    boolean isRegisteredPacket(Class<? extends ForgePacket> clazz);

}
