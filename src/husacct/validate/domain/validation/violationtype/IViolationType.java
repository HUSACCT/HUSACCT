package husacct.validate.domain.validation.violationtype;

import husacct.validate.domain.validation.DefaultSeverities;

public interface IViolationType {

	@Override
	public String toString();

	public String getCategory();

	public DefaultSeverities getDefaultSeverity();
}