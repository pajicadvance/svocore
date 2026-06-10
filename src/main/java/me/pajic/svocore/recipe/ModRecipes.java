package me.pajic.svocore.recipe;

import me.pajic.svocore.SVO;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class ModRecipes {

	public static RecipeSerializer<ItemRenameRecipe> ITEM_RENAME = new RecipeSerializer<>(ItemRenameRecipe.MAP_CODEC, ItemRenameRecipe.STREAM_CODEC);

	public static void init() {
		Registry.register(
				BuiltInRegistries.RECIPE_SERIALIZER,
				SVO.id("crafting_special_item_rename"),
				ModRecipes.ITEM_RENAME
		);
	}
}
