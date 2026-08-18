package net.sodiumzh.naming.registry;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sodiumzh.naming.MobNamingUtilities;
import net.sodiumzh.naming.item.BossBarShowingTagItem;
import net.sodiumzh.naming.item.BossBarHidingTagItem;
import net.sodiumzh.naming.item.CustomNameRemoverItem;

public class MNUItems
{
	public static final DeferredRegister<Item> ITEMS =  DeferredRegister.create(ForgeRegistries.ITEMS, MobNamingUtilities.MOD_ID);
	
	public static final RegistryObject<BossBarHidingTagItem> BOSS_BAR_HIDING_TAG = 
			ITEMS.register("boss_bar_hiding_tag", () -> new BossBarHidingTagItem(new Item.Properties().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<BossBarShowingTagItem> BOSS_BAR_SHOWING_TAG = 
			ITEMS.register("boss_bar_showing_tag", () -> new BossBarShowingTagItem(new Item.Properties().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<CustomNameRemoverItem> NAME_REMOVER =
		ITEMS.register("name_remover", () -> new CustomNameRemoverItem(new Item.Properties()));


}
