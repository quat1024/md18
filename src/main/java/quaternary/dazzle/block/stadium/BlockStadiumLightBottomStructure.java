package quaternary.dazzle.block.stadium;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import quaternary.dazzle.block.BlockBase;

import java.util.Collections;

/** An invisible block only used for taking up space around the base of the stadium light. */
public class BlockStadiumLightBottomStructure extends BlockBase {
	public BlockStadiumLightBottomStructure() {
		super("stadium_base_structure", Material.IRON, MapColor.IRON);
	}
	
	@Override
	public boolean hasItemForm() {
		return false;
	}
	
	@Override
	public boolean hasCustomStatemapper() {
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IStateMapper getCustomStatemapper() {
		return block -> Collections.emptyMap();
	}
	
	//aabb
	static final AxisAlignedBB AABB = new AxisAlignedBB(0, 0, 0, 1, 0.5, 1);
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return AABB;
	}
	
	//make it "part of the structure"
	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
		boolean hasAdjacentBase = false;
		for(EnumFacing h : EnumFacing.HORIZONTALS) {
			BlockPos offsetPos = pos.offset(h);
			Block b = world.getBlockState(offsetPos).getBlock();
			
			if(b instanceof BlockStadiumLightBottom) {
				hasAdjacentBase = true;
				//make sure the lamp base gets an update
				//this is fucking spaghetti
				((BlockStadiumLightBottom)b).doLighting(world, offsetPos);
			}
		}
		
		if(!hasAdjacentBase) {
			world.setBlockToAir(pos);
		}
	}
	
	//Make it invisible
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.INVISIBLE;
	}
	
	//Make it invisible super for real
	@Override
	public boolean isFullBlock(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
		return false;
	}
	
	@Override //VERY GOOD MOJANGLE
	public boolean isBlockNormalCube(IBlockState state) {
		return false;
	}
}
