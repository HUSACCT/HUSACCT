package husacct.graphics.task.modulelayout;

import husacct.common.dto.SoftwareUnitDTO;
import husacct.graphics.domain.figures.BaseFigure;
import husacct.graphics.task.modulelayout.layered.CreateLayersInGraphic;
import husacct.graphics.task.modulelayout.layered.LayoutStrategy;
import org.jhotdraw.draw.AbstractCompositeFigure;
import org.jhotdraw.draw.Figure;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class LayeredLayoutStrategy implements LayoutStrategy {

	TreeMap<Integer, List<BaseFigure>> treeMap;
	AbstractCompositeFigure drawing;

	public LayeredLayoutStrategy(AbstractCompositeFigure theDrawing) {
		drawing = theDrawing;
	}

	@Override
	public void doLayout() {
		treeMap = createTreeMapFromFigures();
		List<SoftwareUnitDTO> list = createSoftwareDTO_FromTreeMap();
		TreeMap<Integer, ArrayList<SoftwareUnitDTO>> anotherTreeMap = new CreateLayersInGraphic().executeAlgorithm(list);
		drawing.getChildren();
		for (int i = 0; i < anotherTreeMap.keySet().size(); i++){
			List<SoftwareUnitDTO> softwareUnitDTOs = anotherTreeMap.get(i);
			for (SoftwareUnitDTO dto : softwareUnitDTOs) {
				Figure figure = null;
				for (Figure f : drawing.getChildren()){
					if (((BaseFigure)f).getUniqueName().equals(dto.uniqueName)){
						setLayout(f);
						break;
					}
				}
			}
		}
	}

	private TreeMap<Integer, List<BaseFigure>> createTreeMapFromFigures() {
		List<Figure> figures = drawing.getChildren();
		TreeMap<Integer, List<BaseFigure>> treeMap = new TreeMap<>();
		int level = 0;
		List<BaseFigure> subList = new ArrayList<>();
		treeMap.put(level, subList);
		for (Figure figure: figures) {
			BaseFigure f = (BaseFigure) figure;
			if (f.isParent()) {
				subList = new ArrayList<>();
				level++;
				treeMap.put(level, subList);
			}
			subList.add(f);
		}
		return treeMap;
	}


	private List<SoftwareUnitDTO> createSoftwareDTO_FromTreeMap() {
		List<BaseFigure> list = new ArrayList<>();
		list.addAll(treeMap.get(0));
		for (int i = 1; i < treeMap.size(); i ++) {
			list.add(treeMap.get(i).get(0));
		}
		return createSoftwareDTO_FromArrayList(list);
	}

	private List<SoftwareUnitDTO> createSoftwareDTO_FromArrayList(List<BaseFigure> figures){
		ArrayList<SoftwareUnitDTO> softwareUnitDTOs = new ArrayList<>();
		for (BaseFigure f : figures) {
			SoftwareUnitDTO dto = new SoftwareUnitDTO(f.getUniqueName(), f.getName(), f.getType(), ""+f.isVisible());
			softwareUnitDTOs.add(dto);
		}
		return softwareUnitDTOs;
	}

	private void setLayout(Figure f) {

	}
}
