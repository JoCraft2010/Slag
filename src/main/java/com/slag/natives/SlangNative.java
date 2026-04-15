package com.slag.natives;

import java.lang.foreign.MemorySegment;

public interface SlangNative {

    String LZ4_versionString();

    String mz_version();

    String spGetBuildTagString();

    MemorySegment _Z27slang_getEmbeddedCoreModulev();
}
