package me.andante.nochickenrender;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import org.lwjgl.glfw.GLFW;

public class NoChickenRender implements ClientModInitializer {
    public static final KeyBinding KEY_BINDING = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.nochickenrender.toggle", GLFW.GLFW_KEY_UNKNOWN, "key.nochickenrender"));

    public static boolean ENABLED = true;

    @Override
    public void onInitializeClient() {
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (KEY_BINDING.wasPressed()) {
                ENABLED = !ENABLED;
            }
        });
    }
}
