package quaternary.dazzle.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import quaternary.dazzle.Dazzle;
import quaternary.dazzle.DazzleCreativeTab;
import quaternary.dazzle.item.DzItemBlock;

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
			itemForm = new DzItemBlock(this);
		}
		return itemForm;
	}
	
	//custom statemapper management
	@SideOnly(Side.CLIENT)
	public boolean hasCustomStatemapper() {
		return false;
	}
	@SideOnly(Side.CLIENT)
	public IStateMapper getCustomStatemapper() {
		return null;
	}
	
	//custom iblockcolor stuff
	@SideOnly(Side.CLIENT)
	public boolean hasBlockColors() { 
		return false;
	}
	@SideOnly(Side.CLIENT)
	public IBlockColor getBlockColors() {
		return null;
	}
	
	//a more reasonable default
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face)
	{
		return state.isFullBlock() ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
	}
}
