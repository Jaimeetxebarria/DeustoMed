package org.deustomed;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.fonts.inter.FlatInterFont;
import org.deustomed.authentication.AnonymousAuthenticationService;
import org.deustomed.ui.WindowPatient;

import javax.swing.*;

public class DoctorMsgCode {
    private static final int despF = 3;

    //------------------------ID --> Message Code ---------------------------------------------------------------------

    /**
     * Convert doctor ID into the Message code used by the patient recursively
     */
    public static String idToMsgCode(String code) {
        return idToMsgCode2(code, 0);
    }

    private static String idToMsgCode2(String code, int index) {
        if (index == code.length()) {
            return "";
        }

        char actualChar = code.charAt(index);
        int desp = despF + index;

        if (Character.isDigit(actualChar)) {
            int num = actualChar - '0';
            num = (num + desp) % 10;
            actualChar = (char) ('K' + num);
        } else if (Character.isLetter(actualChar)) {
            actualChar = (char) ((actualChar - 'A' + desp) % 26 + 'A');
        }

        return actualChar + idToMsgCode2(code, index + 1);
    }

    //---------------------- Message Code --> ID ---------------------------------------------------------------------

    /**
     * Convert Message Code into the doctor ID recursively
     */
    public static String MsgCodeToId(String code) {
        return MsgCodeToId2(code, 0);
    }

    private static String MsgCodeToId2(String code, int index) {
        if (index == code.length()) {
            return "";
        }

        char actualChar = code.charAt(index);
        int desp = despF + index;

        if (actualChar >= 'K' && actualChar <= 'T') {
            int num = actualChar - 'K';
            num = (num + 10 - desp) % 10;
            actualChar = (char) ('0' + num);
        } else if (Character.isLetter(actualChar)) {
            actualChar = (char) ((actualChar - 'A' + 26 - desp) % 26 + 'A');
        }

        return actualChar + MsgCodeToId2(code, index + 1);
    }
}

