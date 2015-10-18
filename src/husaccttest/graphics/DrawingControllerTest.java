package husaccttest.graphics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import husacct.common.dto.AbstractDTO;
import husacct.common.dto.SoftwareUnitDTO;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ModuleDTO;
import husacct.graphics.presentation.figures.AbstractClassFigure;
import husacct.graphics.presentation.figures.BaseFigure;
import husacct.graphics.presentation.figures.ClassFigure;
import husacct.graphics.presentation.figures.InterfaceFigure;
import husacct.graphics.presentation.figures.ParentFigure;
import husacct.graphics.presentation.figures.RelationFigure;
import husacct.graphics.task.AnalysedController;

import java.util.ArrayList;
import java.util.HashMap;

import org.jhotdraw.draw.Figure;
import org.junit.Before;
import org.junit.Test;

public class DrawingControllerTest {
	AnalysedController analysedController;
	
	@Before
	public void setup() {
		analysedController = new AnalysedController();
	}

	@Test
	public void drawSingleLevelModulesTest() {
		SoftwareUnitDTO layerDTO = new SoftwareUnitDTO("test.layer", "analysedLayer", "layer", "public");
		ModuleDTO subsystemDTO = new ModuleDTO("test.subsystem", "subsystem", "subsystem", new ModuleDTO[] {});
		ModuleDTO componentDTO = new ModuleDTO("test.component", "component", "component", new ModuleDTO[] {});
		ModuleDTO externalLibraryDTO = new ModuleDTO("xLibrary.externalLibrary", "externalLibrary", "library", new ModuleDTO[] {});

		analysedController.drawSingleLevelModules(new AbstractDTO[] { layerDTO, subsystemDTO, componentDTO, externalLibraryDTO });

		assertEquals("wrong amount of figures drawn", 4, analysedController.getDrawing().getChildren().size());

		for (Figure f : analysedController.getDrawing().getChildren()) {
			if (!(f instanceof BaseFigure)) {
				fail("non-basefigure in drawing");
			}

			BaseFigure baseF = (BaseFigure) f;

			assertTrue("module figure says not to be a module", baseF.isModule());
		}
	}

	@Test
	public void drawMultiLevelModulesTest() {
		HashMap<String, ArrayList<AbstractDTO>> multiLevelDTOs = new HashMap<String, ArrayList<AbstractDTO>>();

		ModuleDTO classDTO = new ModuleDTO("parent.class", "class", "class", new ModuleDTO[] {});
		ModuleDTO interfaceDTO = new ModuleDTO("parent.interface", "interface", "interface", new ModuleDTO[] {});
		ModuleDTO abstractClassDTO = new ModuleDTO("parent.abstractClass", "abstractClass", "abstract", new ModuleDTO[] {});

		SoftwareUnitDTO analysedClassDTO = new SoftwareUnitDTO("parent.analysedChild", "analysedChild", "class", "public");

		ArrayList<AbstractDTO> childModules = new ArrayList<AbstractDTO>();
		childModules.add(classDTO);
		childModules.add(interfaceDTO);
		childModules.add(abstractClassDTO);
		childModules.add(analysedClassDTO);

		multiLevelDTOs.put("parent", childModules);

		analysedController.drawMultiLevelModules(multiLevelDTOs);

		assertEquals("wrong amount of figure drawn", 5, analysedController.getDrawing().getChildren().size());

		for (Figure f : analysedController.getDrawing().getChildren()) {
			if (!(f instanceof BaseFigure)) {
				fail("non-basefigure in drawing");
			}

			BaseFigure baseF = (BaseFigure) f;

			if (f instanceof ParentFigure) {
				BaseFigure[] children = ((ParentFigure) f).getChildFigures();
				assertEquals("wrong amount of children in parent", 4, children.length);
				assertEquals("unexpected child figure", "class", children[0].getName());
				assertEquals("unexpected child figure", "interface", children[1].getName());
				assertEquals("unexpected child figure", "abstractClass", children[2].getName());
				assertEquals("unexpected child figure", "analysedChild", children[3].getName());
			} else if (f instanceof AbstractClassFigure) {
				assertSame("wrong dto for abstract class figure", abstractClassDTO, analysedController.getFigureMap().getModuleDTO(baseF));
			} else if (f instanceof InterfaceFigure) {
				assertSame("wrong dto for interface figure", interfaceDTO, analysedController.getFigureMap().getModuleDTO(baseF));
			} else if (f instanceof ClassFigure) {
				if (baseF.getName().equals("class")) {
					assertSame("wrong dto for class figure", classDTO, analysedController.getFigureMap().getModuleDTO(baseF));
				} else if (baseF.getName().equals("analysedChild")) {
					assertSame("wrong analysed dto for class figure", analysedClassDTO, analysedController.getFigureMap().getModuleDTO(baseF));
				} else {
					fail("unexpected class figure in drawing");
				}
			} else {
				fail("unexpected type of figure in drawing");
			}
		}
	}

	@Test
	public void drawDependenciesBetweenTest() {
		SoftwareUnitDTO dtoFrom = new SoftwareUnitDTO("test.from", "from", "class", "public");
		SoftwareUnitDTO dtoTo = new SoftwareUnitDTO("test.to", "to", "class", "public");

		analysedController.drawSingleLevelModules(new AbstractDTO[] { dtoFrom, dtoTo });

		DependencyDTO dep1 = new DependencyDTO("from", "to", "Call", "method", 20, false, false);
		DependencyDTO dep2 = new DependencyDTO("from", "to", "Call", "method", 21, false, false);

		BaseFigure figFrom = null;
		BaseFigure figTo = null;
		for (Figure f : analysedController.getDrawing().getChildren()) {
			if (f instanceof BaseFigure) {
				AbstractDTO figDTO = analysedController.getFigureMap().getModuleDTO((BaseFigure) f);
				if (figDTO == dtoFrom) {
					figFrom = (BaseFigure) f;
				}
				if (figDTO == dtoTo) {
					figTo = (BaseFigure) f;
				}
			}
		}

		assertNotNull("figure from not found in drawing", figFrom);
		assertNotNull("figure to not found in drawing", figTo);

		analysedController.drawDependenciesBetween(new DependencyDTO[] { dep1, dep2 }, figFrom, figTo);

		ArrayList<RelationFigure> relationFigures = new ArrayList<RelationFigure>();
		for (Figure f : analysedController.getDrawing().getChildren()) {
			if (!(f instanceof BaseFigure)) {
				fail("found figure not a base figure");
			}

			BaseFigure baseF = (BaseFigure) f;

			if (baseF.isLine()) {
				if (!(baseF instanceof RelationFigure)) {
					fail("found line not a relation figure");
				}

				relationFigures.add((RelationFigure) baseF);
			}
		}

		assertEquals("unexpected number of lines found", 1, relationFigures.size());

		for (RelationFigure relationFigure : relationFigures) {
			assertSame("wrong from figure", figFrom, relationFigure.getStartFigure());
			assertSame("wrong to figure", figTo, relationFigure.getEndFigure());

			DependencyDTO[] depDTOs = analysedController.getFigureMap().getDependencyDTOs(relationFigure);
			assertSame("wrong dto", dep1, depDTOs[0]);
			assertSame("wrong dto", dep2, depDTOs[1]);
		}
	}

}
