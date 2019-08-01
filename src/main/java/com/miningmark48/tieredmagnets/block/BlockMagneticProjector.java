package com.miningmark48.tieredmagnets.block;

import com.miningmark48.tieredmagnets.container.ContainerMagneticProjector;
import com.miningmark48.tieredmagnets.init.ModBlocks;
import com.miningmark48.tieredmagnets.tileentity.TileEntityMagneticProjector;
import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings("Duplicates")
public class BlockMagneticProjector extends ContainerBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    private static final BooleanProperty POWERED = BooleanProperty.create("powered");

    private VoxelShape SHAPE_BASE = Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 6.0D, 15.0D);
    private VoxelShape SHAPE_ACTIVE = VoxelShapes.or(SHAPE_BASE, Block.makeCuboidShape(5.0D, 6.0D, 5.0D, 11.0D, 9.0D, 11.0D));
    private VoxelShape SHAPE_INACTIVE = VoxelShapes.or(SHAPE_BASE, Block.makeCuboidShape(5.0D, 6.0D, 5.0D, 11.0D, 8.25D, 11.0D));

    public BlockMagneticProjector(Properties properties) {
        super(properties);
        this.setDefaultState(this.getStateContainer().getBaseState().with(FACING, Direction.NORTH).with(POWERED, false));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(FACING);
        builder.add(POWERED);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        return !state.get(POWERED) ? SHAPE_ACTIVE : SHAPE_INACTIVE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader p_220071_2_, BlockPos p_220071_3_, ISelectionContext p_220071_4_) {
        return !state.get(POWERED) ? SHAPE_ACTIVE : SHAPE_INACTIVE;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new TileEntityMagneticProjector(ModBlocks.ModTileEntities.MAGNETIC_PROJECTOR);
    }

    @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult ray) {
        if (!player.isSneaking()) {
            if (player instanceof ServerPlayerEntity) {
                TileEntity te = world.getTileEntity(pos);
                if (te instanceof TileEntityMagneticProjector) {
                    NetworkHooks.openGui((ServerPlayerEntity) player, new INamedContainerProvider() {
                        @Nonnull
                        @Override
                        public ITextComponent getDisplayName() {
                            return state.getBlock().getNameTextComponent();
                        }

                        @Nonnull
                        @Override
                        public Container createMenu(int i, @Nonnull PlayerInventory playerInventory, @Nonnull PlayerEntity playerEntity) {
                            return new ContainerMagneticProjector(i, playerInventory, (TileEntityMagneticProjector) te);
                        }
                    }, packetBuffer -> packetBuffer.writeBlockPos(pos));
                }
            }
        }
        return true;
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState state1, boolean p_220082_5_) {
        super.onBlockAdded(state, world, pos, state1, p_220082_5_);
        this.setDefaultFacing(world, pos, state);
        world.setBlockState(pos, state.with(POWERED, world.isBlockPowered(pos)));
    }

    //    @SuppressWarnings("deprecation")
//    @Override
//    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos changedPos)
//    {
//        worldIn.setBlockState(pos, state.withProperty(POWERED, worldIn.isBlockPowered(pos)));
//    }

    private void setDefaultFacing(World worldIn, BlockPos pos, BlockState state)
    {
        if (!worldIn.isRemote) {
            BlockState iblockstate = worldIn.getBlockState(pos.north());
            BlockState iblockstate1 = worldIn.getBlockState(pos.south());
            BlockState iblockstate2 = worldIn.getBlockState(pos.west());
            BlockState iblockstate3 = worldIn.getBlockState(pos.east());
            Direction enumfacing = state.get(FACING);
            if (enumfacing == Direction.NORTH && iblockstate.isSolid() && !iblockstate1.isSolid()) {
                enumfacing = Direction.SOUTH;
            } else if (enumfacing == Direction.SOUTH && iblockstate1.isSolid() && !iblockstate.isSolid()) {
                enumfacing = Direction.NORTH;
            } else if (enumfacing == Direction.WEST && iblockstate2.isSolid() && !iblockstate3.isSolid()) {
                enumfacing = Direction.EAST;
            } else if (enumfacing == Direction.EAST && iblockstate3.isSolid() && !iblockstate2.isSolid()) {
                enumfacing = Direction.WEST;
            }

            worldIn.setBlockState(pos, state.with(FACING, enumfacing), 2);
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlacementHorizontalFacing().getOpposite());
    }

    @Override
    public void onBlockPlacedBy(World p_180633_1_, BlockPos p_180633_2_, BlockState p_180633_3_, @Nullable LivingEntity p_180633_4_, ItemStack p_180633_5_) {
//        worldIn.setBlockState(pos, state.withProperty(FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer)), 2);
    }

//    @SuppressWarnings("deprecation")
//    @Override
//    public IBlockState getStateFromMeta(int meta)
//    {
//        boolean powered = false;
//        if (meta >= 6)
//        {
//            meta -= 6;
//            powered = true;
//        }
//
//        return this.getDefaultState().withProperty(FACING, EnumFacing.values()[meta]).withProperty(POWERED, powered);
//    }
//
//    @Override
//    public int getMetaFromState(IBlockState state)
//    {
//        int i = state.getValue(FACING).getIndex();
//
//        if (state.getValue(POWERED))
//        {
//            i += 6;
//        }
//
//        return i;
//    }

//    @Override
//    protected BlockStateContainer createBlockState()
//    {
//        return new BlockStateContainer(this, FACING, POWERED);
//    }

    public void setState(World worldIn, BlockPos pos, boolean isActive)
    {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        worldIn.setBlockState(pos, worldIn.getBlockState(pos).with(POWERED, isActive), 3);

        if (tileentity != null)
        {
            tileentity.validate();
            worldIn.setTileEntity(pos, tileentity);
        }
    }

    @Override
    public BlockRenderType getRenderType(BlockState state)
    {
        return BlockRenderType.MODEL;
    }

    @Override
    public boolean isSolid(BlockState state){
        return false;
    }

//    @Override
//    public boolean isOpaqueCube(BlockState state){
//        return false;
//    }

    @Override
    public BlockRenderLayer getRenderLayer(){
        return BlockRenderLayer.CUTOUT;
    }

//    @Override
//    public boolean isFullBlock(BlockState state) {
//        return false;
//    }


    @Override
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState state1, boolean p_196243_5_) {
        if (state1.getBlock() == Blocks.AIR) {
            TileEntity tileentity = world.getTileEntity(pos);
            if (tileentity instanceof IInventory) {
                InventoryHelper.dropInventoryItems(world, pos, (IInventory) tileentity);
                world.updateComparatorOutputLevel(pos, this);
            }
        }

        super.onReplaced(state, world, pos, state1, p_196243_5_);
    }

}
