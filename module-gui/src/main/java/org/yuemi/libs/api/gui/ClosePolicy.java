package org.yuemi.libs.api.gui;

/**
 * Defines the behavior when a user interface GUI is closed.
 */
public enum ClosePolicy {

    /**
     * Closes the GUI normally, invoking any registered close/cancel callbacks.
     */
    CLOSE,

    /**
     * Prevents closing by automatically reopening the GUI.
     */
    REOPEN,

    /**
     * Closes the GUI, but does not invoke any close/cancel callbacks.
     */
    NOTHING
}
