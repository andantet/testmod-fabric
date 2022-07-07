package me.andante.test.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface Test {
    String MOD_ID   = "testmod";
    String MOD_NAME = "Test Mod";
    Logger LOGGER   = LoggerFactory.getLogger(MOD_ID);
}
