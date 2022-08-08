package me.andante.test.impl.client;

import com.google.common.reflect.Reflection;
import me.andante.test.api.Test;
import me.andante.test.api.client.config.TestClientConfig;
import net.fabricmc.api.ClientModInitializer;

public final class TestClientImpl implements Test, ClientModInitializer {
    @SuppressWarnings("UnstableApiUsage")
    @Override
    public void onInitializeClient() {
        Reflection.initialize(TestClientConfig.class);
    }
}
