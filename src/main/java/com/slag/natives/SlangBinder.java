package com.slag.natives;

import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SymbolLookup;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.Proxy;
import java.nio.charset.StandardCharsets;

public class SlangBinder {

    public static <T> T bind(Class<T> clazz, SymbolLookup lookup) {
        return clazz
                .cast(Proxy.newProxyInstance(clazz.getClassLoader(), new Class[] { clazz }, (proxy, method, args) -> {
                    @SuppressWarnings("null")
                    NativeSymbol symbolAnno = method.getAnnotation(NativeSymbol.class);
                    String symbolName = (symbolAnno != null) ? symbolAnno.value() : method.getName();

                    MemorySegment symbol = lookup.findOrThrow(symbolName);

                    Class<?>[] params = method.getParameterTypes();
                    MemoryLayout[] argLayouts = new MemoryLayout[params.length];
                    for (int i = 0; i < params.length; i++)
                        argLayouts[i] = (params[i] == int.class) ? ValueLayout.JAVA_INT : ValueLayout.ADDRESS;

                    Class<?> retType = method.getReturnType();
                    MemoryLayout retLayout = (retType == int.class) ? ValueLayout.JAVA_INT : ValueLayout.ADDRESS;

                    MethodHandle handle = Linker.nativeLinker().downcallHandle(symbol,
                            FunctionDescriptor.of(retLayout, argLayouts));
                    Object result = handle.invokeWithArguments(args);

                    if (retType == String.class && result instanceof MemorySegment ptr)
                        return ptr.reinterpret(Long.MAX_VALUE).getString(0, StandardCharsets.UTF_8);

                    return result;
                }));
    }
}
