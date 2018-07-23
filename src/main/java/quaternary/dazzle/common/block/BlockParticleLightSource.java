package quaternary.dazzle.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.*;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import quaternary.dazzle.common.DazzleCreativeTab;
import quaternary.dazzle.common.item.ItemParticleLight;
import quaternary.dazzle.common.tile.TileParticleLightSource;

import javax.annotation.Nullable;
import java.util.Collections;

public class BlockParticleLightSource extends Block {
	public static final PropertyEnum<EnumDyeColor> COLOR = PropertyEnum.create("color", EnumDyeColor.class);
	public static final AxisAlignedBB AABB = new AxisAlignedBB(4/16d, 4/16d, 4/16d, 12/16d, 12/16d, 12/16d);
	
	public BlockParticleLightSource() {
		//The closest thing to Material.AIR that isn't.
		super(Material.STRUCTURE_VOID);
		
		setDefaultState(getDefaultState().withProperty(COLOR, EnumDyeColor.WHITE));
	}
	
	//selection box
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return AABB;
	}
	
	//metadata memes
	@Override
	public int damageDropped(IBlockState state) {
		return state.getValue(COLOR).getMetadata();
	}
	
	//tile
	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}
	
	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileParticleLightSource();
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
	
	//Light level
	@Override
	public int getLightValue(IBlockState state) {
		return 15;
	}
	
	//Make it invisible
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.INVISIBLE;
	}
	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	@Nullable
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return NULL_AABB;
	}
	
	//Yeet
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}
}
