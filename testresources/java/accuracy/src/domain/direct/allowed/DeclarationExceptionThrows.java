package domain.direct.allowed;

import technology.direct.dao.StaticsException;

public class DeclarationExceptionThrows {

	public String getStatics() throws StaticsException{
			throw new StaticsException("No statics available");
	}
}