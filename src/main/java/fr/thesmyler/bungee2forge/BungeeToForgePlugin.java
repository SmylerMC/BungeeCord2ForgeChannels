package fr.thesmyler.bungee2forge;

import fr.thesmyler.bungee2forge.imp.ForgeChannelRegistryImplementation;
import net.md_5.bungee.api.plugin.Plugin;

/**
 * @author SmylerMC
 *
 */
public class BungeeToForgePlugin extends Plugin {
	
	@Override
	public void onEnable() {
		onEnable(this);
	}
	
	@Override
	public void onDisable() {
		onDisable(this);
	}
	
	public static void onEnable(Plugin plugin) {
		plugin.getProxy().getPluginManager().registerListener(plugin, ForgeChannelRegistryImplementation.INSTANCE);
		ForgeChannelRegistryImplementation.INSTANCE.registerChannelsToBungee(plugin);
	}
	
	public static void onDisable(Plugin plugin) {
		plugin.getProxy().getPluginManager().unregisterListener(ForgeChannelRegistryImplementation.INSTANCE);
		ForgeChannelRegistryImplementation.INSTANCE.unregisterChannelsToBungee(plugin);
	}

}
