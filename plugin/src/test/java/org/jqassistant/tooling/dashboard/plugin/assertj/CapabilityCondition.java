package org.jqassistant.tooling.dashboard.plugin.assertj;

import org.assertj.core.api.Condition;
import org.jqassistant.tooling.dashboard.plugin.api.model.Capability;

public class CapabilityCondition extends Condition<Capability> {

    private final String expectedType;
    private final String expectedValue;

    private CapabilityCondition(String expectedType, String expectedValue) {
        super("capability '" + expectedType + ":" + expectedValue + "'");
        this.expectedType = expectedType;
        this.expectedValue = expectedValue;
    }

    @Override
    public boolean matches(Capability capability) {
        return expectedType.equals(capability.getType()) && expectedValue.equals(capability.getValue());
    }

    public static CapabilityCondition capability(String expectedType, String expectedValue) {
        return new CapabilityCondition(expectedType, expectedValue);
    }

}
