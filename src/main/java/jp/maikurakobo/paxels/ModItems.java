package jp.maikurakobo.paxels;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public final class ModItems {
    private ModItems() {}

    public static final ToolMaterial WOODEN_PAXEL_MATERIAL = doubleDurability(ToolMaterial.WOOD);
    public static final ToolMaterial STONE_PAXEL_MATERIAL = doubleDurability(ToolMaterial.STONE);
    public static final ToolMaterial COPPER_PAXEL_MATERIAL = doubleDurability(ToolMaterial.COPPER);
    public static final ToolMaterial IRON_PAXEL_MATERIAL = doubleDurability(ToolMaterial.IRON);
    public static final ToolMaterial GOLDEN_PAXEL_MATERIAL = doubleDurability(ToolMaterial.GOLD);
    public static final ToolMaterial DIAMOND_PAXEL_MATERIAL = doubleDurability(ToolMaterial.DIAMOND);
    public static final ToolMaterial NETHERITE_PAXEL_MATERIAL = doubleDurability(ToolMaterial.NETHERITE);

    public static final ToolMaterial BEACON_MATERIAL = new ToolMaterial(
            ToolMaterial.NETHERITE.incorrectBlocksForDrops(),
            ToolMaterial.NETHERITE.durability() * 2,
            ToolMaterial.NETHERITE.speed(),
            ToolMaterial.NETHERITE.attackDamageBonus(),
            ToolMaterial.NETHERITE.enchantmentValue(),
            TagKey.of(RegistryKeys.ITEM, Identifier.of(MaikuraPaxels.MOD_ID, "beacon_paxel_no_anvil_repair"))
    );

    public static final Item WOODEN_PAXEL = registerPaxel("wooden_paxel", WOODEN_PAXEL_MATERIAL, 2.0F, 6.0F, -3.2F, false);
    public static final Item STONE_PAXEL = registerPaxel("stone_paxel", STONE_PAXEL_MATERIAL, 4.0F, 7.0F, -3.2F, false);
    public static final Item COPPER_PAXEL = registerPaxel("copper_paxel", COPPER_PAXEL_MATERIAL, 5.0F, 6.0F, -3.1F, false);
    public static final Item IRON_PAXEL = registerPaxel("iron_paxel", IRON_PAXEL_MATERIAL, 6.0F, 6.0F, -3.1F, false);
    public static final Item GOLDEN_PAXEL = registerPaxel("golden_paxel", GOLDEN_PAXEL_MATERIAL, 12.0F, 6.0F, -3.0F, false);
    public static final Item DIAMOND_PAXEL = registerPaxel("diamond_paxel", DIAMOND_PAXEL_MATERIAL, 8.0F, 5.0F, -3.0F, false);
    public static final Item NETHERITE_PAXEL = registerPaxel("netherite_paxel", NETHERITE_PAXEL_MATERIAL, 9.0F, 5.0F, -3.0F, true);
    public static final Item BEACON_PAXEL = registerPaxel("beacon_paxel", BEACON_MATERIAL, 9.0F, 5.0F, -3.0F, true);

    private static ToolMaterial doubleDurability(ToolMaterial base) {
        return new ToolMaterial(
                base.incorrectBlocksForDrops(),
                base.durability() * 2,
                base.speed(),
                base.attackDamageBonus(),
                base.enchantmentValue(),
                base.repairItems()
        );
    }

    private static Item registerPaxel(String id, ToolMaterial material, float miningSpeed, float attackDamage, float attackSpeed, boolean fireproof) {
        Item.Settings settings = new Item.Settings().axe(material, attackDamage, attackSpeed);
        if (fireproof) settings = settings.fireproof();
        return register(id, s -> new PaxelItem(material, miningSpeed, s), settings);
    }

    private static Item register(String path, Function<Item.Settings, Item> factory, Item.Settings settings) {
        RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(MaikuraPaxels.MOD_ID, path));
        return Items.register(key, factory, settings);
    }

    public static void initialize() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
            entries.add(WOODEN_PAXEL);
            entries.add(STONE_PAXEL);
            entries.add(COPPER_PAXEL);
            entries.add(IRON_PAXEL);
            entries.add(GOLDEN_PAXEL);
            entries.add(DIAMOND_PAXEL);
            entries.add(NETHERITE_PAXEL);
            entries.add(BEACON_PAXEL);
        });
    }
}
