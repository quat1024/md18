package quaternary.dazzle;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.registry.GameRegistry;
import quaternary.dazzle.block.BlockBase;

public class DazzleCreativeTab extends CreativeTabs {
	public static DazzleCreativeTab INST = new DazzleCreativeTab();
	
	public DazzleCreativeTab() {
		super(Dazzle.MODID);
	}
	
	@GameRegistry.ObjectHolder("dazzle:red_modern_digital_lamp")
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
	
	@Override
	public void displayAllRelevantItems(NonNullList<ItemStack> list) {
		for(BlockBase b : Dazzle.BLOCKS) {
			if(b.hasItemForm()) {
				list.add(new ItemStack(b.getItemForm()));
			}
		}
	}
}
