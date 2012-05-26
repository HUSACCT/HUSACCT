package husaccttest.analyse.benchmarkjava.domain;

import java.util.HashMap;

import org.junit.Test;

import static org.junit.Assert.*;
import husacct.common.dto.AnalysedModuleDTO;
import husaccttest.analyse.benchmarkjava.BenchmarkExtended;



public class LanguageNavigation extends BenchmarkExtended{

	@Test
	public void testNavigation(){
		String from = "domain.language";
		
		int expectedchildmodules = 2;
		AnalysedModuleDTO[] childModules = service.getChildModulesInModule(from);
		assertEquals(expectedchildmodules, childModules.length);
		
		HashMap<String, Object> brightkiteExpectedModule = createModuleHashmap(
				"babbel", 
				from +  ".babbel",
				0,
				super.PACKAGE);
		
		HashMap<String, Object> glympseExpectedModule = createModuleHashmap(
				"busuu", 
				from +  ".busuu",
				0,
				super.PACKAGE);
		
		boolean foundbrightkite = compaireDTOWithValues(brightkiteExpectedModule, childModules);
		boolean foundglympse = compaireDTOWithValues(glympseExpectedModule, childModules);
		
		assertEquals(true, foundbrightkite);
		assertEquals(true, foundglympse);
	}
	
	@Test
	public void testNavigationBabbel(){
		String from = "domain.language.babbel";
		
		int expectedchildmodules = 12;
		AnalysedModuleDTO[] childModules = service.getChildModulesInModule(from);
		assertEquals(expectedchildmodules, childModules.length);
		
		HashMap<String, Object> learnArabicExpectedModule = createModuleHashmap(
				"LearnArabic", 
				from +  ".LearnArabic",
				0,
				super.CLASS);
		
		HashMap<String, Object> learnChineseExpectedModule = createModuleHashmap(
				"LearnChinese", 
				from +  ".LearnChinese",
				0,
				super.CLASS);
		
		HashMap<String, Object> learnDutchExpectedModule = createModuleHashmap(
				"LearnDutch", 
				from +  ".LearnDutch",
				0,
				super.CLASS);
		
		HashMap<String, Object> learnEnglishExpectedModule = createModuleHashmap(
				"LearnEnglish", 
				from +  ".LearnEnglish",
				0,
				super.CLASS);
		
		HashMap<String, Object> learnFrenchExpectedModule = createModuleHashmap(
				"LearnFrench", 
				from +  ".LearnFrench",
				0,
				super.CLASS);
		
		HashMap<String, Object> learnGermanExpectedModule = createModuleHashmap(
				"LearnGerman", 
				from +  ".LearnGerman",
				0,
				super.CLASS);
		
		HashMap<String, Object> learnJapaneseExpectedModule = createModuleHashmap(
				"LearnJapanese", 
				from +  ".LearnJapanese",
				0,
				super.CLASS);
		
		HashMap<String, Object> learnPolishExpectedModule = createModuleHashmap(
				"LearnPolish", 
				from +  ".LearnPolish",
				0,
				super.CLASS);
		
		HashMap<String, Object> learnRussianExpectedModule = createModuleHashmap(
				"LearnRussian", 
				from +  ".LearnRussian",
				0,
				super.CLASS);
		
		HashMap<String, Object> learnSpanishExpectedModule = createModuleHashmap(
				"LearnSpanish", 
				from +  ".LearnSpanish",
				0,
				super.CLASS);
		
		HashMap<String, Object> learnSwedishExpectedModule = createModuleHashmap(
				"LearnSwedish", 
				from +  ".LearnSwedish",
				0,
				super.CLASS);
		
		HashMap<String, Object> learnTurkishExpectedModule = createModuleHashmap(
				"LearnTurkish", 
				from +  ".LearnTurkish",
				0,
				super.CLASS);
		
		boolean foundlearnArabic = compaireDTOWithValues(learnArabicExpectedModule, childModules);
		boolean foundlearnChinese = compaireDTOWithValues(learnChineseExpectedModule, childModules);
		boolean foundlearnDutch = compaireDTOWithValues(learnDutchExpectedModule, childModules);
		boolean foundlearnEnglish = compaireDTOWithValues(learnEnglishExpectedModule, childModules);
		boolean foundlearnFrench = compaireDTOWithValues(learnFrenchExpectedModule, childModules);
		boolean foundlearnGerman = compaireDTOWithValues(learnGermanExpectedModule, childModules);
		boolean foundlearnJapanese = compaireDTOWithValues(learnJapaneseExpectedModule, childModules);
		boolean foundlearnPolish = compaireDTOWithValues(learnPolishExpectedModule, childModules);
		boolean foundlearnRussian = compaireDTOWithValues(learnRussianExpectedModule, childModules);
		boolean foundlearnSpanish = compaireDTOWithValues(learnSpanishExpectedModule, childModules);
		boolean foundlearnSwedish = compaireDTOWithValues(learnSwedishExpectedModule, childModules);
		boolean foundlearnTurkish = compaireDTOWithValues(learnTurkishExpectedModule, childModules);
		
		
		
		assertEquals(true, foundlearnArabic);
		assertEquals(true, foundlearnChinese);
		assertEquals(true, foundlearnDutch);
		assertEquals(true, foundlearnEnglish);
		assertEquals(true, foundlearnFrench);
		assertEquals(true, foundlearnGerman);
		assertEquals(true, foundlearnJapanese);
		assertEquals(true, foundlearnPolish);
		assertEquals(true, foundlearnRussian);
		assertEquals(true, foundlearnSpanish);
		assertEquals(true, foundlearnSwedish);
		assertEquals(true, foundlearnTurkish);
	}
	
