package husaccttest.analyse.benchmarkjava.domain;

import java.util.HashMap;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;
import husaccttest.analyse.benchmarkjava.BenchmarkExtended;



public class LastFM extends BenchmarkExtended{

	@Test
	public void testNavigation(){
		String from = "domain.lastfm";
		
		int expectedchildmodules = 3;
		AnalysedModuleDTO[] childModules = service.getChildModulesInModule(from);
		assertEquals(expectedchildmodules, childModules.length);
		
		HashMap<String, Object> albumExpectedModule = createModuleHashmap(
				"Album", 
				from +  ".Album",
				0,
				super.CLASS);
		
		HashMap<String, Object> artistExpectedModule = createModuleHashmap(
				"Artist", 
				from +  ".Artist",
				0,
				super.CLASS);
		
		HashMap<String, Object> songExpectedModule = createModuleHashmap(
				"Song", 
				from +  ".Song",
				0,
				super.CLASS);
		
		boolean foundAlbum = compaireDTOWithValues(albumExpectedModule, childModules);
		boolean foundArtist = compaireDTOWithValues(artistExpectedModule, childModules);
		boolean foundSong = compaireDTOWithValues(songExpectedModule, childModules);
		
		assertEquals(true, foundAlbum);
		assertEquals(true, foundArtist);
		assertEquals(true, foundSong);
	}
	
	@Ignore("double access of field/property")
	@Test
	public void testDomainLastFMAlbum(){
		String from = "domain.lastfm.Album";
		int expectedDependencies = 5;
		
		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);
	}	
	
	@Ignore("double access of field/property")
	@Test
	public void testDomainLastFMArtist(){
		String from = "domain.lastfm.Artist";
		int expectedDependencies = 2;
		
		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);
	}	
	
	@Ignore("double access of field/property")
	@Test
	public void testDomainLastFMSong(){
		String from = "domain.lastfm.Song";
		int expectedDependencies = 2;
		
		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);
	}	
	
	
}

