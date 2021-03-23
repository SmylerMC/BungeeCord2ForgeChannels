package fr.thesmyler.bungee2forge.imp;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import fr.thesmyler.bungee2forge.api.ForgeChannel;
import fr.thesmyler.bungee2forge.api.ForgeChannelRegistry;
import fr.thesmyler.bungee2forge.api.ForgePacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;

public class ForgeChannelImplementation implements ForgeChannel {
	
	private final String name;
	private final ForgeChannelRegistry registry;
	
    private final Map<Integer, Class<? extends ForgePacket>> packetMap = new HashMap<>();
    private final Map<Class<? extends ForgePacket>, Integer> discriminatorMap = new HashMap<>();
    
    private Logger logger;
	
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
