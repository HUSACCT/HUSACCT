package husaccttest.analyse.java.benchmark.domain;

import java.util.HashMap;

import org.junit.Test;

import static org.junit.Assert.*;
import husacct.common.dto.AnalysedModuleDTO;
import husaccttest.analyse.java.benchmark.BenchmarkExtended;



public class Facebook extends BenchmarkExtended{

	@Test
	public void testNavigation(){
		String from = "domain.facebook";
		
		int expectedchildmodules = 14;
		AnalysedModuleDTO[] childClasses = service.getChildModulesInModule(from);
		assertEquals(expectedchildmodules, childClasses.length);
		
		HashMap<String, Object> accountExpectedModule = createModuleHashmap(
				"Account", 
				from +  ".Account",
				0,
				super.CLASS);
		
		HashMap<String, Object> eventExpectedModule = createModuleHashmap(
				"Event", 
				from +  ".Event",
				0,
				super.CLASS);
		
		HashMap<String, Object> facebookannotationExpectedModule = createModuleHashmap(
				"FacebookAnnotation", 
				from +  ".FacebookAnnotation",
				0,
				super.INTERFACE);
		
		HashMap<String, Object> facebookexceptionExpectedModule = createModuleHashmap(
				"FacebookException", 
				from +  ".FacebookException",
				0,
				super.CLASS);
		
		HashMap<String, Object> friendsExpectedModule = createModuleHashmap(
				"Friends", 
				from +  ".Friends",
				0,
				super.CLASS);
		
		HashMap<String, Object> ipokeExpectedModule = createModuleHashmap(
				"IPoke", 
				from +  ".IPoke",
				0,
				super.INTERFACE);
		
		HashMap<String, Object> linkExpectedModule = createModuleHashmap(
				"Link", 
				from +  ".Link",
				0,
				super.CLASS);
		
		HashMap<String, Object> menubarExpectedModule = createModuleHashmap(
				"Menubar", 
				from +  ".Menubar",
				0,
				super.CLASS);
		
		HashMap<String, Object> messageExpectedModule = createModuleHashmap(
				"Message", 
				from +  ".Message",
				0,
				super.CLASS);
		
		HashMap<String, Object> noteExpectedModule = createModuleHashmap(
				"Note", 
				from +  ".Note",
				0,
				super.CLASS);
		
		HashMap<String, Object> photoExpectedModule = createModuleHashmap(
				"Photo", 
				from +  ".Photo",
				0,
				super.CLASS);
		
		HashMap<String, Object> privacysettingsExpectedModule = createModuleHashmap(
				"PrivacySettings", 
				from +  ".PrivacySettings",
				0,
				super.CLASS);
		
		HashMap<String, Object> questionExpectedModule = createModuleHashmap(
				"Question", 
				from +  ".Question",
				0,
				super.CLASS);
		
		HashMap<String, Object> songExpectedModule = createModuleHashmap(
				"Song", 
				from +  ".Song",
				0,
				super.CLASS);
		
		boolean foundaccount = compaireDTOWithValues(accountExpectedModule, childClasses);
		boolean foundevent = compaireDTOWithValues(eventExpectedModule, childClasses);
		boolean foundfacebookannotation = compaireDTOWithValues(facebookannotationExpectedModule, childClasses);
		boolean foundfacebookexception = compaireDTOWithValues(facebookexceptionExpectedModule, childClasses);
		boolean foundfriends = compaireDTOWithValues(friendsExpectedModule, childClasses);
		boolean foundipoke = compaireDTOWithValues(ipokeExpectedModule, childClasses);
		boolean foundlink = compaireDTOWithValues(linkExpectedModule, childClasses);
		boolean foundmenubar = compaireDTOWithValues(menubarExpectedModule, childClasses);
		boolean foundmessage = compaireDTOWithValues(messageExpectedModule, childClasses);
		boolean foundnote = compaireDTOWithValues(noteExpectedModule, childClasses);
		boolean foundphoto = compaireDTOWithValues(photoExpectedModule, childClasses);
		boolean foundprivacysettings = compaireDTOWithValues(privacysettingsExpectedModule, childClasses);
		boolean foundquestion = compaireDTOWithValues(questionExpectedModule, childClasses);
		boolean foundsong = compaireDTOWithValues(songExpectedModule, childClasses);
		
		
		assertEquals(true, foundaccount);
		assertEquals(true, foundevent);
		assertEquals(true, foundfacebookannotation);
		assertEquals(true, foundfacebookexception);
		assertEquals(true, foundfriends);
		assertEquals(true, foundipoke);
		assertEquals(true, foundlink);
		assertEquals(true, foundmenubar);
		assertEquals(true, foundmessage);
		assertEquals(true, foundnote);
		assertEquals(true, foundphoto);
		assertEquals(true, foundprivacysettings);
		assertEquals(true, foundquestion);
		assertEquals(true, foundsong);
	}
}
