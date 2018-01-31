package quaternary.dazzle;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class DazzleCreativeTab extends CreativeTabs {
	public static DazzleCreativeTab INST = new DazzleCreativeTab();
	
	public DazzleCreativeTab() {
		super(Dazzle.MODID);
	}
	
	@GameRegistry.ObjectHolder("dazzle:light_blue_pulsating_digital_lamp")
	public static final Item tabIcon = Items.AIR;
	
	@Override
	public ItemStack getTabIconItem() {
		return new ItemStack(tabIcon);
	}
	
	@Override
	public boolean hasSearchBar() {
		return true;
	}
	
	@Override
	public String getBackgroundImageName() {
		return "item_search.png";
	}
}
