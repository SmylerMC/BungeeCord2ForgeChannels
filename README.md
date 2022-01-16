# BungeeCord2ForgeChannels
Allows communicating with Forge mods from a Bungee proxy. Can be used by either relying on it as a plugin dependency, in which case it needs to be installed as a BungeeCord plugin, or simply shaded in your own plugin's jar.

## How to use it
### Enabling the library
If the library is loaded as a standalone plugin from the plugin directory, it will initialize itself.
If you prefer to shade it in your own jar, you will need to call the initializing methods yourself:

```java
import fr.thesmyler.bungee2forge.BungeeToForgePlugin;
import net.md_5.bungee.api.plugin.Plugin;

public class ExamplePlugin extends Plugin {
	
	@Override
	public void onEnable() {
		BungeeToForgePlugin.onEnable(this);
	}
	
	@Override
	public void onDisable() {
		BungeeToForgePlugin.onDisable(this);
	}
  
}
```

### Implementing a packet
Create your own class implementing the [ForgePacket](https://github.com/SmylerMC/BungeeCord2ForgeChannels/blob/main/src/main/java/fr/thesmyler/bungee2forge/api/ForgePacket.java) interface:

```java
import fr.thesmyler.bungee2forge.api.ForgePacket;

public class ExamplePacket implements ForgePacket {

    private long leetLong = 1337;

    public ExamplePacket(long leetLong) {
        this.leetLong = leetLong;
    }
    
    public ExamplePacket() {
        // ForgePacket implementations always need a constructor with no parameters
    }

    @Override
    public void encode(ByteBuf buf) {
        // Write to the buffer
        buf.writeLong(this.leetLong);
    }

    @Override
    public void decode(ByteBuf buf) {
        // Read from the buffer
        this.leetLong = buf.readLong();
    }

    @Override
    public boolean processFromServer(String channel, Server fromServer, ProxiedPlayer toPlayer) {
        // Only let message reach the client if this.leetLong equals 1337
        return this.leetLong != 1337;
    }

    @Override
    public boolean processFromClient(String channel, ProxiedPlayer fromPlayer, Server toServer) {
        // Only let message reach the server if this.leetLong is not equal to 1337
        return this.leetLong == 1337;
    }

}
```

### Registering a packet to a channel
In order to be able to intercept and send packets, BungeeCord2ForgeChannels needs to be aware of your packet implementation. You therefore need to register it to a [ForgeChannel](https://github.com/SmylerMC/BungeeCord2ForgeChannels/blob/main/src/main/java/fr/thesmyler/bungee2forge/api/ForgeChannel.java) instance. You can retreive one from the [ForgeChannelRegistry](https://github.com/SmylerMC/BungeeCord2ForgeChannels/blob/main/src/main/java/fr/thesmyler/bungee2forge/api/ForgeChannelRegistry.java) singleton:
```java
import fr.thesmyler.bungee2forge.api.ForgeChannel;
import fr.thesmyler.bungee2forge.api.ForgeChannelRegistry;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ForgeNetworkManager {

  private final channel;
  
  public ForgeNetworkManager() {
    this.channel = ForgeChannelRegistry.instance().get("mymod:leetChannel");
    // Register a packet with the same discriminator as on the Forge side (0 in this example)
    this.channel.registerPacket(0, ExamplePacket.class);
  }
  
  public void sendLeetToClient(ProxiedPlayer player) {
    this.channel.send(new ExamplePacket(1337), player);
  }
  
  public void sendNoLeetToServer(Server server) {
    // From the server perspective, it will look like thisacket comes from one of the connected players
    this.channel.send(new ExamplePacket(0), server);
  }

}
```

Please refer to the javadoc for more details.
