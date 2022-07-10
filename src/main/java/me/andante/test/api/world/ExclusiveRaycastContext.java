package me.andante.test.api.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.RaycastContext;

public class ExclusiveRaycastContext extends RaycastContext {
    private final Block block;

    public ExclusiveRaycastContext(Vec3d start, Vec3d end, Entity entity, Block block) {
        super(start, end, ShapeType.OUTLINE, FluidHandling.NONE, entity);
        this.block = block;
    }

    @Override
    public VoxelShape getBlockShape(BlockState state, BlockView world, BlockPos pos) {
        return state.getBlock() == this.block && !state.isOpaque() ? VoxelShapes.empty() : super.getBlockShape(state, world, pos);
    }
}
