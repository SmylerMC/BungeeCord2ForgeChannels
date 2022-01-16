package fr.thesmyler.bungee2forge;

import fr.thesmyler.bungee2forge.imp.ForgeChannelRegistryImplementation;
import net.md_5.bungee.api.plugin.Plugin;

/**
 * When this is loaded as a plugin by BungeeCord, no further action is needed to use the library.
 * However, if ou wish to shade the library in your own plugin, you will need to:
 * <ul>
 *  <li>Call {@link #onEnable(Plugin)} with your own plugin as the argument when it gets enabled.</li>
 *  <li>Call {@link #onDisable(Plugin)} with your own plugin as the argument when it gets disable.</li>
 * </ul>
 * This will register and unregister the necessary event listeners.
 *
 *
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
