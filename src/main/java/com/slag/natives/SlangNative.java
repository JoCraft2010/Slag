package com.slag.natives;

import java.lang.foreign.MemorySegment;

public interface SlangNative {

    String LZ4_versionString();

    String mz_version();

    String spGetBuildTagString();

    @NativeSymbol("_Z27slang_getEmbeddedCoreModulev")
    MemorySegment slang_getEmbeddedCoreModule();
}
