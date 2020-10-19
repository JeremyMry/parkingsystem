package com.parkit.parkingsystem;

import com.parkit.parkingsystem.util.InputReaderUtil;
import junit.framework.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertThrows;


public class InputReadUtilTest {

    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    private static void setUp() {
        inputReaderUtil = new InputReaderUtil();
    }

    @Test // Test the readSelection() method when the input is correct
    public void InputCanBeParseToInt() {
        String data = "1";
        InputStream in = new ByteArrayInputStream(data.getBytes());
        System.setIn(in);

        Assert.assertEquals(1, inputReaderUtil.readSelection());
    }

    @Test // Test the readSelection() method when the input is incorrect
    public void InputCannotBeParseToInt() {
        String data = "eeeeee";
        InputStream in = new ByteArrayInputStream(data.getBytes());
        System.setIn(in);

        Assert.assertEquals(-1, inputReaderUtil.readSelection());
    }

    @Test // Test the readSelection() method when the input is empty
    public void InputIsEmpty() {
        String data = "";
        InputStream in = new ByteArrayInputStream(data.getBytes());
        System.setIn(in);

        Assert.assertEquals(-1, inputReaderUtil.readSelection());
    }

    @Test // Test the readVehicleRegistrationNumber() method when the input is correct
    public void VehicleRegNumberIsCorrect() {
        String data = "12345AZE6";
        InputStream in = new ByteArrayInputStream(data.getBytes());
        System.setIn(in);

        Assert.assertEquals("12345AZE6", inputReaderUtil.readVehicleRegistrationNumber());
    }

    @Test // Test the readVehicleRegistrationNumber() method when the input is too long
    public void VehicleRegNumberIsTooLong() {
        String data = "12345678910";
        InputStream in = new ByteArrayInputStream(data.getBytes());
        System.setIn(in);

        assertThrows(IllegalArgumentException.class, () -> inputReaderUtil.readVehicleRegistrationNumber());
    }

    @Test  // Test the readVehicleRegistrationNumber() method when the input is empty
    public void VehicleRegNumberIsEmpty() {
        String data = "";
        InputStream in = new ByteArrayInputStream(data.getBytes());
        System.setIn(in);

        assertThrows(NoSuchElementException.class, () -> inputReaderUtil.readVehicleRegistrationNumber());
    }
}
