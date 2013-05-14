package com.junfengdai.compiler;

import com.junfengdai.ClassBytesMapClassLoader;

import javax.tools.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static java.lang.String.format;
import static java.util.Arrays.asList;

public class BlowPermSizeWithCompilerExample {
    private static final String FAKE_CLASS_NAME = "MyFakeCompilerClassName";
    private static final int MAX_CLASS_COUNT = 10000000;
    private static final int MAX_METHOD_COUNT = 10;
    private static final String FAKE_METHOD_NAME = "M";
    private final ClassBytesMapClassLoader loader = new ClassBytesMapClassLoader();
    private final List<Class> clazz = newArrayList();
    private final Map<String, ByteArrayJavaClass> byteArrayJavaClassMap = newHashMap();
    private final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    private final DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();

    public static void main(String[] args) throws IOException {
        BlowPermSizeWithCompilerExample example = new BlowPermSizeWithCompilerExample();
        example.work();
    }

    private void work() throws IOException {
        JavaFileManager fileManager = createJavaFileManager();

        for (int i = 0; i < MAX_CLASS_COUNT; i++) {
            String className = FAKE_CLASS_NAME + i;
            compileClass(fileManager, className);

            loadClass(className);
        }
        fileManager.close();

        System.out.println("Done");
    }

    private JavaFileManager createJavaFileManager() {
        JavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);
        fileManager = new ForwardingJavaFileManager<JavaFileManager>(fileManager) {
            @Override
            public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) throws IOException {
                if (className.startsWith(FAKE_CLASS_NAME)) {
                    ByteArrayJavaClass fileObject = new ByteArrayJavaClass(className);
                    byteArrayJavaClassMap.put(className, fileObject);
                    return fileObject;
                } else {
                    return super.getJavaFileForOutput(location, className, kind, sibling);
                }
            }
        };
        return fileManager;
    }

    private void loadClass(String className) {
        loader.addClassBytes(className, byteArrayJavaClassMap.get(className).getBytes());
        try {
            clazz.add(loader.loadClass(className));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void compileClass(JavaFileManager fileManager, String className) {
        JavaFileObject source = buildSource(className);
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, null, null, asList(source));
        boolean result = task.call();
        for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
            System.out.println(diagnostic.getKind() + ": " + diagnostic.getMessage(null));
        }

        if (!result) {
            System.out.println("Compilation failed.");
            System.exit(1);
        }
    }

    private JavaFileObject buildSource(String className) {
        StringBuilderJavaSource source = new StringBuilderJavaSource(className);
        source.append(format("public class %s {", className));
        for (int i = 0; i < MAX_METHOD_COUNT; i++) {
            source.append(format("public static void %s (){", FAKE_METHOD_NAME + i));
            source.append(format("System.out.println(\"%s\");", FAKE_METHOD_NAME + i));
            source.append("}");
        }
        source.append("}");
        return source;
    }
}
