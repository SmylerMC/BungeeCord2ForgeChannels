package fr.thesmyler.bungee2forge.packets;

import java.util.Random;

import com.google.common.base.Charsets;

import fr.thesmyler.bungee2forge.TestB2FPlugin;
import fr.thesmyler.bungee2forge.api.ForgePacket;
import io.netty.buffer.ByteBuf;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;

public class PacketTest implements ForgePacket {
	
	public int testInt;
	public long testLong;
	public String testString;
	
	public PacketTest() {
		String alpha = "azertyuiopqsdfghjklmwxcvbn1234567890AZERTYUIOPQSDFGHJKLMWXCVBN";
		Random r = new Random();
		this.testInt = r.nextInt();
		this.testLong = r.nextLong();
		this.testString = "";
		while(r.nextFloat() > 0.05) this.testString += alpha.charAt(Math.abs(r.nextInt()) % alpha.length());
	}

	@Override
	public void encode(ByteBuf buf) {
		buf.writeInt(this.testInt);
		buf.writeLong(this.testLong);
		buf.writeInt(this.testString.length());
		buf.writeCharSequence(this.testString, Charsets.UTF_8);
	}

	@Override
	public void decode(ByteBuf buf) {
		this.testInt = buf.readInt();
		this.testLong = buf.readLong();
		int i = buf.readInt();
		this.testString = "" + buf.readCharSequence(i, Charsets.UTF_8);
	}

	@Override
	public boolean processFromServer(String channel, Server fromServer, ProxiedPlayer toPlayer) {
		TestB2FPlugin.lastFromServer = this;
		return false;
	}

	@Override
	public boolean processFromClient(String channel, ProxiedPlayer fromPlayer, Server toServer) {
		TestB2FPlugin.lastFromClient = this;
		return true;
	}
	
	

}
