package quaternary.dazzle.block.stadium;

import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class BlockStadiumLightTop extends BlockStadiumLightBase {
	public static final PropertyInteger ROTATION = PropertyInteger.create("rotation", 0, 3);
	public static final PropertyBool LIT = PropertyBool.create("lit");
	
	public BlockStadiumLightTop() {
		super("stadium_top", ComponentType.TOP);
		
		setDefaultState(getDefaultState().withProperty(ROTATION, 0).withProperty(LIT, false));
	}
	
	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos) {
		Block b = world.getBlockState(pos.down()).getBlock();
		ComponentType comp = b instanceof BlockStadiumLightBase ? ((BlockStadiumLightBase) b).type : null;
		return super.canPlaceBlockAt(world, pos) && comp == ComponentType.POLE;
	}
	
	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
		boolean shouldLight = shouldLight(world, pos);
		if(shouldLight != world.getBlockState(pos).getValue(LIT)) {
			world.setBlockState(pos, world.getBlockState(pos).withProperty(LIT, shouldLight));
		}
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		//adapted from ItemSign.java
		int i = MathHelper.floor((double)((placer.rotationYaw + 180.0F) * 4.0F / 360.0F) + 0.5D) & 3;
		
		return getDefaultState().withProperty(ROTATION, i).withProperty(LIT, false);
	}
	
	boolean shouldLight(World w, BlockPos p) {
		BlockPos basePos = climbDownPole(w, p);
		if(basePos == null) return false; //somehow
		
		for(EnumFacing whichWay : EnumFacing.HORIZONTALS) {
			if(w.isBlockPowered(basePos.offset(whichWay))) return true;
		}
		return false;
	}
	
	//blockstate boilerplate
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(LIT, (meta & 4) != 0).withProperty(ROTATION, meta & 3);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(ROTATION) | (state.getValue(LIT) ? 4 : 0);
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, ROTATION, LIT);
	}
}
