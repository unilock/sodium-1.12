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
    private Fluid sodium$forgeFluid;

    @Override
    public Fluid getFluid() {
        Fluid fluid = sodium$forgeFluid;
        if(fluid == null) {
            Block block = ((Block) (Object) this);
            sodium$forgeFluid = fluid = block.getDefaultState().getMaterial() == Material.WATER ? FluidRegistry.WATER : FluidRegistry.LAVA;
        }
        return fluid;
    }

    @Override
    public float getFilledPercentage(World world, BlockPos pos) {
        IBlockState blockState = world.getBlockState(pos);
        return getFluid() == null ? 0 : 1 - BlockLiquid.getLiquidHeightPercent(blockState.getValue(BlockLiquid.LEVEL));
    }
}