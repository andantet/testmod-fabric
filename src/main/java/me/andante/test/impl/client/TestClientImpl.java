package me.andante.test.impl.client;

import com.google.common.reflect.Reflection;
import me.andante.test.api.Test;
import me.andante.test.api.client.config.TestClientConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.moddingplayground.frame.api.util.InitializationLogger;

public final class TestClientImpl implements Test, ClientModInitializer {
    private final InitializationLogger initializer;

    public TestClientImpl() {
        this.initializer = new InitializationLogger(LOGGER, MOD_NAME, EnvType.CLIENT);
    }

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public void onInitializeClient() {
        this.initializer.start();
        Reflection.initialize(TestClientConfig.class);
        this.initializer.finish();
    }
}
