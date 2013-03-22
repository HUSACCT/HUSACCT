package husaccttest.analyse.java.benchmark.domain;

import java.util.HashMap;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;
import husacct.common.dto.DependencyDTO;
import husaccttest.analyse.java.benchmark.BenchmarkExtended;

public class LanguageBusuu extends BenchmarkExtended {

    @Test
    public void testDomainLanguageLearnArabic() {
        String from = "domain.language.busuu.LearnArabic";
        int expectedDependencies = 2;

        //DependencyDTO[] dependencies = service.getDependenciesFrom(from);
        DependencyDTO[] dependencies = super.getDependenciesFrom(from);
        assertEquals(expectedDependencies, dependencies.length);

        String fromImportExpected = from;
        String toImportExpected = "infrastructure.socialmedia.SocialNetwork";
        String typeImportExpected = super.IMPORT;
        int linenumberImportExpected = 3;

        String fromInvocConstructorExpected = from;
        String toInvocConstructorExpected = "infrastructure.socialmedia.SocialNetwork";
        String typeInvocConstructorExpected = super.INVOCCONSTRUCTOR;
        int linenumberInvocConstructorExpected = 11;

        HashMap<String, Object> dependencyImportExpected = createDependencyHashmap(
                fromImportExpected, toImportExpected, typeImportExpected, linenumberImportExpected);
        HashMap<String, Object> dependencyInvocConstructorExpected = createDependencyHashmap(
                fromInvocConstructorExpected, toInvocConstructorExpected, typeInvocConstructorExpected, linenumberInvocConstructorExpected);

        boolean foundImportDependency = compaireDTOWithValues(dependencyImportExpected, dependencies);
        boolean foundInvocConstructorDependency = compaireDTOWithValues(dependencyInvocConstructorExpected, dependencies);

        assertEquals(true, foundImportDependency);
        assertEquals(true, foundInvocConstructorDependency);
    }

    @Test
    public void testDomainLanguageLearnChinese() {
        String from = "domain.language.busuu.LearnChinese";
        int expectedDependencies = 3;

        //DependencyDTO[] dependencies = service.getDependenciesFrom(from);
        DependencyDTO[] dependencies = super.getDependenciesFrom(from);
        assertEquals(expectedDependencies, dependencies.length);

        String fromImportExpected = from;
        String toImportExpected = "infrastructure.socialmedia.SocialNetworkInfo";
        String typeImportExpected = super.IMPORT;
        int linenumberImportExpected = 3;

        String fromInvocMethodExpected = from;
        String toInvocMethodExpected = "infrastructure.socialmedia.SocialNetworkInfo";
        String typeInvocMethodExpected = super.INVOCMETHOD;
        int linenumberInvocMethodExpected = 11;

        HashMap<String, Object> dependencyImportExpected = createDependencyHashmap(
                fromImportExpected, toImportExpected, typeImportExpected, linenumberImportExpected);
        HashMap<String, Object> dependencyInvocMethodExpected = createDependencyHashmap(
                fromInvocMethodExpected, toInvocMethodExpected, typeInvocMethodExpected, linenumberInvocMethodExpected);

        boolean foundImportDependency = compaireDTOWithValues(dependencyImportExpected, dependencies);
        boolean foundInvocMethodDependency = compaireDTOWithValues(dependencyInvocMethodExpected, dependencies);

        assertEquals(true, foundImportDependency);
        assertEquals(true, foundInvocMethodDependency);
    }

