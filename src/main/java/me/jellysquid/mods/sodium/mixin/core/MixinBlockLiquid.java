package me.jellysquid.mods.sodium.mixin.core;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.IFluidBlock;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockLiquid.class)
public abstract class MixinBlockLiquid implements IFluidBlock {

    @Override
    public Fluid getFluid() {
        Block block = ((Block) (Object) this);
        return block.getMaterial(block.getDefaultState()) == Material.WATER ? FluidRegistry.WATER : FluidRegistry.LAVA;
    }

    @Override
    public float getFilledPercentage(World world, BlockPos pos) {
        IBlockState blockState = world.getBlockState(pos);
        return getFluid() == null ? 0 : 1 - BlockLiquid.getLiquidHeightPercent(blockState.getValue(BlockLiquid.LEVEL));
    }
}