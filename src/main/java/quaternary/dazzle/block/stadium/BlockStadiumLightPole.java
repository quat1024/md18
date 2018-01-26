package quaternary.dazzle.block.stadium;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import quaternary.dazzle.block.BlockBase;

public class BlockStadiumLightPole extends BlockStadiumLightBase {
	public BlockStadiumLightPole() {
		super("stadium_pole", ComponentType.POLE);
	}
	
	static final double THICC = 5/16d; 
	static final AxisAlignedBB AABB = new AxisAlignedBB(THICC, 0, THICC, 1 - THICC, 1, 1 - THICC);
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return AABB;
	}
	
	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos) {
		Block b = world.getBlockState(pos.down()).getBlock();
		ComponentType comp = b instanceof BlockStadiumLightBase ? ((BlockStadiumLightBase) b).type : null;
		return super.canPlaceBlockAt(world, pos) && comp == ComponentType.BASE || comp == ComponentType.POLE;
	}
}
