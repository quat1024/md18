package quaternary.dazzle.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import quaternary.dazzle.Dazzle;
import quaternary.dazzle.DazzleCreativeTab;

public class BlockBase extends Block {
	public BlockBase(String name, Material mat) {
		super(mat);
		
		setRegistryName(new ResourceLocation(Dazzle.MODID, name));
		setUnlocalizedName(Dazzle.MODID + "." + name);
		
		if(this.hasItemForm()) {
			setCreativeTab(DazzleCreativeTab.INST);
		}
	}
	
	public BlockBase(String name, Material mat, MapColor color) {
		super(mat, color);
		//CODE DUPLICATION HAHA YES
		setRegistryName(new ResourceLocation(Dazzle.MODID, name));
		setUnlocalizedName(Dazzle.MODID + "." + name);
		
		if(this.hasItemForm()) {
			setCreativeTab(DazzleCreativeTab.INST);
		}
	}
	
	//item form management
	Item itemForm;
	
	public boolean hasItemForm() {
		return true;
	}
	
	public Item getItemForm() {
		if(itemForm == null) {
			itemForm = new ItemBlock(this).setRegistryName(this.getRegistryName());
		}
		return itemForm;
	}
	
	//custom statemapper management
	public boolean hasCustomStatemapper() {
		return false;
	}
	
	public Object getCustomStatemapper() {
		return null;
	}
	
	//custom iblockcolor stuff
	public boolean hasBlockColors() { 
		return false;
	}
	
	//Yaknow i should really make these abstract.
	public Object getBlockColors() {
		return null;
	}
	
	public Object getItemColors() {
		return null;
	}
	
	//a more reasonable default
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face)
	{
		return state.isFullBlock() ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
	}
}
