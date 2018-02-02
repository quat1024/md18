package quaternary.dazzle;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.common.Mod;

@Config(modid = Dazzle.MODID, name = Dazzle.NAME)
@Mod.EventBusSubscriber(modid = Dazzle.MODID)
public class DazzleConfig {
	@Config.Name("mirage_support")
	@Config.Comment("If Mirage is installed, certain lights will create colored lighting shader effects.\n\nThis config option does nothing without Mirage installed.")
	@Config.LangKey(Dazzle.MODID + ".config.mirage_support")
	public static boolean MIRAGE_SUPPORT = true;
}
