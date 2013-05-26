package husacct.define.task;

import husacct.define.analyzer.AnalyzedUnitComparator;
import husacct.define.domain.services.WarningMessageService;
import husacct.define.presentation.moduletree.AnalyzedModuleTree;
import husacct.define.task.components.AbstractCombinedComponent;
import husacct.define.task.components.AnalyzedModuleComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class JtreeStateEngine {
    private static JtreeStateEngine instance = null;

    public static JtreeStateEngine instance() {

	if (instance == null) {
	    return instance = new JtreeStateEngine();
	} else {

	    return instance;
	}
    }

    private AnalyzedModuleComponent currentRoot;
    private Logger logger;
    private ArrayList<Map<Long, AbstractCombinedComponent>> orderofinsertions = new ArrayList<Map<Long, AbstractCombinedComponent>>();

    private ArrayList<AnalyzedModuleComponent> reNewedCodes = new ArrayList<AnalyzedModuleComponent>();

    public JtreeStateEngine() {
	logger = Logger.getLogger(JtreeStateEngine.class);
    }

    public void analyze() {
	SoftwareUnitController controller = new SoftwareUnitController(-1);
	if (JtreeController.instance().isLoaded()) {
	    AnalyzedModuleComponent rootComponent = controller
		    .getSoftwareUnitTreeComponents();
	    JtreeStateEngine.instance().compareNewData(rootComponent);

	} else {

	    AnalyzedModuleComponent rootComponent = controller
		    .getSoftwareUnitTreeComponents();

	    JtreeController.instance().setCurrentTree(
		    new AnalyzedModuleTree(rootComponent));

	    JtreeController.instance().setLoadState(true);

	}

    }

    private void compare(AnalyzedModuleComponent newdata) {
	currentRoot = JtreeController.instance().getRootOfModel();

	AnalyzedUnitComparator c = new AnalyzedUnitComparator();
	c.calucalteChanges(currentRoot, newdata);

    }

    public void compareNewData(AnalyzedModuleComponent newdata) {

	importNewData(newdata);

    }

    private void flush() {
	AnalyzedModuleTree mainTree = JtreeController.instance().getTree();
	for (Map<Long, AbstractCombinedComponent> usersequenceinput : orderofinsertions) {
	    for (Long key : usersequenceinput.keySet()) {
		AnalyzedModuleComponent unitTobeRestored = (AnalyzedModuleComponent) usersequenceinput
			.get(key);
		unitTobeRestored.unfreeze();
		if (unitTobeRestored.getType().toUpperCase()
			.equals("REGEX".toUpperCase())) {
		    flushRegix(unitTobeRestored, mainTree);

		} else {
		    mainTree.restoreTreeItem(unitTobeRestored);
		}

	    }

	}

    }

    private void flushRegix(AnalyzedModuleComponent unitTobeRestored,
	    AnalyzedModuleTree mainTree) {
	for (AbstractCombinedComponent result : unitTobeRestored.getChildren()) {
	    AnalyzedModuleComponent restoreUnit = (AnalyzedModuleComponent) result;
	    restoreUnit.unfreeze();
	    mainTree.restoreTreeItem(restoreUnit);
	}

    }

    public void importNewData(final AnalyzedModuleComponent newdata) {

	Thread first = new Thread(new Runnable() {

	    @Override
	    public void run() {
		logger.debug("Starting to reanalyze");
		flush();

	    }
	});

	Thread second = new Thread(new Runnable() {

	    @Override
	    public void run() {

		compare(newdata);

	    }
	});

	Thread third = new Thread(new Runnable() {

	    @Override
	    public void run() {

		restoreFlush();

		System.out.println("Finished Reanalyzing");
	    }
	});

	try {
	    first.start();
	    first.join();
	    second.start();
	    second.join();
	    third.start();

	} catch (InterruptedException e) {

	    e.printStackTrace();
	}

    }

    public void registerCodeRenewal(AnalyzedModuleComponent analyzedModuleToChek) {
	reNewedCodes.add(analyzedModuleToChek);

    }

    public void registerSate(Long id, AbstractCombinedComponent inpute) {
	LinkedHashMap<Long, AbstractCombinedComponent> input = new LinkedHashMap<Long, AbstractCombinedComponent>();
	input.put(id, inpute);

	orderofinsertions.add(0, input);

    }

    public void removeSoftwareUnit(long moduleId,
	    AnalyzedModuleComponent unitTobeRemoved) {

	int index = -1;
	for (int i = 0; i < orderofinsertions.size(); i++) {

	    for (long key : orderofinsertions.get(i).keySet()) {
		AnalyzedModuleComponent unitTochek = (AnalyzedModuleComponent) orderofinsertions
			.get(i).get(key);

		if (unitTochek.getUniqueName().toUpperCase()
			.equals(unitTobeRemoved.getUniqueName().toUpperCase())) {
		    index = i;
		}

	    }
	}

	if (index != -1) {
	    orderofinsertions.remove(index);
	} else {
	    restoreCodeRenewal(unitTobeRemoved);
	}
    }

    private void restoreCodeRenewal(AnalyzedModuleComponent unitTobeRemoved) {
	AnalyzedModuleComponent result = null;
	for (AnalyzedModuleComponent softwareUnit : reNewedCodes) {
	    if (unitTobeRemoved.getUniqueName().toLowerCase()
		    .equals(softwareUnit.getUniqueName().toLowerCase())) {
		result = softwareUnit;

		break;

	    }

	}
	if (result != null) {
	    result.unfreeze();
	    int index = reNewedCodes.indexOf(result);
	    reNewedCodes.remove(index);
	} else {
	    WarningMessageService.getInstance().hasCodeLevelWarning(
		    unitTobeRemoved,
		    WarningMessageService.removalType.fullRemoval);
	}

    }

    private void restoreFlush() {
	AnalyzedModuleTree mainTree = JtreeController.instance().getTree();
	Collections.reverse(orderofinsertions);
	ArrayList<Map<Long, AbstractCombinedComponent>> temp = orderofinsertions;

	orderofinsertions = new ArrayList<Map<Long, AbstractCombinedComponent>>();
	try {
	    for (Map<Long, AbstractCombinedComponent> result1 : temp) {
		for (Long key : result1.keySet()) {
		    AnalyzedModuleComponent unitTobeRestored = (AnalyzedModuleComponent) result1
			    .get(key);
		    if (unitTobeRestored.getType().toUpperCase()
			    .equals("REGEX".toUpperCase())) {
			restoreflushRegix(key, unitTobeRestored, mainTree);

		    } else {
			if (unitTobeRestored.isRemoved()) {
			    WarningMessageService.getInstance()
				    .addCodeLevelWarning(key, unitTobeRestored);
			}
		    }

		}

	    }
	} catch (Exception o) {
	    System.out.println(o.getMessage());
	    o.printStackTrace();
	}
    }

    private void restoreflushRegix(long id,
	    AnalyzedModuleComponent unitTobeRestored,
	    AnalyzedModuleTree mainTree) {

	for (AbstractCombinedComponent result : unitTobeRestored.getChildren()) {
	    AnalyzedModuleComponent child = (AnalyzedModuleComponent) result;
	    if (unitTobeRestored.isRemoved()) {
		WarningMessageService.getInstance().addCodeLevelWarning(id,
			child);
	    }
	}
    }

}