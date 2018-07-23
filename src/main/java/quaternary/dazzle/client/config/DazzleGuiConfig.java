package quaternary.dazzle.client.config;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import quaternary.dazzle.common.Dazzle;
import quaternary.dazzle.common.DazzleConfig;

import java.util.List;
import java.util.stream.Collectors;

public class DazzleGuiConfig extends GuiConfig {
	public DazzleGuiConfig(GuiScreen parent) {
		super(parent, populateConfigElements(), Dazzle.MODID, false, false, Dazzle.NAME);
	}
	
	//Adapted from Choonster's TestMod3. They say they adapted it from EnderIO "a while back".
	//http://www.minecraftforge.net/forum/topic/39880-110solved-make-config-options-show-up-in-gui/
	private static List<IConfigElement> populateConfigElements() {
		Configuration c = DazzleConfig.config;
		//Don't look!
		return c.getCategoryNames().stream().filter(name -> !c.getCategory(name).isChild()).map(name -> new ConfigElement(c.getCategory(name).setLanguageKey(Dazzle.MODID + ".config." + name))).collect(Collectors.toList());
	}
}
