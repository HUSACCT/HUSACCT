package domain.direct.violating;

import technology.direct.dao.StaticsException;

public class DeclarationExceptionThrows {

	public String getStatics() throws StaticsException{
			throw new StaticsException("No statics available");
	}
}