    @Test
    public void testDomainLanguageLearnDutch() {
        String from = "domain.language.busuu.LearnDutch";
        int expectedDependencies = 4;

        //DependencyDTO[] dependencies = service.getDependenciesFrom(from);
        DependencyDTO[] dependencies = super.getDependenciesFrom(from);
        assertEquals(expectedDependencies, dependencies.length);

        String fromImportExpected = from;
        String toImportExpected = "infrastructure.socialmedia.SocialNetwork";
        String typeImportExpected = super.IMPORT;
        int linenumberImportExpected = 3;

        String fromDeclarationExpected = from;
        String toDeclarationExpected = "infrastructure.socialmedia.SocialNetwork";
        String typeDeclarationExpected = super.DECLARATION;
        int linenumberDeclarationExpected = 10;

        String fromInvocMethodExpected = from;
        String toInvocMethodExpected = "infrastructure.socialmedia.SocialNetwork";
        String typeInvocMethodExpected = super.INVOCMETHOD;
        int linenumberInvocMethodExpected = 14;

        HashMap<String, Object> dependencyImportExpected = createDependencyHashmap(
                fromImportExpected, toImportExpected, typeImportExpected, linenumberImportExpected);
        HashMap<String, Object> dependencyDeclarationExpected = createDependencyHashmap(
                fromDeclarationExpected, toDeclarationExpected, typeDeclarationExpected, linenumberDeclarationExpected);
        HashMap<String, Object> dependencyInvocMethodExpected = createDependencyHashmap(
                fromInvocMethodExpected, toInvocMethodExpected, typeInvocMethodExpected, linenumberInvocMethodExpected);

        boolean foundImportDependency = compaireDTOWithValues(dependencyImportExpected, dependencies);
        boolean foundDeclarationDependency = compaireDTOWithValues(dependencyDeclarationExpected, dependencies);
        boolean foundInvocMethodDependency = compaireDTOWithValues(dependencyInvocMethodExpected, dependencies);

        assertEquals(true, foundImportDependency);
        assertEquals(true, foundDeclarationDependency);
        assertEquals(true, foundInvocMethodDependency);
    }

    @Ignore("AccesOfpropertyorField double sniName = Ac...name")
    @Test
    public void testDomainLanguageLearnEnglish() {
        String from = "domain.language.busuu.LearnEnglish";
        int expectedDependencies = 3;

//		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
        DependencyDTO[] dependencies = super.getDependenciesFrom(from);
        assertEquals(expectedDependencies, dependencies.length);

        String fromImportExpected = from;
        String toImportExpected = "infrastructure.socialmedia.SocialNetworkInfo";
        String typeImportExpected = super.IMPORT;
        int linenumberImportExpected = 3;

        String fromDeclarationExpected = from;
        String toDeclarationExpected = "infrastructure.socialmedia.SocialNetworkInfo";
        String typeDeclarationExpected = super.DECLARATION;
        int linenumberDeclarationExpected = 10;

        String fromInvocMethodExpected = from;
        String toInvocMethodExpected = "infrastructure.socialmedia.SocialNetworkInfo";
        String typeInvocMethodExpected = super.INVOCMETHOD;
        int linenumberInvocMethodExpected = 14;

        HashMap<String, Object> dependencyImportExpected = createDependencyHashmap(
                fromImportExpected, toImportExpected, typeImportExpected, linenumberImportExpected);
        HashMap<String, Object> dependencyDeclarationExpected = createDependencyHashmap(
                fromDeclarationExpected, toDeclarationExpected, typeDeclarationExpected, linenumberDeclarationExpected);
        HashMap<String, Object> dependencyInvocMethodExpected = createDependencyHashmap(
                fromInvocMethodExpected, toInvocMethodExpected, typeInvocMethodExpected, linenumberInvocMethodExpected);

        boolean foundImportDependency = compaireDTOWithValues(dependencyImportExpected, dependencies);
        boolean foundDeclarationDependency = compaireDTOWithValues(dependencyDeclarationExpected, dependencies);
        boolean foundInvocMethodDependency = compaireDTOWithValues(dependencyInvocMethodExpected, dependencies);

        assertEquals(true, foundImportDependency);
        assertEquals(true, foundDeclarationDependency);
        assertEquals(true, foundInvocMethodDependency);
    }

