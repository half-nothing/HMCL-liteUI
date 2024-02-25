package cn.pigeon.annotation.processor;

import com.google.auto.service.AutoService;
import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Names;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@SupportedAnnotationTypes("cn.pigeon.annotation.InjectionTime")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class InjectionTimeProcessor extends AbstractProcessor {
    private Messager messager;
    private JavacTrees trees;
    private TreeMaker treeMaker;
    private Names names;
    private String timeStr;
    private long timeStamp;
    private Map<String, JCTree.JCAssign> consumerSourceAnnotationValue = new HashMap<>();
    private Map<String, JCTree.JCAssign> providerSourceAnnotationValue = new HashMap<>();
    private java.util.List<String> javaBaseVarType;

    @Override
    public void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.messager = processingEnv.getMessager();
        this.trees = JavacTrees.instance(processingEnv);
        Context context = ((JavacProcessingEnvironment) processingEnv).getContext();
        this.treeMaker = TreeMaker.instance(context);
        this.names = Names.instance(context);
        this.timeStamp = System.currentTimeMillis();
        this.timeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(this.timeStamp));
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        note("InjectionTimeProcessor is processing");
        for (TypeElement annotation : annotations) {
            roundEnv.getElementsAnnotatedWith(annotation).forEach(element -> {
                if (element.getKind() != ElementKind.FIELD) {
                    error("Just processor for fields");
                    return;
                }
                JCTree.JCVariableDecl jcVariableDecl = (JCTree.JCVariableDecl) trees.getTree(element);
                note("Original field:" + jcVariableDecl.toString());
                if (jcVariableDecl.vartype.toString().equals("String")) {
                    jcVariableDecl.init = treeMaker.Literal(timeStr);
                } else if (jcVariableDecl.vartype.toString().equals("Long")) {
                    jcVariableDecl.init = treeMaker.Literal(timeStamp);
                } else {
                    error(String.format("Type %s error!", jcVariableDecl.vartype.toString()));
                }
                note("Modified field: " + jcVariableDecl.toString());
            });
        }
        return true;
    }

    private void note(String message) {
        messager.printMessage(Diagnostic.Kind.NOTE, message);
    }

    private void error(String message) {
        messager.printMessage(Diagnostic.Kind.ERROR, message);
    }
}
