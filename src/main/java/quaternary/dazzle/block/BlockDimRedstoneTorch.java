package quaternary.dazzle.block;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import quaternary.dazzle.Dazzle;
import quaternary.dazzle.DazzleCreativeTab;

import java.util.*;

/** Based on a copy of BlockRedstoneTorch. Changes noted. */
public class BlockDimRedstoneTorch extends BlockTorch
{
	private static final Map<World, List<BlockDimRedstoneTorch.Toggle>> toggles = new java.util.WeakHashMap<>(); // FORGE - fix vanilla MC-101233
	
	//Dazzle: Use IProperty for litness because Forge gives a state sensitive version of getLightValue
	public static final PropertyBool LIT = PropertyBool.create("lit");
	
	//Dazzle: public constructor
	public BlockDimRedstoneTorch()
	{
		//Why the fuck
		//this.setTickRandomly(true);
		
		//Dazzle: set proper names
		String name = "dim_redstone_torch";
		setRegistryName(Dazzle.MODID, name);
		setUnlocalizedName(Dazzle.MODID + "." + name);
		setCreativeTab(DazzleCreativeTab.INST);
	}
	
	//Dazzle: set light value here instead of when constructing
	//also use blockstate
	@Override
	public int getLightValue(IBlockState state) {
		return state.getValue(LIT) ? 2 : 0;
	}
	
