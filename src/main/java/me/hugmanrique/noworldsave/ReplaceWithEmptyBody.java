package me.hugmanrique.noworldsave;

import jdk.internal.org.objectweb.asm.Opcodes;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;

public class ReplaceWithEmptyBody extends MethodVisitor {
    private final MethodVisitor target;
    private final int newMaxLocals;

    public ReplaceWithEmptyBody(MethodVisitor writer, int newMaxLocals) {
        super(Opcodes.ASM5); // Don't pass the writer to the superclass

        this.target = writer;
        this.newMaxLocals = newMaxLocals;
    }

    // Override the minimum to create a code attribute with a RETURN

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        target.visitMaxs(0, newMaxLocals);
    }

    @Override
    public void visitCode() {
        target.visitCode();
        target.visitInsn(Opcodes.RETURN);
    }

    @Override
    public void visitEnd() {
        target.visitEnd();
    }

    // Meta info, annotations and parameter names

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        return target.visitAnnotation(desc, visible);
    }

    @Override
    public void visitParameter(String name, int access) {
        target.visitParameter(name, access);
    }
}
