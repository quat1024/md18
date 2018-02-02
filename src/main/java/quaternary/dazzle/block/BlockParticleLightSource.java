package quaternary.dazzle.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import quaternary.dazzle.Dazzle;
import quaternary.dazzle.DazzleCreativeTab;
import quaternary.dazzle.item.ItemParticleLight;
import quaternary.dazzle.tile.TileParticleLightSource;

import javax.annotation.Nullable;

//based on copypasta of BlockInvisibleLightSource
//TODO make it not copypasta
public class BlockParticleLightSource extends BlockBase {
	public static final PropertyEnum<EnumDyeColor> COLOR = PropertyEnum.create("color", EnumDyeColor.class);
	
	public BlockParticleLightSource() {
		//                                vvv The closest thing to Material.AIR that isn't.
		super("particle_light_source", Material.STRUCTURE_VOID);
		
		setDefaultState(getDefaultState().withProperty(COLOR, EnumDyeColor.WHITE));
	}
	
	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
		if(tab == DazzleCreativeTab.INST) {
			for(int i=0; i < EnumDyeColor.values().length; i++) {
				items.add(new ItemStack(getItemForm(), 1, i));
			}
		}
	}
	
	ItemParticleLight item;
	@Override
	public Item getItemForm() {
		if(item == null) {
			item = new ItemParticleLight(this);
		}
		return item;
	}
	
	//tile
	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}
	
	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileParticleLightSource(state.getValue(COLOR));
	}
	
	//meta
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(COLOR, EnumDyeColor.values()[meta]);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(COLOR).ordinal();
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, COLOR);
	}
	
	//signals to BlockBase
	@Override
	public boolean hasCustomStatemapper() {
		return true;
	}
	
	@Override
	public Object getCustomStatemapper() {
		return Dazzle.PROXY.getEmptyStatemapper();
	}
	
	//Light level
	@Override
	public int getLightValue(IBlockState state) {
	    //Check for client side and mod lights so normal lights don't render on the client, but still prevent mob spawns
        //on the server side.
		if(FMLCommonHandler.instance().getEffectiveSide().isClient()
                && Dazzle.instance.getAlbedoCompat().getLightsEnabled()) {
            return 0;
        }
		return 15;
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
	
	@Override
	public boolean isAir(IBlockState state, IBlockAccess world, BlockPos pos) {
		return true;
	}
	
	@Override //VERY GOOD MOJANGLE
	public boolean isBlockNormalCube(IBlockState state) {
		return false;
	}
	
	//Make it non collideable
	public boolean canCollideCheck(IBlockState state, boolean hitIfLiquid)
	{
		return false;
	}
	
	@Nullable
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return NULL_AABB;
	}
	
	//Uhh
	@Override
	public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos)
	{
		return true;
	}
}