	//Dazzle: add my new lit property to the map etc etc
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING, LIT);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return super.getMetaFromState(state) | (state.getValue(LIT) ? 8 : 0);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return super.getStateFromMeta(meta & 7).withProperty(LIT, (meta & 8) != 0);
	}
	
	private boolean isBurnedOut(World worldIn, BlockPos pos, boolean turnOff)
	{
		if (!toggles.containsKey(worldIn))
		{
			toggles.put(worldIn, Lists.newArrayList());
		}
		
		List<BlockDimRedstoneTorch.Toggle> list = (List)toggles.get(worldIn);
		
		if (turnOff)
		{
			list.add(new BlockDimRedstoneTorch.Toggle(pos, worldIn.getTotalWorldTime()));
		}
		
		int i = 0;
		
		for (int j = 0; j < list.size(); ++j)
		{
			BlockDimRedstoneTorch.Toggle BlockDimRedstoneTorch$toggle = list.get(j);
			
			if (BlockDimRedstoneTorch$toggle.pos.equals(pos))
			{
				++i;
				
				if (i >= 8)
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * How many world ticks before ticking
	 */
	public int tickRate(World worldIn)
	{
		return 2;
	}
	
	/**
	 * Called after the block is set in the Chunk data, but before the Tile Entity is set
	 */
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
	{
		if (state.getValue(LIT)) //Dazzle: use lit state
		{
			for (EnumFacing enumfacing : EnumFacing.values())
			{
				worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this, false);
			}
		}
	}
	
	/**
	 * Called serverside after this block is replaced with another in Chunk, but before the Tile Entity is updated
	 */
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		if (state.getValue(LIT)) //Dazzle: use lit state
		{
			for (EnumFacing enumfacing : EnumFacing.values())
			{
				worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this, false);
			}
		}
	}
	
	
	public int getWeakPower(IBlockState state, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
	{
		//Dazzle: use lit state and make it emit less signal strength
		return state.getValue(LIT) && state.getValue(FACING) != side ? 1 : 0;
	}
	
	private boolean shouldBeOff(World worldIn, BlockPos pos, IBlockState state)
	{
		EnumFacing enumfacing = state.getValue(FACING).getOpposite();
		return worldIn.isSidePowered(pos.offset(enumfacing), enumfacing);
	}
	
	/**
	 * Called randomly when setTickRandomly is set to true (used by e.g. crops to grow, etc.)
	 */
	public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random)
	{
	}
	
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		boolean flag = this.shouldBeOff(worldIn, pos, state);
		List<BlockDimRedstoneTorch.Toggle> list = (List)toggles.get(worldIn);
		
		while (list != null && !list.isEmpty() && worldIn.getTotalWorldTime() - (list.get(0)).time > 60L)
		{
			list.remove(0);
		}
		
		if (state.getValue(LIT)) //Dazzle: use lit state
		{
			if (flag)
			{
				//Dazzle: directly set lit state
				worldIn.setBlockState(pos, state.withProperty(LIT, false), 3);
				
				if (this.isBurnedOut(worldIn, pos, true))
				{
					worldIn.playSound(null, pos, SoundEvents.BLOCK_REDSTONE_TORCH_BURNOUT, SoundCategory.BLOCKS, 0.5F, 2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F);
					
					for (int i = 0; i < 5; ++i)
					{
						double d0 = (double)pos.getX() + rand.nextDouble() * 0.6D + 0.2D;
						double d1 = (double)pos.getY() + rand.nextDouble() * 0.6D + 0.2D;
						double d2 = (double)pos.getZ() + rand.nextDouble() * 0.6D + 0.2D;
						worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0D, 0.0D, 0.0D);
					}
					
					worldIn.scheduleUpdate(pos, worldIn.getBlockState(pos).getBlock(), 160);
				}
			}
		}
		else if (!flag && !this.isBurnedOut(worldIn, pos, false))
		{
			//Dazzle: use lit state
			worldIn.setBlockState(pos, state.withProperty(LIT, true), 3);
		}
	}
	
	/**
	 * Called when a neighboring block was changed and marks that this state should perform any checks during a neighbor
	 * change. Cases may include when redstone power is updated, cactus blocks popping off due to a neighboring solid
	 * block, etc.
	 */
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
	{
		if (!this.onNeighborChangeInternal(worldIn, pos, state))
		{
			if (state.getValue(LIT) == this.shouldBeOff(worldIn, pos, state)) //Dazzle: use lit state
			{
				worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
			}
		}
	}
	
	public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
	{
		return side == EnumFacing.DOWN ? blockState.getWeakPower(blockAccess, pos, side) : 0;
	}
	
	/**
	 * Get the Item that this Block should drop when harvested.
	 */
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return Item.getItemFromBlock(Blocks.REDSTONE_TORCH);
	}
	
	/**
	 * Can this block provide power. Only wire currently seems to have this change based on its state.
	 */
	public boolean canProvidePower(IBlockState state)
	{
		return true;
	}
	
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState state, World worldIn, BlockPos pos, Random rand)
	{
		if (state.getValue(LIT)) //Dazzle: use lit state
		{
			double d0 = (double)pos.getX() + 0.5D + (rand.nextDouble() - 0.5D) * 0.2D;
			double d1 = (double)pos.getY() + 0.7D + (rand.nextDouble() - 0.5D) * 0.2D;
			double d2 = (double)pos.getZ() + 0.5D + (rand.nextDouble() - 0.5D) * 0.2D;
			EnumFacing enumfacing = state.getValue(FACING);
			
			if (enumfacing.getAxis().isHorizontal())
			{
				EnumFacing enumfacing1 = enumfacing.getOpposite();
				//double d3 = 0.27D; //lmao mojang
				d0 += 0.27D * (double)enumfacing1.getFrontOffsetX();
				d1 += 0.22D;
				d2 += 0.27D * (double)enumfacing1.getFrontOffsetZ();
			}
			
			worldIn.spawnParticle(EnumParticleTypes.REDSTONE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
		}
	}
	
	public ItemStack getItem(World world, BlockPos pos, IBlockState state)
	{
		return new ItemStack(this);
	}
	
	//Dazzle: copy from BlockBase which I can't extend
	Item item;
	public Item itemForm() {
		if(item == null) {
			item = new ItemBlock(this);
			item.setRegistryName(getRegistryName());
		}
		return item;
	}
	
	/*
	//Dazzle: remove associated block stuff
	public boolean isAssociatedBlock(Block other)
	{
		return other == Blocks.UNLIT_REDSTONE_TORCH || other == Blocks.REDSTONE_TORCH;
	}
	*/
	
	static class Toggle
	{
		BlockPos pos;
		long time;
		
		public Toggle(BlockPos pos, long time)
		{
			this.pos = pos;
			this.time = time;
		}
	}
}