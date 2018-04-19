package quaternary.dazzle.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import quaternary.dazzle.block.statemapper.RenamedIgnoringStatemapper;
import quaternary.dazzle.compat.shaderlights.*;
import quaternary.dazzle.item.ItemBlockLamp;

import java.util.Random;

public class BlockDigitalLamp extends BlockLamp implements IDazzleStaticLight {
	public static final PropertyBool LIT = PropertyBool.create("lit");
	public static final PropertyBool INVERTED = PropertyBool.create("inverted");
	
	public BlockDigitalLamp(EnumDyeColor color, String variant) {
		super("digital_lamp", color, variant);
		
		setDefaultState(getDefaultState().withProperty(LIT, false).withProperty(INVERTED, false));
	}
	
	Item item;
	@Override
	public Item getItemForm() {
		if(item == null) {
			item = new ItemBlockLamp(this, color, variant, "tile.dazzle.digital_lamp.name");
		}
		return item;
	}
	
	@Override
	int getBrightnessFromState(IBlockState state) {
		return (state.getValue(LIT) ^ state.getValue(INVERTED)) ? 15 : 0;
	}
	
	@Override
	IBlockState setStateBrightness(IBlockState state, int powerLevel) {
		return state.withProperty(LIT, powerLevel != 0);
	}
	
	@Override
	IBlockState getInvertedState(IBlockState in) {
		return in.withProperty(INVERTED, !in.getValue(INVERTED));
	}
	
	/*
	//Shader light support.
	//"quat, why the hell are you overriding randomDisplayTick?"
	//The vanilla *client* already calls this code randomly for nearby blocks.
	//It's random, but somewhat workable for this use case.
	//As long as you don't get that far away from the lights, it's okay.
	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState state, World world, BlockPos mutablePos, Random rand) {
		BlockPos pos = mutablePos.toImmutable();
		
		StaticShaderLightManager sslm = ColoredLightingMods.getStaticLightManager();
		if(!sslm.has(pos)) {
			WrappedLight w = new WrappedLight(pos, color.getColorValue(), .6f, 15);
			sslm.put(pos, w);
		}
	}
	*/
	
	//Blockstate boilerplate
	@Override
	public int getMetaFromState(IBlockState state) {
		return (state.getValue(LIT) ? 1 : 0) | (state.getValue(INVERTED) ? 2 : 0);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(LIT, (meta & 1) != 0).withProperty(INVERTED, (meta & 2) != 0);
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, LIT, INVERTED);
	}
	
	//Statemapper
	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasCustomStatemapper() {
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IStateMapper getCustomStatemapper() {
		return new RenamedIgnoringStatemapper("lamp_" + variant);
	}
}
