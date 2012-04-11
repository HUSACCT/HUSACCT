package husacct.validate.domain.violation;



import java.util.ArrayList;
import java.util.List;

public class ViolationStub {
	public List<Violation> getViolations() {
		List<Violation> violations = new ArrayList<Violation>();
		Violation v1 = new Violation();
		v1.setViolationtypeKey("Implements");
		v1.setIndirect(false);
		v1.setLinenumber(101);
		v1.setMessage("Module c1 is not allowed to use library Validate");
		v1.setRuletypeKey("IsAllowedToUse");
		v1.setSeverityValue(2);
		v1.setClassPathFrom("m1.p1.c1");
		v1.setLogicalModuleToType("Module");
		v1.setClassPathTo("hu.sacct.validate.Validate");
		v1.setLogicalModuleFromType("Library");
		v1.setLogicalModuleFrom("Module.module");

		Violation v2 = new Violation();
		v2.setViolationtypeKey("Implements");
		v2.setIndirect(true);
		v2.setLinenumber(50);
		v2.setMessage("Module module is not allowed to use module2");
		v2.setRuletypeKey("IsNotAllowedToUse");
		v2.setSeverityValue(1);
		v2.setClassPathFrom("Module.module");
		v2.setLogicalModuleFrom("Module.module");
		v2.setClassPathTo("module.module2");
		v2.setLogicalModuleFromType("Module");
		v2.setLogicalModuleToType("Module");

		Violation v3 = new Violation();
		v3.setViolationtypeKey("Extends");
		v3.setIndirect(true);
		v3.setLinenumber(50);
		v3.setMessage("Module module is not allowed to use module2");
		v3.setRuletypeKey("IsAllowedToUse");
		v3.setSeverityValue(1);
		v3.setClassPathFrom("Module.module");
		v3.setClassPathTo("module.module2");
		v3.setLogicalModuleFromType("Module");
		v3.setLogicalModuleToType("Module");
		v3.setLogicalModuleFrom("Module.module.module");

		Violation v4 = new Violation();
		v4.setViolationtypeKey("Implements");
		v4.setIndirect(true);
		v4.setLinenumber(50);
		v4.setMessage("Module module is not allowed to use module2");
		v4.setRuletypeKey("IsNotAllowedToUse");
		v4.setSeverityValue(5);
		v4.setClassPathFrom("Module.module");
		v4.setClassPathTo("module.module2");
		v4.setLogicalModuleFromType("Module");
		v4.setLogicalModuleToType("Module");
		v4.setLogicalModuleFrom("Model.mooi");

		violations.add(v2);
		violations.add(v1);
		violations.add(v3);
		violations.add(v4);
		return violations;
	}
}