package me.andante.test.impl;

import me.andante.test.api.Test;
import net.fabricmc.api.ModInitializer;
import net.moddingplayground.frame.api.util.InitializationLogger;

public final class TestImpl implements Test, ModInitializer {
    private final InitializationLogger initializer;

    public TestImpl() {
        this.initializer = new InitializationLogger(LOGGER, MOD_NAME);
    }

    @Override
    public void onInitialize() {
        this.initializer.start();

        //

        this.initializer.finish();
    }
}
