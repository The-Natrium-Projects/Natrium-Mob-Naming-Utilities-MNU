package net.sodiumzh.nmd.registry;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sodiumzh.nmd.NatriumMobDisplay;
import net.sodiumzh.nmd.item.BossBarShowingTagItem;
import net.sodiumzh.nmd.item.BossBarHidingTagItem;
import net.sodiumzh.nmd.item.CustomNameRemoverItem;

public class NMDItems
{
	public static final DeferredRegister<Item> ITEMS =  DeferredRegister.create(ForgeRegistries.ITEMS, NatriumMobDisplay.MOD_ID);
	
	public static final RegistryObject<BossBarHidingTagItem> BOSS_BAR_HIDING_TAG = 
			ITEMS.register("boss_bar_hiding_tag", () -> new BossBarHidingTagItem(new Item.Properties().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<BossBarShowingTagItem> BOSS_BAR_SHOWING_TAG = 
			ITEMS.register("boss_bar_showing_tag", () -> new BossBarShowingTagItem(new Item.Properties().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<CustomNameRemoverItem> NAME_REMOVER =
		ITEMS.register("name_remover", () -> new CustomNameRemoverItem(new Item.Properties()));


}
