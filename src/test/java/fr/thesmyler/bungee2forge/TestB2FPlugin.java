package fr.thesmyler.bungee2forge;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fr.thesmyler.bungee2forge.api.ForgeChannel;
import fr.thesmyler.bungee2forge.api.ForgeChannelRegistry;
import fr.thesmyler.bungee2forge.imp.ForgeChannelRegistryImplementation;
import fr.thesmyler.bungee2forge.packets.OtherPacketTest;
import fr.thesmyler.bungee2forge.packets.PacketTest;
import fr.thesmyler.bungee2forge.substitute.FakeProxiedPlayer;
import fr.thesmyler.bungee2forge.substitute.FakeServer;
import fr.thesmyler.bungee2forge.substitute.FakeServerInfo;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;

public class TestB2FPlugin {
	
	private ForgeChannel testChannel;
	
	private PacketTest testPacket;
	private byte[] testEncodedPacket;
	
	public static PacketTest lastFromClient;
	public static PacketTest lastFromServer;
	
	@Before public void setupTestEnvirronment() {
		TestSetup.ensureTestEnvironementSetup();
		this.testChannel = ForgeChannelRegistry.instance().get("test");
		this.testChannel.deregisterAllPackets();
		this.testChannel.registerPacket(0, PacketTest.class);
		

		this.testPacket = new PacketTest();
		Server s = new FakeServer(new FakeServerInfo("Server"));
		ProxiedPlayer p = new FakeProxiedPlayer(UUID.randomUUID(), "Testman", s) {
			@Override
			public void sendData(String channel, byte[] data) {
				super.sendData(channel, data);
				TestB2FPlugin.this.testEncodedPacket = data;
			}
		};
		this.testChannel.send(this.testPacket, p);
	}
	
	@Test public void testSend() {
		Server s = new FakeServer(new FakeServerInfo("Server"));
		PacketTest original = new PacketTest();
		PacketTest received = new PacketTest();
		ProxiedPlayer p = new FakeProxiedPlayer(UUID.randomUUID(), "Testman", s) {

			@Override
			public void sendData(String channel, byte[] data) {
				super.sendData(channel, data);
				ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
				buf.writeBytes(data);
				// Since we are manually decoding this, we need to read the discriminator ourself
				buf.readByte();
				received.decode(buf);
			}
			
		};
		this.testChannel.send(original, p);
		Assert.assertEquals(original.testInt, received.testInt);
		Assert.assertEquals(original.testLong, received.testLong);
		Assert.assertEquals(original.testString, received.testString);
	}
	
	@Test public void testReceive() {
		Server s = new FakeServer(new FakeServerInfo("Server"));
		ProxiedPlayer p = new FakeProxiedPlayer(UUID.randomUUID(), "Testman", s);
		
		PluginMessageEvent event = new PluginMessageEvent(p, s, this.testChannel.name(), this.testEncodedPacket);
		ForgeChannelRegistryImplementation.INSTANCE.onPluginMessage(event);
		Assert.assertEquals(event.isCancelled(), true);
		Assert.assertEquals(this.testPacket.testInt, lastFromClient.testInt);
		Assert.assertEquals(this.testPacket.testLong, lastFromClient.testLong);
		Assert.assertEquals(this.testPacket.testString, lastFromClient.testString);
		
		event = new PluginMessageEvent(s, p, this.testChannel.name(), this.testEncodedPacket);
		ForgeChannelRegistryImplementation.INSTANCE.onPluginMessage(event);
		Assert.assertEquals(event.isCancelled(), false);
		Assert.assertEquals(this.testPacket.testInt, lastFromServer.testInt);
		Assert.assertEquals(this.testPacket.testLong, lastFromServer.testLong);
		Assert.assertEquals(this.testPacket.testString, lastFromServer.testString);
	}
	
	@Test public void testPacketRegistration() {
		this.testChannel.registerPacket(15, PacketTest.class);
		Assert.assertThrows(IllegalArgumentException.class, () -> testChannel.registerPacket(-1, PacketTest.class));
		Assert.assertThrows(IllegalArgumentException.class, () -> testChannel.registerPacket(256, PacketTest.class));
		ForgeChannel channel = ForgeChannelRegistry.instance().get("regstest");
		Assert.assertFalse(channel.isDiscriminatorRegistered(50));
		Assert.assertFalse(channel.isRegisteredPacket(PacketTest.class));
		channel.registerPacket(50, PacketTest.class);
		Assert.assertTrue(channel.isDiscriminatorRegistered(50));
		Assert.assertTrue(channel.isRegisteredPacket(PacketTest.class));
		channel.deregisterPacket(PacketTest.class);
		Assert.assertFalse(channel.isDiscriminatorRegistered(50));
		Assert.assertFalse(channel.isRegisteredPacket(PacketTest.class));
		channel.registerPacket(50, PacketTest.class);
		Assert.assertTrue(channel.isDiscriminatorRegistered(50));
		Assert.assertTrue(channel.isRegisteredPacket(PacketTest.class));
		channel.deregisterPacket(50);
		Assert.assertFalse(channel.isDiscriminatorRegistered(50));
		Assert.assertFalse(channel.isRegisteredPacket(PacketTest.class));
		channel.registerPacket(50, PacketTest.class);
		channel.registerPacket(50, OtherPacketTest.class);
		Assert.assertFalse(channel.isRegisteredPacket(PacketTest.class));
		Assert.assertTrue(channel.isRegisteredPacket(OtherPacketTest.class));
		Assert.assertTrue(channel.isDiscriminatorRegistered(50));
		channel.deregisterAllPackets();
		Assert.assertFalse(channel.isDiscriminatorRegistered(50));
		Assert.assertFalse(channel.isRegisteredPacket(OtherPacketTest.class));
	}
	
	@Test public void testChannelRegistration() {
		ForgeChannel channel1 = ForgeChannelRegistry.instance().get("channel1");
		ForgeChannel channel2 = ForgeChannelRegistry.instance().get("channel2");
		Assert.assertTrue(channel1 == ForgeChannelRegistry.instance().get(channel1.name()));
		Assert.assertFalse(ForgeChannelRegistry.instance().exists("channel3"));
		Assert.assertTrue(ForgeChannelRegistry.instance().exists("channel2"));
		Assert.assertTrue(channel2.isRegistered());
		ForgeChannelRegistry.instance().deregister(channel2.name());
		Assert.assertFalse(channel2.isRegistered());
		Assert.assertFalse(ForgeChannelRegistry.instance().exists("channel2"));
		Assert.assertNull(ForgeChannelRegistry.instance().getExisting("channel2"));
		Assert.assertNull(ForgeChannelRegistry.instance().getExisting("channel34"));
		Assert.assertThrows(IllegalArgumentException.class, () -> ForgeChannelRegistry.instance().get("thisnameislongerthan20chars"));
		ForgeChannelRegistry.instance().deregister(channel1);
		Assert.assertFalse(channel1.isRegistered());
		Assert.assertThrows(IllegalArgumentException.class, () -> ForgeChannelRegistry.instance().deregister(channel2));
	}

}
