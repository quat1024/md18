package quaternary.dazzle.block.stadium;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import quaternary.dazzle.block.BlockBase;

import javax.annotation.Nullable;

public class BlockStadiumLightPole extends BlockBase {
	public BlockStadiumLightPole() {
		super("stadium_pole", Material.IRON);
	}
	
	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos) {
		Block b = world.getBlockState(pos.down()).getBlock();
		return super.canPlaceBlockAt(world, pos) && (b instanceof BlockStadiumLightBase || b instanceof  BlockStadiumLightPole);
	}
	
	//allow placing blocks on the side of the pole to function as placing on the top
	
	@GameRegistry.ObjectHolder("dazzle:stadium_pole")
	public static final Item THIS_ITEM = Items.AIR;
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack heldStack = player.getHeldItem(hand);
		if(heldStack.getItem() == THIS_ITEM) {
			BlockPos poleTop = pos;
			while(poleTop.getY() < 255 && (world.getBlockState(poleTop).getBlock() instanceof BlockStadiumLightPole || world.getBlockState(poleTop).getBlock() instanceof BlockStadiumLightBase)) {
				poleTop = poleTop.up();
			}
			if(poleTop.getY() == 255) return false;			
			//poleTop now contains the blockpos of the first non-pole block
			if(world.getBlockState(poleTop).getBlock().isReplaceable(world, poleTop)) {
				world.setBlockState(poleTop, getDefaultState());
				
				if(!player.capabilities.isCreativeMode) heldStack.shrink(1);
				
				return true;
			}
		}
		return false;
	}
	
	//remove the top of the pole
	@Override
	public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack) {
		StadiumLightUtils.breakPole(world, pos.up(), !player.capabilities.isCreativeMode);
		super.harvestBlock(world, player, pos, state, te, stack);
	}
}
