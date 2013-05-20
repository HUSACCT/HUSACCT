package husacct.validate.domain.validation;

import husacct.ServiceProvider;

import java.awt.Color;
import java.util.UUID;

public class Severity implements Cloneable {
    private final UUID id;
    private final String severityKey;
    private Color color;

    public Severity(String severityKey, Color color) {
	this.id = UUID.randomUUID();
	this.severityKey = severityKey;
	this.color = color;
    }

    public Severity(UUID id, String severityKey, Color color) {
	this.id = id;
	this.severityKey = severityKey;
	this.color = color;
    }

    public String getSeverityKey() {
	return severityKey;
    }

    public String getSeverityName() {
	return ServiceProvider.getInstance().getLocaleService()
		.getTranslatedString(severityKey);
    }

    public Color getColor() {
	return color;
    }

    public void setColor(Color color) {
	this.color = color;
    }

    public UUID getId() {
	return id;
    }

    @Override
    public String toString() {
	return getSeverityName();
    }

    @Override
    public boolean equals(Object obj) {
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	final Severity other = (Severity) obj;
	if ((this.severityKey == null) ? (other.severityKey != null)
		: !this.severityKey.equals(other.severityKey)) {
	    return false;
	}
	if (this.color != other.color
		&& (this.color == null || !this.color.equals(other.color))) {
	    return false;
	}
	if (this.id != other.id
		&& (this.id == null || !this.id.equals(other.id))) {
	    return false;
	}
	return true;
    }

    public Severity clone() {
	try {
	    Severity clone = (Severity) super.clone();
	    return clone;
	} catch (CloneNotSupportedException e) {
	    throw new husacct.validate.domain.exception.CloneNotSupportedException(
		    e);
	}
    }

    @Override
    public int hashCode() {
	int hash = 3;
	hash = 73 * hash
		+ (this.severityKey != null ? this.severityKey.hashCode() : 0);
	hash = 73 * hash + (this.color != null ? this.color.hashCode() : 0);
	// hash = 73 * hash + (this.id != null ? this.id.hashCode() : 0);
	return hash;
    }
}