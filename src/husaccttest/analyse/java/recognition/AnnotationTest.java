package husaccttest.analyse.java.recognition;

import static org.junit.Assert.*;
import husacct.common.dto.DependencyDTO;

import org.junit.Test;

public class AnnotationTest extends RecognationExtended {

    @Test
    public void testSamePackage() {
        boolean annotationFound = false;
//		DependencyDTO[] dependencies = service.getDependenciesFrom("annotation.a.SamePackage");
        DependencyDTO[] dependencies = super.getDependenciesFrom("annotation.a.SamePackage");
        for (DependencyDTO dependency : dependencies) {
            if (dependency.type.equals("Annotation")) {
                annotationFound = true;
                assertEquals(5, dependency.lineNumber);
                assertEquals("annotation.a.AnnotationInterface", dependency.to);
            }
        }
        assertEquals(true, annotationFound);
    }

    @Test
    public void testOtherPackageA() {
        boolean annotationFound = false;
        boolean importFound = false;
//		DependencyDTO[] dependencies = service.getDependenciesFrom("annotation.b.OtherPackageA");
        DependencyDTO[] dependencies = super.getDependenciesFrom("annotation.b.OtherPackageA");

        for (DependencyDTO dependency : dependencies) {
            if (dependency.type.equals("Annotation")) {
                annotationFound = true;
                assertEquals(7, dependency.lineNumber);
                assertEquals("annotation.a.AnnotationInterface", dependency.to);
            } else if (dependency.type.equals("Import")) {
                importFound = true;
                assertEquals(3, dependency.lineNumber);
                assertEquals("annotation.a.AnnotationInterface", dependency.to);
            }
        }
        assertEquals(true, annotationFound);
        assertEquals(true, importFound);
    }

    @Test
    public void testOtherPackageB() {
        boolean annotationFound = false;
        boolean importFound = false;
//		DependencyDTO[] dependencies = service.getDependenciesFrom("annotation.b.OtherPackageB");
        DependencyDTO[] dependencies = super.getDependenciesFrom("annotation.b.OtherPackageB");

        for (DependencyDTO dependency : dependencies) {
            if (dependency.type.equals("Annotation")) {
                annotationFound = true;
                assertEquals(7, dependency.lineNumber);
                assertEquals("annotation.a.AnnotationInterface", dependency.to);
            } else if (dependency.type.equals("Import")) {
                importFound = true;
                assertEquals(3, dependency.lineNumber);
                assertEquals("annotation.a", dependency.to);
            }
        }
        assertEquals(true, annotationFound);
        assertEquals(true, importFound);
    }

    @Test
    public void testOtherPackageC() {
        boolean annotationFound = false;
        boolean importFound = false;
//		DependencyDTO[] dependencies = service.getDependenciesFrom("annotation.b.OtherPackageC");
        DependencyDTO[] dependencies = super.getDependenciesFrom("annotation.b.OtherPackageC");

        for (DependencyDTO dependency : dependencies) {
            if (dependency.type.equals("Annotation")) {
                annotationFound = true;
                assertEquals(7, dependency.lineNumber);
                assertEquals("annotation.a.AnnotationInterface", dependency.to);
            } else if (dependency.type.equals("Import")) {
                importFound = true;
                assertEquals(3, dependency.lineNumber);
                assertEquals("annotation.a.AnnotationInterface", dependency.to);
            }
        }
        assertEquals(true, annotationFound);
        assertEquals(true, importFound);
    }
}
