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
}