    @Ignore("AccesPropertyOrField double asocialnetwork.type = 'klant'")
    @Test
    public void testDomainLanguageLearnFrench() {
        String from = "domain.language.busuu.LearnFrench";
        int expectedDependencies = 3;

        DependencyDTO[] dependencies = super.getDependenciesFrom(from);
        assertEquals(expectedDependencies, dependencies.length);

        String fromImportExpected = from;
        String toImportExpected = "infrastructure.socialmedia.SocialNetwork";
        String typeImportExpected = super.IMPORT;
        int linenumberImportExpected = 3;

        String fromDeclarationExpected = from;
        String toDeclarationExpected = "infrastructure.socialmedia.SocialNetwork";
        String typeDeclarationExpected = super.DECLARATION;
        int linenumberDeclarationExpected = 11;

        String fromInvocMethodExpected = from;
        String toInvocMethodExpected = "infrastructure.socialmedia.SocialNetwork";
        String typeInvocMethodExpected = super.INVOCMETHOD;
        int linenumberInvocMethodExpected = 14;

        HashMap<String, Object> dependencyImportExpected = createDependencyHashmap(
                fromImportExpected, toImportExpected, typeImportExpected, linenumberImportExpected);
        HashMap<String, Object> dependencyDeclarationExpected = createDependencyHashmap(
                fromDeclarationExpected, toDeclarationExpected, typeDeclarationExpected, linenumberDeclarationExpected);
        HashMap<String, Object> dependencyInvocMethodExpected = createDependencyHashmap(
                fromInvocMethodExpected, toInvocMethodExpected, typeInvocMethodExpected, linenumberInvocMethodExpected);

        boolean foundImportDependency = compaireDTOWithValues(dependencyImportExpected, dependencies);
        boolean foundDeclarationDependency = compaireDTOWithValues(dependencyDeclarationExpected, dependencies);
        boolean foundInvocMethodDependency = compaireDTOWithValues(dependencyInvocMethodExpected, dependencies);

        assertEquals(true, foundImportDependency);
        assertEquals(true, foundDeclarationDependency);
        assertEquals(true, foundInvocMethodDependency);
    }

    @Test
    public void testDomainLanguageLearnGerman() {
        String from = "domain.language.busuu.LearnGerman";
        int expectedDependencies = 2;

//		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
        DependencyDTO[] dependencies = super.getDependenciesFrom(from);
        assertEquals(expectedDependencies, dependencies.length);

        String fromImportExpected = from;
        String toImportExpected = "infrastructure.socialmedia.SocialMedia";
        String typeImportExpected = super.IMPORT;
        int linenumberImportExpected = 3;

        String fromExtendsExpected = from;
        String toExtendsExpected = "infrastructure.socialmedia.SocialMedia";
        String typeExtendsExpected = super.EXTENDSABSTRACT;
        int linenumberExtendsExpected = 10;

        HashMap<String, Object> dependencyImportExpected = createDependencyHashmap(
                fromImportExpected, toImportExpected, typeImportExpected, linenumberImportExpected);
        HashMap<String, Object> dependencyExtendsExpected = createDependencyHashmap(
                fromExtendsExpected, toExtendsExpected, typeExtendsExpected, linenumberExtendsExpected);

        boolean foundImportDependency = compaireDTOWithValues(dependencyImportExpected, dependencies);
        boolean foundExtendsDependency = compaireDTOWithValues(dependencyExtendsExpected, dependencies);

        assertEquals(true, foundImportDependency);
        assertEquals(true, foundExtendsDependency);
    }

    @Test
    public void testDomainLanguageLearnJapanese() {
        String from = "domain.language.busuu.LearnJapanese";
        int expectedDependencies = 2;

        //DependencyDTO[] dependencies = service.getDependenciesFrom(from);
        DependencyDTO[] dependencies = super.getDependenciesFrom(from);
        assertEquals(expectedDependencies, dependencies.length);

        String fromImportExpected = from;
        String toImportExpected = "infrastructure.socialmedia.SocialNetwork";
        String typeImportExpected = super.IMPORT;
        int linenumberImportExpected = 3;

        String fromExtendsExpected = from;
        String toExtendsExpected = "infrastructure.socialmedia.SocialNetwork";
        String typeExtendsExpected = super.EXTENDSCONCRETE;
        int linenumberExtendsExpected = 10;

        HashMap<String, Object> dependencyImportExpected = createDependencyHashmap(
                fromImportExpected, toImportExpected, typeImportExpected, linenumberImportExpected);
        HashMap<String, Object> dependencyExtendsExpected = createDependencyHashmap(
                fromExtendsExpected, toExtendsExpected, typeExtendsExpected, linenumberExtendsExpected);

        boolean foundImportDependency = compaireDTOWithValues(dependencyImportExpected, dependencies);
        boolean foundExtendsDependency = compaireDTOWithValues(dependencyExtendsExpected, dependencies);

        assertEquals(true, foundImportDependency);
        assertEquals(true, foundExtendsDependency);
    }

