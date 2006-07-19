package org.andromda.android.core.project.cartridge;

import java.util.Map;

/**
 * This interface desribes a precondition of a prompt in the project generator cartridge.
 *
 * @author Peter Friese
 * @since 16.07.2006
 */
public interface IPrecondition
{

    /**
     * @return The ID of this precondition.
     */
    String getId();

    /**
     * @param id The ID of this precondition.
     */
    void setId(String id);

    /**
     * @return The value for equality for this precondition.
     */
    String getEqual();

    /**
     * @param equal The value for equality for this precondition.
     */
    void setEqual(String equal);

    /**
     * @return The value for no equality for this precondition.
     */
    String getNotEqual();

    /**
     * @param notEqual The value for no equality for this precondition.
     */
    void setNotEqual(String notEqual);

    /**
     * Evaluates this precondition against the given project properties.
     *
     * @param projectProperties a map containing the project properties.
     * @return <code>true</code> if the precondition is met, <code>false</code>otherwise.
     */
    boolean evaluate(Map projectProperties);

}
