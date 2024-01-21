package org.deustomed;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class DoctorMsgCodeTest {

    @Test
    public void testEncodeAndDecode() {
        String original = "00AAA";
        String encoded = DoctorMsgCode.idToMsgCode(original);
        String decoded = DoctorMsgCode.MsgCodeToId(encoded);
        assertEquals(original, decoded);
    }

    @Test
    public void testEmptyString() {
        String original = "";
        String encoded = DoctorMsgCode.idToMsgCode(original);
        String decoded = DoctorMsgCode.MsgCodeToId(encoded);
        assertEquals( original, decoded);
    }

    @Test
    public void testSpecialCharacters() {
        String original = "!@#$%^&*()";
        String encoded = DoctorMsgCode.idToMsgCode(original);
        String decoded = DoctorMsgCode.MsgCodeToId(encoded);
        assertEquals( original, decoded);
    }

    @Test
    public void testNumbersOnly() {
        String original = "123456";
        String encoded = DoctorMsgCode.idToMsgCode(original);
        String decoded = DoctorMsgCode.MsgCodeToId(encoded);
        assertEquals(original, decoded);
    }

    @Test
    public void testDistinctOutputs() {
        String input1 = "00AAA";
        String input2 = "00AAB";
        String encoded1 = DoctorMsgCode.idToMsgCode(input1);
        String encoded2 = DoctorMsgCode.idToMsgCode(input2);
        assertNotEquals( encoded1, encoded2);
    }
    @Test
    public void testSameOutputs() {
        String input1 = "00AAA";
        String input2 = "00AAA";
        String encoded1 = DoctorMsgCode.idToMsgCode(input1);
        String encoded2 = DoctorMsgCode.idToMsgCode(input2);
        assertEquals(encoded1, encoded2);
    }
}
