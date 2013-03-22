package husacct.validate.domain.validation.internal_transfer_objects;

import husacct.validate.domain.validation.Severity;

public class ViolationsPerSeverity {

    private final int amount;
    private final Severity severity;

    public ViolationsPerSeverity(int amount, Severity severity) {
        this.amount = amount;
        this.severity = severity;
    }

    public int getAmount() {
        return amount;
    }

    public Severity getSeverity() {
        return severity;
    }
}