package svenhjol.charm.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import svenhjol.charm.base.helper.ClientHelper;
import svenhjol.charm.blockentity.CaskBlockEntity;
import svenhjol.charm.client.StorageLabelsClient;
import svenhjol.charm.module.StorageLabels;

import java.util.Optional;

@Environment(EnvType.CLIENT)
public class CaskBlockEntityRenderer<T extends CaskBlockEntity> implements BlockEntityRenderer<T> {
    private final BlockEntityRendererFactory.Context context;

    public CaskBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        this.context = context;
    }

    @Override
    public boolean rendersOutsideBoundingBox(T blockEntity) {
        return true;
    }

    @Override
    public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (entity == null)
            return;

        if (entity.name == null || entity.name.isEmpty())
            return;

        Optional<PlayerEntity> optPlayer = ClientHelper.getPlayer();
        if (!optPlayer.isPresent())
            return;

        PlayerEntity player = optPlayer.get();
        LiteralText text = new LiteralText(entity.name);
        BlockEntityRenderDispatcher dispatcher = context.getRenderDispatcher();
        Camera camera = dispatcher.camera;

        double distance = ClientHelper.getBlockEntityDistance(player, entity, camera);

        if (distance < StorageLabels.VIEW_DISTANCE)
            StorageLabelsClient.renderLabel(matrices, vertexConsumers, player, camera, text);
    }
}
