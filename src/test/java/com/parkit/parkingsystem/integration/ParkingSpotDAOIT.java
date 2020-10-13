package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import junit.framework.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Null;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class ParkingSpotDAOIT {

    private static ParkingSpotDAO parkingSpotDAO;
    private static DataBasePrepareService dataBasePrepareService;

    @BeforeEach
    private void setUpPerTest() {
        parkingSpotDAO = new ParkingSpotDAO();
        dataBasePrepareService = new DataBasePrepareService();
        dataBasePrepareService.clearDataBaseEntries();
    }

    @Test
    public void getNextAvailableSlotCarTest() throws IOException, ClassNotFoundException {
        Assert.assertEquals(2, parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR));
    }

    @Test
    public void getNextAvailableSlotBikeTest() throws IOException, ClassNotFoundException {
        Assert.assertEquals(4, parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE));
    }
    @Test
    public void getNextAvailableSlotNullParkingTypeTest() {
        assertThrows(NullPointerException.class, () ->parkingSpotDAO.getNextAvailableSlot(null));
    }

    @Test
    public void updateParkingTest() throws IOException, ClassNotFoundException, SQLException {
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        Assert.assertTrue(parkingSpotDAO.updateParking(parkingSpot));
    }

    @Test
    public void updateParkingWithNoParkingTypeTest() throws IOException, ClassNotFoundException, SQLException {
        ParkingSpot parkingSpot = new ParkingSpot(1, null, false);
        Assert.assertTrue(parkingSpotDAO.updateParking(parkingSpot));
    }

    @Test
    public void updateParkingWithNegativeParkingSlotTest() throws IOException, ClassNotFoundException, SQLException {
        ParkingSpot parkingSpot = new ParkingSpot(-1, ParkingType.CAR, false);
        Assert.assertFalse(parkingSpotDAO.updateParking(parkingSpot));
    }


}
