package com.slag;

import net.fabricmc.api.ModInitializer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.slag.natives.SlangNative;

public class Slag implements ModInitializer {
	public static final String MOD_ID = "slag";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final Path TEMP_DIR;
	public static SlangNative NATIVE;

	static {
		try {
			TEMP_DIR = Files.createTempDirectory(MOD_ID);
		} catch (IOException e) {
			throw new ExceptionInInitializerError("Failed to create temp dir: " + e);
		}
	}

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");
	}
}