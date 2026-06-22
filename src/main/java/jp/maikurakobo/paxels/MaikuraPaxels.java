package jp.maikurakobo.paxels;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropBlock;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

import java.util.Map;

public class MaikuraPaxels implements ModInitializer {
    public static final String MOD_ID = "maikura_paxels";

    private static final Map<Block, Block> STRIPPED_BLOCKS = Map.ofEntries(
            Map.entry(Blocks.OAK_LOG, Blocks.STRIPPED_OAK_LOG),
            Map.entry(Blocks.SPRUCE_LOG, Blocks.STRIPPED_SPRUCE_LOG),
            Map.entry(Blocks.BIRCH_LOG, Blocks.STRIPPED_BIRCH_LOG),
            Map.entry(Blocks.JUNGLE_LOG, Blocks.STRIPPED_JUNGLE_LOG),
            Map.entry(Blocks.ACACIA_LOG, Blocks.STRIPPED_ACACIA_LOG),
            Map.entry(Blocks.DARK_OAK_LOG, Blocks.STRIPPED_DARK_OAK_LOG),
            Map.entry(Blocks.MANGROVE_LOG, Blocks.STRIPPED_MANGROVE_LOG),
            Map.entry(Blocks.CHERRY_LOG, Blocks.STRIPPED_CHERRY_LOG),
            Map.entry(Blocks.PALE_OAK_LOG, Blocks.STRIPPED_PALE_OAK_LOG),
            Map.entry(Blocks.BAMBOO_BLOCK, Blocks.STRIPPED_BAMBOO_BLOCK),
            Map.entry(Blocks.CRIMSON_STEM, Blocks.STRIPPED_CRIMSON_STEM),
            Map.entry(Blocks.WARPED_STEM, Blocks.STRIPPED_WARPED_STEM),
            Map.entry(Blocks.OAK_WOOD, Blocks.STRIPPED_OAK_WOOD),
            Map.entry(Blocks.SPRUCE_WOOD, Blocks.STRIPPED_SPRUCE_WOOD),
            Map.entry(Blocks.BIRCH_WOOD, Blocks.STRIPPED_BIRCH_WOOD),
            Map.entry(Blocks.JUNGLE_WOOD, Blocks.STRIPPED_JUNGLE_WOOD),
            Map.entry(Blocks.ACACIA_WOOD, Blocks.STRIPPED_ACACIA_WOOD),
            Map.entry(Blocks.DARK_OAK_WOOD, Blocks.STRIPPED_DARK_OAK_WOOD),
            Map.entry(Blocks.MANGROVE_WOOD, Blocks.STRIPPED_MANGROVE_WOOD),
            Map.entry(Blocks.CHERRY_WOOD, Blocks.STRIPPED_CHERRY_WOOD),
            Map.entry(Blocks.PALE_OAK_WOOD, Blocks.STRIPPED_PALE_OAK_WOOD),
            Map.entry(Blocks.CRIMSON_HYPHAE, Blocks.STRIPPED_CRIMSON_HYPHAE),
            Map.entry(Blocks.WARPED_HYPHAE, Blocks.STRIPPED_WARPED_HYPHAE)
    );

    @Override
    public void onInitialize() {
        ModItems.initialize();
        registerBlockUseBehavior();
        registerBeaconPaxelHaste();
    }

    private static void registerBlockUseBehavior() {
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            ItemStack stack = player.getStackInHand(hand);
            if (!(stack.getItem() instanceof PaxelItem)) return ActionResult.PASS;

            BlockPos pos = hitResult.getBlockPos();
            BlockState state = world.getBlockState(pos);

            BlockState result = null;
            if (player.isSneaking()) {
                result = getTillableState(world, pos, state);
            } else {
                result = getStrippedState(state);
                if (result == null) {
                    result = getPathState(state);
                }
            }

            if (result == null) return ActionResult.PASS;
            if (!world.isClient()) {
                world.setBlockState(pos, result, Block.NOTIFY_ALL);
                world.playSound(null, pos, player.isSneaking() ? SoundEvents.ITEM_HOE_TILL : SoundEvents.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1.0F, 1.0F);
                stack.damage(1, player, EquipmentSlot.MAINHAND);
                player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
            }
            return ActionResult.SUCCESS;
        });
    }

    private static BlockState getStrippedState(BlockState state) {
        Block stripped = STRIPPED_BLOCKS.get(state.getBlock());
        return stripped == null ? null : stripped.getDefaultState().with(net.minecraft.state.property.Properties.AXIS, state.get(net.minecraft.state.property.Properties.AXIS));
    }

    private static BlockState getPathState(BlockState state) {
        if (state.isOf(Blocks.GRASS_BLOCK) || state.isOf(Blocks.DIRT) || state.isOf(Blocks.PODZOL) || state.isOf(Blocks.COARSE_DIRT) || state.isOf(Blocks.MYCELIUM) || state.isOf(Blocks.ROOTED_DIRT)) {
            return Blocks.DIRT_PATH.getDefaultState();
        }
        return null;
    }

    private static BlockState getTillableState(World world, BlockPos pos, BlockState state) {
        if (!world.getBlockState(pos.up()).isAir()) return null;
        if (state.isOf(Blocks.GRASS_BLOCK) || state.isOf(Blocks.DIRT) || state.isOf(Blocks.DIRT_PATH)) {
            return Blocks.FARMLAND.getDefaultState();
        }
        if (state.isOf(Blocks.COARSE_DIRT)) {
            return Blocks.DIRT.getDefaultState();
        }
        if (state.isOf(Blocks.ROOTED_DIRT)) {
            return Blocks.DIRT.getDefaultState();
        }
        return null;
    }

    private static void registerBeaconPaxelHaste() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                if (player.getMainHandStack().isOf(ModItems.BEACON_PAXEL)) {
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, 40, 0, true, false, true));
                }
            }
        });
    }
}
