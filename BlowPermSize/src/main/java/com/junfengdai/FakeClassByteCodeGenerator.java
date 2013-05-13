package com.junfengdai;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class FakeClassByteCodeGenerator {

    private static final int METHOD_NUMBER = 10;

    public byte[] generateClass(String className) {
        ClassWriter classWriter = new ClassWriter(0);
        classWriter.visit(Opcodes.V1_1, Opcodes.ACC_PUBLIC, className, null, "java/lang/Object", null);

        generateConstructor(classWriter);
        for (int i = 0; i < METHOD_NUMBER; i++) {
            generateMethod(classWriter, "M" + i);
        }

        classWriter.visitEnd();
        return classWriter.toByteArray();
    }

    private void generateMethod(ClassWriter classWriter, String methodName) {
        MethodVisitor methodVisitor = classWriter.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC, methodName, "()V", null, null);
        methodVisitor.visitFieldInsn(
                Opcodes.GETSTATIC,
                "java/lang/System",
                "out",
                Type.getObjectType("java/io/PrintStream").getDescriptor()
        );
        methodVisitor.visitLdcInsn(methodName);
        methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V");
        methodVisitor.visitInsn(Opcodes.RETURN);
        methodVisitor.visitMaxs(2, 1);
        methodVisitor.visitEnd();
    }

    private void generateConstructor(ClassWriter classWriter) {
        MethodVisitor methodVisitor = classWriter.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
        methodVisitor.visitVarInsn(Opcodes.ALOAD, 0);
        methodVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
        methodVisitor.visitInsn(Opcodes.RETURN);
        methodVisitor.visitMaxs(1, 1);
        methodVisitor.visitEnd();
    }
}
