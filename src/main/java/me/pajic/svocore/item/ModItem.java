package me.pajic.svocore.item;

import me.pajic.svocore.SVO;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.NonNull;

public class ModItem extends Item {

    private final boolean enabled;

    public ModItem(Properties properties, String id, boolean enabled) {
        super(properties.setId(ResourceKey.create(Registries.ITEM, SVO.id(id))));
        this.enabled = enabled;
    }

    @Override
    public boolean isEnabled(@NonNull FeatureFlagSet featureFlagSet) {
        return enabled;
    }
}
