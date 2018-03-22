package org.springboot.demo.config;

import org.apache.ibatis.javassist.*;

import java.io.IOException;

public class Pojie {

    private final static String IDEA_LIB = "/Applications/IntelliJ IDEA.app/Contents/lib/*";
    private final static String IDEIS_LIB = "/Users/abee/Library/Application Support/IntelliJIdea2017.3/Iedis/lib/*";

    public static void main(String[] args) throws NotFoundException {
        try {
            ClassPool.getDefault().appendClassPath(IDEA_LIB);
            ClassPool.getDefault().appendClassPath(IDEIS_LIB);
            CtClass clazz = ClassPool.getDefault().getCtClass("com.seventh7.widget.iedis.a.p");
            CtMethod[] mds = clazz.getDeclaredMethods();
            for (CtMethod method : mds) {
                if (method.getLongName().startsWith("com.seventh7.widget.iedis.a.p.f")) {
                    System.out.println("Inject :: SUCCESS!");
                    try {
                        method.insertBefore("if(true){return true;} ");
                    } catch (CannotCompileException e) {
                        e.printStackTrace();
                    }
                }
            }
            clazz.writeFile("/tmp/p.class");
        } catch (NotFoundException | CannotCompileException | IOException e) {
            e.printStackTrace();
        }
    }
}
