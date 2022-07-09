package me.andante.test.impl;

import com.google.common.reflect.Reflection;
import me.andante.test.api.Test;
import me.andante.test.api.client.config.TestConfig;
import net.fabricmc.api.ModInitializer;
import net.moddingplayground.frame.api.util.InitializationLogger;

public final class TestImpl implements Test, ModInitializer {
    private final InitializationLogger initializer;

    public TestImpl() {
        this.initializer = new InitializationLogger(LOGGER, MOD_NAME);
    }

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public void onInitialize() {
        this.initializer.start();
        Reflection.initialize(TestConfig.class);
        this.initializer.finish();
    }
}
