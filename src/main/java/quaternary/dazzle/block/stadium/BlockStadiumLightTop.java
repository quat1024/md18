package quaternary.dazzle.block.stadium;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nullable;
import java.util.List;

public class BlockStadiumLightTop extends BlockStadiumLightBase {
	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	public static final PropertyBool LIT = PropertyBool.create("lit");
	
	public BlockStadiumLightTop() {
		super("stadium_top", ComponentType.TOP);
		
		setDefaultState(getDefaultState().withProperty(FACING, EnumFacing.NORTH).withProperty(LIT, false));
	}
	
	@Override
	public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(TextFormatting.DARK_GRAY + "" + TextFormatting.ITALIC + "Pay no attention to the item model.");
	}
	
	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos) {
		Block b = world.getBlockState(pos.down()).getBlock();
		ComponentType comp = b instanceof BlockStadiumLightBase ? ((BlockStadiumLightBase) b).type : null;
		return super.canPlaceBlockAt(world, pos) && comp == ComponentType.POLE;
	}
	
	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
		int lightLevel = getLightPower(world, pos);
		placeLightBlocks(world, pos, lightLevel);		
		super.neighborChanged(state, world, pos, block, fromPos);
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		placeLightBlocks(world, pos, 0);
		super.breakBlock(world, pos, state);
	}
	
	//@GameRegistry.ObjectHolder("dazzle:invisible_light_source")
	//public static final Block LIGHT_SOURCE = Blocks.DIAMOND_BLOCK;
	
	//placing and removing light blocks
	void placeLightBlocks(World w, BlockPos lampPos, int lightValue) {
		if(w.isRemote) return;
		
		if(w.getBlockState(lampPos).getBlock() instanceof BlockStadiumLightTop)
			w.setBlockState(lampPos, w.getBlockState(lampPos).withProperty(LIT, lightValue != 0));
		
		/*
		
		//find the position on the ground that the lamp is "pointing at"
		EnumFacing facing = w.getBlockState(lampPos).getValue(FACING);
		
		BlockPos chaser = lampPos.add(0,0,0);
		Block thisBlock;
		float distanceBonus = 0;
		do {
			chaser = chaser.add(facing.getFrontOffsetX() * 2, -1, facing.getFrontOffsetZ() * 2);
			thisBlock = w.getBlockState(chaser).getBlock();
			distanceBonus++;
		} while (thisBlock.isReplaceable(w, chaser) && chaser.getY() > 0);
		
		distanceBonus = MathHelper.clamp(1 + (distanceBonus / 2f), 1, 10);
		
		//might have sunken into the ground a little bit so find the top here
		int correctionCount = 0;
		do {
			chaser = chaser.add(0, 1, 0);
			thisBlock = w.getBlockState(chaser).getBlock();
			correctionCount++;
		} while (!thisBlock.isReplaceable(w, chaser));
		if(correctionCount > 8) return; //don't climb wayyy up a pillar so lights don't show up in a bizarre spot
		
		float powerBonus;
		boolean placeLights;
		if(lightValue != 0) {
			powerBonus = lightValue / 5f;
			placeLights = true;
		} else {
			powerBonus = 3;
			placeLights = false;
		}
		
		//place the light circle at that location
		List<BlockPos> maxCircle = getCircle(999);
		List<BlockPos> circle = getCircle(MathHelper.floor(distanceBonus * powerBonus));
		
		System.out.println(distanceBonus * powerBonus);
		
		for(BlockPos pos : maxCircle) {
			BlockPos placementPos = pos.add(chaser.getX(), chaser.getY(), chaser.getZ());
			IBlockState stateToReplace = w.getBlockState(placementPos);
			if(stateToReplace.getBlock() == LIGHT_SOURCE) {
				w.setBlockState(placementPos, Blocks.AIR.getDefaultState(), 2);
			}
		}
		
		if(placeLights) {
			for(BlockPos pos : circle) {
				BlockPos placementPos = pos.add(chaser.getX(), chaser.getY(), chaser.getZ());
				IBlockState stateToReplace = w.getBlockState(placementPos);
				
				if(stateToReplace.getBlock().isAir(stateToReplace, w, placementPos)) {
					w.setBlockState(placementPos, LIGHT_SOURCE.getDefaultState(), 2);
				}
			}
		}
		*/
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		//adapted from ItemSign.java
		//int i = MathHelper.floor((double)((placer.rotationYaw + 180.0F) * 4.0F / 360.0F) + 0.5D) & 3;
		EnumFacing face = EnumFacing.fromAngle(placer.rotationYaw);
		
		return getDefaultState().withProperty(FACING, face).withProperty(LIT, getLightPower(world, pos) != 0);
	}
	
	int getLightPower(World w, BlockPos p) {
		BlockPos basePos = climbDownPole(w, p);
		if(basePos == null) return 0; //somehow
		return getLightPowerFromBase(w, basePos);
	}
	
	int getLightPowerFromBase(World w, BlockPos p) {
		int maxPower = w.isBlockIndirectlyGettingPowered(p);
		for(EnumFacing whichWay : EnumFacing.HORIZONTALS) {
			maxPower = Math.max(maxPower, w.isBlockIndirectlyGettingPowered(p.offset(whichWay)));
		}
		return maxPower;
	}
	
	//blockstate boilerplate
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(LIT, (meta & 4) != 0).withProperty(FACING, EnumFacing.getHorizontal(meta & 3));
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getHorizontalIndex() | (state.getValue(LIT) ? 4 : 0);
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING, LIT);
	}
}
