package me.andante.test.api.client.config;

import me.andante.test.api.Test;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.moddingplayground.frame.api.config.v0.Config;
import net.moddingplayground.frame.api.config.v0.option.BooleanOption;
import net.moddingplayground.frame.api.config.v0.option.Option;

import java.io.File;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.*;

@Environment(EnvType.CLIENT)
public class TestClientConfig extends Config {
    private static final String RELOAD_KEY = "command." + Test.MOD_ID + ":config.reload";

    public static final TestClientConfig INSTANCE = new TestClientConfig(createFile("testmod")).load();

    public final BooleanOption versionOnlyDebugHud = add("version_only_debug_hud", BooleanOption.of(true));
    public final BooleanOption animationsStopOnPause = add("animations_stop_on_pause", BooleanOption.of(true));
    public final BooleanOption waila = add("waila", BooleanOption.of(true));

    public TestClientConfig(File file) {
        super(file);

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(
                literal(Test.MOD_ID + ":config")
                    .then(
                        literal("reload")
                            .executes(context -> {
                                this.load();
                                context.getSource().sendFeedback(Text.translatable(RELOAD_KEY));
                                return 1;
                            })
                    )
            );
        });
    }

    private <T, O extends Option<T>> O add(String id, O option) {
        return this.add(new Identifier(Test.MOD_ID, id), option);
    }
}
