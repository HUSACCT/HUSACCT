package husacct.validate.domain.configuration;

import husacct.validate.domain.exception.SeverityChangedException;
import husacct.validate.domain.exception.SeverityNotFoundException;
import husacct.validate.domain.validation.DefaultSeverities;
import husacct.validate.domain.validation.Severity;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

class SeverityConfigRepository {

    private List<Severity> currentSeverities;
    private final List<Severity> defaultSeverities;

    public SeverityConfigRepository() {
        this.currentSeverities = new ArrayList<Severity>();
        this.defaultSeverities = generateDefaultSeverities();

        initializeCurrentSeverities();
    }

    void restoreToDefault() {
        initializeCurrentSeverities();
    }

    private void initializeCurrentSeverities() {
        this.currentSeverities = new ArrayList<Severity>(defaultSeverities.size());
        for (Severity severity : defaultSeverities) {
            currentSeverities.add(severity);
        }
    }

    Severity getSeverityByName(String severityName) {
        for (Severity customSeverity : currentSeverities) {
            if (!severityName.isEmpty() && (severityName.toLowerCase().equals(customSeverity.getSeverityKey().toLowerCase()) || severityName.toLowerCase().equals(customSeverity.getSeverityName().toLowerCase()))) {
                return customSeverity;
            }
        }
        throw new SeverityNotFoundException(severityName);
    }

    int getSeverityValue(Severity severity) {
        return currentSeverities.indexOf(severity);
    }

    List<Severity> getAllSeverities() {
        return currentSeverities;
    }

    /**
     * @throws SeverityChangedException
     */
    void setSeverities(List<Severity> newSeverities) {
        isSeverityKeyOrOrderChanged(newSeverities);
        this.currentSeverities = newSeverities;
    }

    private void isSeverityKeyOrOrderChanged(List<Severity> newSeverities) {
        if (newSeverities.size() != defaultSeverities.size()) {
            for (int i = 0; i < newSeverities.size(); i++) {
                Severity defaulSeverity = defaultSeverities.get(i);
                Severity newSeverity = newSeverities.get(i);

                if (!defaulSeverity.getSeverityKey().toLowerCase().equals(newSeverity.getSeverityKey().toLowerCase())) {
                    throw new SeverityChangedException(newSeverity.getSeverityKey());
                }
            }
        }
    }

    private List<Severity> generateDefaultSeverities() {
        List<Severity> newDefaultSeverities = new ArrayList<Severity>();
        for (DefaultSeverities defaultSeverity : EnumSet.allOf(DefaultSeverities.class)) {
            Severity severity = new Severity(defaultSeverity.toString(), defaultSeverity.getColor());
            newDefaultSeverities.add(severity);
        }
        return newDefaultSeverities;
    }
}