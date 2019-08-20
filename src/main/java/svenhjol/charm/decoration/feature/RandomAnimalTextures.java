package svenhjol.charm.decoration.feature;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import svenhjol.charm.base.CharmCategories;
import svenhjol.charm.decoration.render.CustomWolfRenderer;
import svenhjol.meson.Feature;
import svenhjol.meson.iface.IMesonEnum;
import svenhjol.meson.iface.MesonLoadModule;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@MesonLoadModule(category = CharmCategories.DECORATION)
public class RandomAnimalTextures extends Feature
{
    public static List<String> wolves = Arrays.asList(
        "minecraft:wolf",
        "charm:brownwolf",
        "charm:greywolf",
        "charm:blackwolf",
        "charm:amotwolf",
        "charm:jupiter1390"
    );

    public enum MobType implements IMesonEnum
    {
        WOLF
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void setupClient(FMLClientSetupEvent event)
    {
        super.setupClient(event);

        //noinspection unchecked
        RenderingRegistry.registerEntityRenderingHandler(WolfEntity.class, CustomWolfRenderer.factory());
    }

    @OnlyIn(Dist.CLIENT)
    public static ResourceLocation getWolfTexture(WolfEntity entity)
    {
        String texture = getRandomTexture(entity, wolves);
        if (entity.isTamed()) {
            texture += "_tame";
        } else if (entity.isAngry()) {
            texture += "_angry";
        }

        return getTextureFromString(MobType.WOLF, texture);
    }

    public static String getRandomTexture(Entity entity, List<String> set)
    {
        UUID id = entity.getUniqueID();
        int choice = Math.abs( (int)(id.getMostSignificantBits() % set.size()) );
        return set.get(choice);
    }

    public static ResourceLocation getTextureFromString(MobType type, String texture)
    {
        String prefix = "textures/entity/" + type.getName() + "/";
        String[] a = texture.split(":");
        return new ResourceLocation(a[0], prefix + a[1] + ".png");
    }
}
