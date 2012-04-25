package husacct.validate.domain.validation.violationtype.csharp;

import husacct.validate.domain.validation.violationtype.IViolationType;

public enum CSharpDependencyRecognition implements IViolationType {
	;

	@Override
	public int getDefaultSeverity() {
		return 0;
	}

	@Override
	public String getCategory() {
		return "";
	}
}