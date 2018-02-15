package quaternary.dazzle.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class DzItemBlock extends ItemBlock {
	public DzItemBlock(Block b) {
		super(b);
		setRegistryName(b.getRegistryName());
	}
}
