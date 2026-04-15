package com.slag;

public class HealthCheck {

    public static void checkPanama() {
        try {
            java.lang.foreign.Linker.nativeLinker();
        } catch (IllegalCallerException e) {
            throw new RuntimeException(
                    "Native access not enabled. Add --enable-native-access=ALL-UNNAMED to your VM args.");
        }
    }

    public static void checkSlang() {
        if (Slag.NATIVE._Z27slang_getEmbeddedCoreModulev() == null)
            throw new RuntimeException("Slang core module could not be loaded.");

        String version = Slag.NATIVE.spGetBuildTagString();
        String lzVersion = Slag.NATIVE.LZ4_versionString();
        String mzVersion = Slag.NATIVE.mz_version();
        Slag.LOGGER.info("Slag built with Slang " + version + ", LZ4 " + lzVersion + " and Miniz " + mzVersion);
    }
}