    @Test
    public void testDomainLanguageLearnPolish() {
        String from = "domain.language.busuu.LearnPolish";
        int expectedDependencies = 2;

//		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
        DependencyDTO[] dependencies = super.getDependenciesFrom(from);
        assertEquals(expectedDependencies, dependencies.length);

        String fromImportExpected = from;
        String toImportExpected = "infrastructure.socialmedia.ISocialMedia";
        String typeImportExpected = super.IMPORT;
        int linenumberImportExpected = 3;

        String fromImplementsExpected = from;
        String toImplementsExpected = "infrastructure.socialmedia.ISocialMedia";
        String typeImplementsExpected = super.IMPLEMENTS;
        int linenumberImplementsExpected = 10;

        HashMap<String, Object> dependencyImportExpected = createDependencyHashmap(
                fromImportExpected, toImportExpected, typeImportExpected, linenumberImportExpected);
        HashMap<String, Object> dependencyImplementsExpected = createDependencyHashmap(
                fromImplementsExpected, toImplementsExpected, typeImplementsExpected, linenumberImplementsExpected);

        boolean foundImportDependency = compaireDTOWithValues(dependencyImportExpected, dependencies);
        boolean foundImplementsDependency = compaireDTOWithValues(dependencyImplementsExpected, dependencies);

        assertEquals(true, foundImportDependency);
        assertEquals(true, foundImplementsDependency);
    }

    @Test
    public void testDomainLanguageLearnRussian() {
        String from = "domain.language.busuu.LearnRussian";
        int expectedDependencies = 2;

        //DependencyDTO[] dependencies = service.getDependenciesFrom(from);
        DependencyDTO[] dependencies = super.getDependenciesFrom(from);
        assertEquals(expectedDependencies, dependencies.length);

        String fromImportExpected = from;
        String toImportExpected = "infrastructure.socialmedia.SocialNetworkInfo";
        String typeImportExpected = super.IMPORT;
        int linenumberImportExpected = 3;

        String fromDeclarationExpected = from;
        String toDeclarationExpected = "infrastructure.socialmedia.SocialNetworkInfo";
        String typeDeclarationExpected = super.DECLARATION;
        int linenumberDeclarationExpected = 10;

        HashMap<String, Object> dependencyImportExpected = createDependencyHashmap(
                fromImportExpected, toImportExpected, typeImportExpected, linenumberImportExpected);
        HashMap<String, Object> dependencyDeclarationExpected = createDependencyHashmap(
                fromDeclarationExpected, toDeclarationExpected, typeDeclarationExpected, linenumberDeclarationExpected);

        boolean foundImportDependency = compaireDTOWithValues(dependencyImportExpected, dependencies);
        boolean foundDeclarationDependency = compaireDTOWithValues(dependencyDeclarationExpected, dependencies);

        assertEquals(true, foundImportDependency);
        assertEquals(true, foundDeclarationDependency);
    }