	@Test
	public void testNavigationBusuu(){
		String from = "domain.language.busuu";
		
		int expectedchildmodules = 12;
		AnalysedModuleDTO[] childModules = service.getChildModulesInModule(from);
		assertEquals(expectedchildmodules, childModules.length);
		
		HashMap<String, Object> learnArabicExpectedModule = createModuleHashmap(
				"LearnArabic", 
				from +  ".LearnArabic",
				0,
				super.CLASS);
		
		HashMap<String, Object> learnChineseExpectedModule = createModuleHashmap(
				"LearnChinese", 
				from +  ".LearnChinese",
				0,
				super.CLASS);
		
		HashMap<String, Object> learnDutchExpectedModule = createModuleHashmap(
				"LearnDutch", 
				from +  ".LearnDutch",
				0,
				super.CLASS);
		
		HashMap<String, Object> learnEnglishExpectedModule = createModuleHashmap(
				"LearnEnglish", 
				from +  ".LearnEnglish",
				0,
				super.CLASS);
		
		HashMap<String, Object> learnFrenchExpectedModule = createModuleHashmap(
				"LearnFrench", 
				from +  ".LearnFrench",
				0,
				super.CLASS);
		
		HashMap<String, Object> learnGermanExpectedModule = createModuleHashmap(
				"LearnGerman", 
				from +  ".LearnGerman",
				0,
				super.CLASS);
		
		HashMap<String, Object> learnJapaneseExpectedModule = createModuleHashmap(
				"LearnJapanese", 
				from +  ".LearnJapanese",
				0,
				super.CLASS);
		
		HashMap<String, Object> learnPolishExpectedModule = createModuleHashmap(
				"LearnPolish", 
				from +  ".LearnPolish",
				0,
				super.CLASS);
		
		HashMap<String, Object> learnRussianExpectedModule = createModuleHashmap(
				"LearnRussian", 
				from +  ".LearnRussian",
				0,
				super.CLASS);
		
		HashMap<String, Object> learnSpanishExpectedModule = createModuleHashmap(
				"LearnSpanish", 
				from +  ".LearnSpanish",
				0,
				super.CLASS);
		
		HashMap<String, Object> learnSwedishExpectedModule = createModuleHashmap(
				"LearnSwedish", 
				from +  ".LearnSwedish",
				0,
				super.CLASS);
		
		HashMap<String, Object> learnTurkishExpectedModule = createModuleHashmap(
				"LearnTurkish", 
				from +  ".LearnTurkish",
				0,
				super.CLASS);
		
		boolean foundlearnArabic = compaireDTOWithValues(learnArabicExpectedModule, childModules);
		boolean foundlearnChinese = compaireDTOWithValues(learnChineseExpectedModule, childModules);
		boolean foundlearnDutch = compaireDTOWithValues(learnDutchExpectedModule, childModules);
		boolean foundlearnEnglish = compaireDTOWithValues(learnEnglishExpectedModule, childModules);
		boolean foundlearnFrench = compaireDTOWithValues(learnFrenchExpectedModule, childModules);
		boolean foundlearnGerman = compaireDTOWithValues(learnGermanExpectedModule, childModules);
		boolean foundlearnJapanese = compaireDTOWithValues(learnJapaneseExpectedModule, childModules);
		boolean foundlearnPolish = compaireDTOWithValues(learnPolishExpectedModule, childModules);
		boolean foundlearnRussian = compaireDTOWithValues(learnRussianExpectedModule, childModules);
		boolean foundlearnSpanish = compaireDTOWithValues(learnSpanishExpectedModule, childModules);
		boolean foundlearnSwedish = compaireDTOWithValues(learnSwedishExpectedModule, childModules);
		boolean foundlearnTurkish = compaireDTOWithValues(learnTurkishExpectedModule, childModules);
		
		
		
		assertEquals(true, foundlearnArabic);
		assertEquals(true, foundlearnChinese);
		assertEquals(true, foundlearnDutch);
		assertEquals(true, foundlearnEnglish);
		assertEquals(true, foundlearnFrench);
		assertEquals(true, foundlearnGerman);
		assertEquals(true, foundlearnJapanese);
		assertEquals(true, foundlearnPolish);
		assertEquals(true, foundlearnRussian);
		assertEquals(true, foundlearnSpanish);
		assertEquals(true, foundlearnSwedish);
		assertEquals(true, foundlearnTurkish);
	}
	
	
}
