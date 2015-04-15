package technology.direct.dao;

import husacct.validate.domain.validation.DefaultSeverities;

public enum TipDAO {
	ONE,TWO,THREE,FOUR;
	
	TipDAO(String key) {
		this.key = key;
	}

	private final String key;

	public String toString() {
		return key;
	}
}