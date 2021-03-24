package fr.thesmyler.bungee2forge.imp;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import fr.thesmyler.bungee2forge.api.ForgeChannel;
import fr.thesmyler.bungee2forge.api.ForgeChannelRegistry;
import fr.thesmyler.bungee2forge.api.ForgePacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;

//TODO Make sure channel name is valid
public class ForgeChannelImplementation implements ForgeChannel {
	
	private final String name;
	private final ForgeChannelRegistry registry;
	
    private final Map<Integer, Class<? extends ForgePacket>> packetMap = new HashMap<>();
    private final Map<Class<? extends ForgePacket>, Integer> discriminatorMap = new HashMap<>();
    
    private final Logger logger = ProxyServer.getInstance().getLogger();
	
	ForgeChannelImplementation(String name, ForgeChannelRegistry registry) {
		this.name = name;
		this.registry = registry;
	}

	@Override
	public String name() {
		return this.name;
	}

	@Override
	public synchronized void send(ForgePacket pkt, ProxiedPlayer to) {
        try {
            to.sendData(this.name, this.encode(pkt));
        } catch (Exception e) {
            this.logger.warning("Failed to send a Forge packet to player " + to.getName() + "/" + to.getUniqueId() + " in channel " + this.name + " : " + e);
            e.printStackTrace();
        }
	}

	@Override
	public synchronized void send(ForgePacket pkt, ProxiedPlayer... to) {
        int sent = 0;
        try {
            byte[] data = this.encode(pkt);
            for(ProxiedPlayer player : to) {
                player.sendData(this.name, data);
                sent++;
            }
        } catch (Exception e) {
            this.logger.warning("Failed to send a Forge packet to " + (to.length - sent) + " players in channel " + this.name + " : " + e);
            e.printStackTrace();
        }
	}

	@Override
	public synchronized void send(ForgePacket pkt, Server to) {
        try {
            to.sendData(this.name, this.encode(pkt));
        } catch (Exception e) {
            this.logger.warning("Failed to send a Forge packet to server " + to.getInfo().getName() + " in channel " + this.name + " : " + e);
            e.printStackTrace();
        }
	}

	@Override
	public synchronized void registerPacket(int discriminator, Class<? extends ForgePacket> clazz) {
		ensureDiscriminatorIsValid(discriminator);
		this.discriminatorMap.remove(clazz);
        this.packetMap.put(discriminator, clazz);
        this.discriminatorMap.put(clazz, discriminator);
	}
	
	@Override
	public synchronized boolean isRegistered() {
		return this.registry.getExisting(this.name) == this;
	}

	@Override
	public synchronized void deregisterPacket(int discriminator) {
		Class<? extends ForgePacket> clazz = this.packetMap.remove(discriminator);
		if(clazz != null) this.discriminatorMap.remove(clazz);
	}

	@Override
	public synchronized void deregisterPacket(Class<? extends ForgePacket> clazz) {
		Integer discriminator = this.discriminatorMap.remove(clazz);
		if(discriminator != null) this.packetMap.remove(discriminator);
	}

	@Override
	public synchronized void deregisterAllPackets() {
		this.discriminatorMap.clear();
		this.packetMap.clear();
	}
	
	synchronized boolean process(byte[] data, Connection sender, Connection receiver) {
        try {
        	
        	// Setup variables
            ByteBuf buffer = Unpooled.copiedBuffer(data);
            int discriminator = buffer.readByte();
            ProxiedPlayer player;
            Server server;
            boolean player2server = true;
            
            // Cast sender and receiver to player and server
            if (sender instanceof ProxiedPlayer && receiver instanceof Server) {
                player = (ProxiedPlayer) sender;
                server = (Server) receiver;
                player2server = true;
            } else if (sender instanceof Server && receiver instanceof ProxiedPlayer) {
                player = (ProxiedPlayer) receiver;
                server = (Server) sender;
                player2server = false;
            } else {
                this.logger.warning(
                        "Got an unknow combination of sender/receiver in Forge channel " + this.name + " channel. " +
                        "Sender " + sender.getClass() +
                        ", Receiver: " + receiver.getClass() +
                        ", Packet discriminator " + discriminator);
                return false;
            }
            
            // Create packet instance and decode the data
            Class<? extends ForgePacket> clazz = packetMap.get(discriminator);
            if (clazz == null) {
                if (player2server) {
                    throw new PacketEncodingException("Received an unregistered packet from player" + player.getName() + "/" + player.getUniqueId() + "/" + player.getSocketAddress() + " for server" + server.getInfo().getName() + "! Discriminator: " + discriminator);
                } else {
                    throw new PacketEncodingException("Received an unregistered packet from server " + server.getInfo().getName() + " for player " + player.getName() + "/" + player.getUniqueId() + "/" + player.getSocketAddress() + "! Discriminator: " + discriminator);
                }
            }
            ForgePacket packetHandler = clazz.newInstance();
            packetHandler.decode(buffer);
            
            // Process packet
            if (player2server) {
                return packetHandler.processFromClient(this.name, player, server);
            } else {
                return packetHandler.processFromServer(this.name, server, player);
            }
            
        } catch (Exception e) {
            this.logger.warning("Failed to process a Forge packet!");
            e.printStackTrace();
            return false;
        }
	}
	
    private byte[] encode(ForgePacket pkt) throws PacketEncodingException {
        if (!discriminatorMap.containsKey(pkt.getClass())) {
            throw new PacketEncodingException("Could not encode packet of class " + pkt.getClass().getCanonicalName() + " as it has not been registered to this channel");
        }
        int discriminator = discriminatorMap.get(pkt.getClass());
        ByteBuf stream = Unpooled.buffer();
        stream.writeByte(discriminator);
        pkt.encode(stream);
        return stream.array();
    }
	
	private static void ensureDiscriminatorIsValid(int discriminator) {
		if(discriminator < 0 || discriminator >= 256)
			throw new IllegalArgumentException("Discriminator needs to satisfy 0 <= discriminator < 256");
	}

}
