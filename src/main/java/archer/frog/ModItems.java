package archer.frog;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistryEvents;
import net.minecraft.component.type.ConsumableComponents;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.consume.ApplyEffectsConsumeEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class ModItems {

    public static final Item FROG_ESSENCE = register("frog_essence", Item::new, new Item.Settings());
    public static final Item FROG_LEGS = register("frog_legs", Item::new, frogLegSettings());

    public static final RegistryKey<ItemGroup> CUSTOM_ITEM_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of(FrogModInitializer.MOD_ID, "item_group"));
    public static final ItemGroup CUSTOM_ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(ModItems.FROG_ESSENCE))
            .displayName(Text.translatable("itemGroup.frog"))
            .build();

    public static void initialize() {
        initializeItemGroup();
        initializeFrogEssence();
        initializeFrogLegs();
        new Item.Settings().food(new FoodComponent.Builder().alwaysEdible().build());
    }

    private static void initializeItemGroup() {

        Registry.register(Registries.ITEM_GROUP, CUSTOM_ITEM_GROUP_KEY, CUSTOM_ITEM_GROUP);

        ItemGroupEvents.modifyEntriesEvent(CUSTOM_ITEM_GROUP_KEY).register(itemGroup -> {
            itemGroup.add(ModItems.FROG_ESSENCE);
            itemGroup.add(ModItems.FROG_LEGS);
            // ...
        });
    }

    private static void initializeFrogEssence() {
        // ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS)
        // .register((itemGroup) -> itemGroup.add(ModItems.FROG_ESSENCE));
        CompostingChanceRegistry.INSTANCE.add(ModItems.FROG_ESSENCE, 0.3f);
        // TODO: Figure out how to look at the FuelRegistryEvents.BuildCallback Interface???
        FuelRegistryEvents.BUILD.register((builder, context) -> builder.add(ModItems.FROG_ESSENCE, 30 * 20));
    }

    private static Item.Settings frogLegSettings() {
        int nutrition = 2;
        float hungerProbability = 0.5f;
        int hungerDurationTicks = 10 * 20;

        return new Item.Settings()
            .food(
                    new FoodComponent.Builder().nutrition(nutrition).build(),
                    ConsumableComponents.food().consumeEffect(new ApplyEffectsConsumeEffect(new StatusEffectInstance(StatusEffects.HUNGER, hungerDurationTicks), hungerProbability)).build()
            );
    }
    private static void initializeFrogLegs() {
    }

    private static Item register(String name, Function<Item.Settings, Item> itemFactory, Item.Settings settings) {
        // Create the item key.
        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(FrogModInitializer.MOD_ID, name));

        // Create the item instance.
        Item item = itemFactory.apply(settings.registryKey(itemKey));

        // Register the item.
        Registry.register(Registries.ITEM, itemKey, item);

        return item;
    }
}
