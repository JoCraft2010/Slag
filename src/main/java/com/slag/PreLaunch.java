package com.slag;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.foreign.Arena;
import java.lang.foreign.SymbolLookup;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import com.slag.natives.SlangBinder;
import com.slag.natives.SlangNative;

import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

public class PreLaunch implements PreLaunchEntrypoint {

    private String[] getNativeNames() {
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            return new String[] { "slang-compiler.lib", "slang-rt.lib", "slang.lib" };
        } else if (os.contains("nix") || os.contains("nux")) {
            return new String[] { "libslang-rt.so", "libslang-llvm.so", "libslang-compiler.so" };
        }

        throw new RuntimeException("Unsupported OS: " + os);
    }

    private void extractNative(String libName) throws IOException {
        Path tempFile = Slag.TEMP_DIR.resolve(libName);

        try (InputStream is = PreLaunch.class.getResourceAsStream("/natives/" + libName)) {
            if (is == null)
                throw new FileNotFoundException("Library not found in JAR: /natives/" + libName);
            Files.copy(is, tempFile, StandardCopyOption.REPLACE_EXISTING);
        }

        tempFile.toFile().deleteOnExit();
    }

    @Override
    public void onPreLaunch() {
        HealthCheck.checkPanama();

        String[] libs = getNativeNames();
        try {
            for (String lib : libs)
                extractNative(lib);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (String lib : libs)
            System.load(Slag.TEMP_DIR.resolve(lib).toAbsolutePath().toString());

        SymbolLookup lib = SymbolLookup.libraryLookup(Slag.TEMP_DIR.resolve(libs[libs.length - 1]), Arena.global());
        Slag.NATIVE = SlangBinder.bind(SlangNative.class, lib);

        HealthCheck.checkSlang();
    }
}
