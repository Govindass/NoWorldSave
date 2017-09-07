package me.hugmanrique.noworldsave;

import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.Type;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

public class SaveRemoverMethod extends ClassVisitor {
    private final String hotMethodName;
    private final String hotMethodDesc;

    public SaveRemoverMethod(ClassWriter writer, String name, String methodDescriptor) {
        super(Opcodes.ASM5, writer);

        hotMethodName = name;
        hotMethodDesc = methodDescriptor;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        if (!name.equals(hotMethodName) || !desc.equals(hotMethodDesc)) {
            // Reproduce original methods
            return super.visitMethod(access, name, desc, signature, exceptions);
        }

        // Alter the new behavior
        return new ReplaceWithEmptyBody(
                super.visitMethod(access, name, desc, signature, exceptions),
                (Type.getArgumentsAndReturnSizes(desc) >> 2) - 1
        );
    }
}
