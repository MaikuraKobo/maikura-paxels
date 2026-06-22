package jp.maikurakobo.paxels;

import net.minecraft.block.BlockState;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.function.Consumer;

public class PaxelItem extends Item {
    private final ToolMaterial material;
    private final float miningSpeed;

    public PaxelItem(ToolMaterial material, float miningSpeed, Settings settings) {
        super(settings);
        this.material = material;
        this.miningSpeed = miningSpeed;
    }


    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        super.appendTooltip(stack, context, displayComponent, textConsumer, type);
        textConsumer.accept(Text.translatable("tooltip.maikura_paxels.right_click").formatted(Formatting.GRAY));
        textConsumer.accept(Text.translatable("tooltip.maikura_paxels.sneak_right_click").formatted(Formatting.GRAY));
        if (stack.isOf(ModItems.BEACON_PAXEL)) {
            textConsumer.accept(Text.empty());
            textConsumer.accept(Text.translatable("tooltip.maikura_paxels.beacon_haste").formatted(Formatting.AQUA));
        }
    }

    @Override
    public float getMiningSpeed(ItemStack stack, BlockState state) {
        if (isPaxelEffective(state)) {
            return miningSpeed;
        }
        return super.getMiningSpeed(stack, state);
    }

    @Override
    public boolean isCorrectForDrops(ItemStack stack, BlockState state) {
        if (isPaxelEffective(state)) {
            return !state.isIn(material.incorrectBlocksForDrops());
        }
        return super.isCorrectForDrops(stack, state);
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (!world.isClient() && state.getHardness(world, pos) != 0.0F) {
            stack.damage(1, miner, EquipmentSlot.MAINHAND);
        }
        return true;
    }

    @Override
    public void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damage(2, attacker, EquipmentSlot.MAINHAND);
    }

    private static boolean isPaxelEffective(BlockState state) {
        return state.isIn(BlockTags.PICKAXE_MINEABLE)
                || state.isIn(BlockTags.AXE_MINEABLE)
                || state.isIn(BlockTags.SHOVEL_MINEABLE)
                || state.isIn(BlockTags.HOE_MINEABLE);
    }
}
