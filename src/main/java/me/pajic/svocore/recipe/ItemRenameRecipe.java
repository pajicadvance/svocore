package me.pajic.svocore.recipe;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ItemRenameRecipe extends CustomRecipe {

	private static final ItemRenameRecipe INSTANCE = new ItemRenameRecipe();
	public static final MapCodec<ItemRenameRecipe> MAP_CODEC = MapCodec.unit(INSTANCE);
	public static final StreamCodec<RegistryFriendlyByteBuf, ItemRenameRecipe> STREAM_CODEC = StreamCodec.unit(INSTANCE);

	private Component name;

	@Override
	public boolean matches(@NotNull CraftingInput input, @NotNull Level level) {
		if (input.size() == 2) {
			for (int i = 0; i < input.size(); i++) {
				ItemStack itemStack = input.getItem(i);
				if (!itemStack.isEmpty() && itemStack.is(Items.NAME_TAG)) {
					name = itemStack.getCustomName();
					if (name != null) return true;
				}
			}
		}
		return false;
	}

	@Override
	public @NotNull ItemStack assemble(CraftingInput input) {
		ItemStack itemStack = ItemStack.EMPTY;
		for (int i = 0; i < input.size(); i++) {
			ItemStack itemStack2 = input.getItem(i);
			if (!itemStack2.is(Items.NAME_TAG)) {
				itemStack = itemStack2.copy();
				itemStack.set(DataComponents.CUSTOM_NAME, name);
			}
		}
		return itemStack;
	}

	@Override
	public @NotNull RecipeSerializer<? extends CustomRecipe> getSerializer() {
		return ModRecipes.ITEM_RENAME;
	}
}