    @Test
    public void testDomainLanguageLearnSpanish() {
        String from = "domain.language.busuu.LearnSpanish";
        int expectedDependencies = 2;

//		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
        DependencyDTO[] dependencies = super.getDependenciesFrom(from);
        assertEquals(expectedDependencies, dependencies.length);

        String fromImportExpected = from;
        String toImportExpected = "infrastructure.socialmedia.SocialTagAnnotation";
        String typeImportExpected = super.IMPORT;
        int linenumberImportExpected = 3;

        String fromAnnotationExpected = from;
        String toAnnotationExpected = "infrastructure.socialmedia.SocialTagAnnotation";
        String typeAnnotationExpected = super.ANNOTATION;
        int linenumberAnnotationExpected = 10;

        HashMap<String, Object> dependencyImportExpected = createDependencyHashmap(
                fromImportExpected, toImportExpected, typeImportExpected, linenumberImportExpected);
        HashMap<String, Object> dependencyAnnotationExpected = createDependencyHashmap(
                fromAnnotationExpected, toAnnotationExpected, typeAnnotationExpected, linenumberAnnotationExpected);

        boolean foundImportDependency = compaireDTOWithValues(dependencyImportExpected, dependencies);
        boolean foundAnnotationDependency = compaireDTOWithValues(dependencyAnnotationExpected, dependencies);

        assertEquals(true, foundImportDependency);
        assertEquals(true, foundAnnotationDependency);
    }

    @Test
    public void testDomainLanguageLearnSwedish() {
        String from = "domain.language.busuu.LearnSwedish";
        int expectedDependencies = 1;

//		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
        DependencyDTO[] dependencies = super.getDependenciesFrom(from);
        assertEquals(expectedDependencies, dependencies.length);

        String fromImportExpected = from;
        String toImportExpected = "infrastructure.socialmedia.language.ILanguage";
        String typeImportExpected = super.IMPORT;
        int linenumberImportExpected = 4;

        HashMap<String, Object> dependencyImportExpected = createDependencyHashmap(
                fromImportExpected, toImportExpected, typeImportExpected, linenumberImportExpected);

        boolean foundImportDependency = compaireDTOWithValues(dependencyImportExpected, dependencies);

        assertEquals(true, foundImportDependency);
    }

    @Test
    public void testDomainLanguageLearnTurkish() {
        String from = "domain.language.busuu.LearnTurkish";
        int expectedDependencies = 4;

        //DependencyDTO[] dependencies = service.getDependenciesFrom(from);
        DependencyDTO[] dependencies = super.getDependenciesFrom(from);
        assertEquals(expectedDependencies, dependencies.length);

        String fromImportExpected = from;
        String toImportExpected = "infrastructure.socialmedia.SocialMediaException";
        String typeImportExpected = super.IMPORT;
        int linenumberImportExpected = 3;

        String fromException1Expected = from;
        String toException1Expected = "infrastructure.socialmedia.SocialMediaException";
        String typeException1Expected = super.EXCEPTION;
        int linenumberException1Expected = 13;

        String fromException2Expected = from;
        String toException2Expected = "infrastructure.socialmedia.SocialMediaException";
        String typeException2Expected = super.EXCEPTION;
        int linenumberException2Expected = 19;

        String fromException3Expected = from;
        String toException3Expected = "infrastructure.socialmedia.SocialMediaException";
        String typeException3Expected = super.EXCEPTION;
        int linenumberException3Expected = 21;

        HashMap<String, Object> dependencyImportExpected = createDependencyHashmap(
                fromImportExpected, toImportExpected, typeImportExpected, linenumberImportExpected);
        HashMap<String, Object> dependencyException1Expected = createDependencyHashmap(
                fromException1Expected, toException1Expected, typeException1Expected, linenumberException1Expected);
        HashMap<String, Object> dependencyException2Expected = createDependencyHashmap(
                fromException2Expected, toException2Expected, typeException2Expected, linenumberException2Expected);
        HashMap<String, Object> dependencyException3Expected = createDependencyHashmap(
                fromException3Expected, toException3Expected, typeException3Expected, linenumberException3Expected);

        boolean foundImportDependency = compaireDTOWithValues(dependencyImportExpected, dependencies);
        boolean foundException1Dependency = compaireDTOWithValues(dependencyException1Expected, dependencies);
        boolean foundException2Dependency = compaireDTOWithValues(dependencyException2Expected, dependencies);
        boolean foundException3Dependency = compaireDTOWithValues(dependencyException3Expected, dependencies);

        assertEquals(true, foundImportDependency);
        assertEquals(true, foundException1Dependency);
        assertEquals(true, foundException2Dependency);
        assertEquals(true, foundException3Dependency);
    }
}
