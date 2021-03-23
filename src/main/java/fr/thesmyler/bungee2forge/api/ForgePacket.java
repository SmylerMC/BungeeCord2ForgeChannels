package fr.thesmyler.bungee2forge.api;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;

/**
 * Represents a Forge plugin message that's being sent or received via a {@link ForgeChannel}.
 *
 * @author SmylerMC
 */
public interface ForgePacket {

    /**
     * Encode the packet's content in the buffer, excluding discriminator
     *
     * @param buf
     */
    void encode(ByteBuf buf);

    /**
     * Decode the packet's content from the buffer, excluding discriminator
     *
     * @param buf
     */
    void decode(ByteBuf buf);

    /**
     * Process a packet sent from a server to a client
     *
     * @param channel
     * @param fromServer
     * @param toPlayer
     * @return true if the packet should be stopped from reaching the client
     */
    default boolean processFromServer(String channel, Server fromServer, ProxiedPlayer toPlayer) {
    	return false;
    }

    /**
     * Process a packet sent from a client to a server
     *
     * @param channel
     * @param fromPlayer
     * @param toServer
     * @return true if the packet should be stopped from reaching the server
     */
    default boolean processFromClient(String channel, ProxiedPlayer fromPlayer, Server toServer) {
    	return false;
    }

}