package svenhjol.charm.client;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import org.lwjgl.glfw.GLFW;
import svenhjol.charm.base.CharmClientModule;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.init.CharmResources;
import svenhjol.charm.init.CharmTags;
import svenhjol.charm.base.helper.ScreenHelper;
import svenhjol.charm.event.SetupGuiCallback;
import svenhjol.charm.event.RenderGuiCallback;
import svenhjol.charm.mixin.accessor.PlayerEntityAccessor;
import svenhjol.charm.mixin.accessor.ScreenAccessor;
import svenhjol.charm.module.PortableCrafting;

import java.util.List;
import java.util.function.Consumer;

public class PortableCraftingClient extends CharmClientModule {
    public TexturedButtonWidget craftingButton;
    public static KeyBinding keyBinding;

    public PortableCraftingClient(CharmModule module) {
        super(module);
    }

    @Override
    public void init() {
        // set up client listeners
        SetupGuiCallback.EVENT.register(this::handleGuiSetup);
        RenderGuiCallback.EVENT.register(this::handleRenderGui);

        if (PortableCrafting.enableKeybind) {
            keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.charm.openCraftingTable",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_V,
                "key.categories.inventory"
            ));

            ClientTickEvents.END_WORLD_TICK.register(world -> {
                if (keyBinding == null || world == null)
                    return;

                while (keyBinding.wasPressed()) {
                    triggerOpenCraftingTable();
                }
            });
        }
    }

    private void handleGuiSetup(MinecraftClient client, int width, int height, List<ClickableWidget> buttons) {
        if (client.player == null)
            return;

        if (!(client.currentScreen instanceof InventoryScreen))
            return;

        InventoryScreen screen = (InventoryScreen)client.currentScreen;
        int guiLeft = ScreenHelper.getX(screen);

        this.craftingButton = new TexturedButtonWidget(guiLeft + 130, height / 2 - 22, 20, 18, 0, 0, 19, CharmResources.INVENTORY_BUTTONS, click -> {
            triggerOpenCraftingTable();
        });

        this.craftingButton.visible = hasCrafting(client.player);
        ((ScreenAccessor)screen).invokeAddButton(this.craftingButton);
    }

    private void handleRenderGui(MinecraftClient client, MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (!(client.currentScreen instanceof InventoryScreen)
            || this.craftingButton == null
            || client.player == null
        ) {
            return;
        }

        if (client.player.world.getTime() % 5 == 0)
            this.craftingButton.visible = hasCrafting(client.player);
    }

    private boolean hasCrafting(PlayerEntity player) {
        return ((PlayerEntityAccessor)player).getInventory().contains(CharmTags.CRAFTING_TABLES);
    }

    private void triggerOpenCraftingTable() {
        ClientPlayNetworking.send(PortableCrafting.MSG_SERVER_OPEN_CRAFTING, new PacketByteBuf(Unpooled.buffer()));
    }

    public boolean isButtonVisible() {
        return this.craftingButton != null && this.craftingButton.visible;
    }
}
