package org.deustomed.chat;

public interface MessageCheckerThread {

    /**
     * Starts the message checker thread
     */

    void messageCheckerStart();

    /**
     * Stops the message checker thread
     */

    void messageThreadInterrupt();

    /**
     * Gets the full name (Name + 2 surnames) of a doctor given its id
     * @param doctorId
     */

    String getDoctorName(String doctorId);

    /**
     * Gets the full name (Name + 2 surnames) of a patient given its id
     * @param patientId
     */

    String getPatientName(String patientId);

}
