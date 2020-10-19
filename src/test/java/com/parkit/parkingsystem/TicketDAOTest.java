package com.parkit.parkingsystem;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import java.sql.Date;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.InputReaderUtil;

@ExtendWith(MockitoExtension.class)
public class TicketDAOTest {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;

    @Mock
    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    private static void setUp() {

        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
        dataBasePrepareService.clearDataBaseEntries();
    }

    @BeforeEach
    private void setUpPerTest() throws Exception {
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        dataBasePrepareService.clearDataBaseEntries();
    }

    @AfterAll
    private static void closeTest() {
        dataBasePrepareService.clearDataBaseEntries();
    }

    @Test // Test the method saveTicket()
    public void saveTicketTest() {
        String vehicleRegNumber = inputReaderUtil.readVehicleRegistrationNumber();

        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        Ticket ticket = new Ticket();
        ticket.setInTime(new Date(System.currentTimeMillis()));
        ticket.setOutTime(null);
        ticket.setParkingSpot(parkingSpot);
        ticket.setPrice(0.0);
        ticket.setVehicleRegNumber("ABCDEF");

        assertTrue(ticketDAO.saveTicket(ticket));
    }

    @Test // Test the method getNextAvailableSlot() with no data
    public void saveTicketWithNoDataTest() {
        String vehicleRegNumber = inputReaderUtil.readVehicleRegistrationNumber();
        Ticket ticket = new Ticket();

        assertFalse(ticketDAO.saveTicket(ticket));
    }

    @Test // Test the method getTicket() with no data
    public void getTicketNullTest() {
        String vehicleRegNumber = inputReaderUtil.readVehicleRegistrationNumber();

        assertNull(ticketDAO.getTicket("AAAAA"));
    }

    @Test // Test the method getTicket()
    public void getTicketTest() {
        String vehicleRegNumber = inputReaderUtil.readVehicleRegistrationNumber();

        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        Ticket ticket = new Ticket();
        ticket.setInTime(new Date(System.currentTimeMillis()));
        ticket.setOutTime(null);
        ticket.setParkingSpot(parkingSpot);
        ticket.setPrice(0.0);
        ticket.setVehicleRegNumber("ABCDEF");
        ticketDAO.saveTicket(ticket);

        assertNotNull(ticketDAO.getTicket(vehicleRegNumber));
    }

    @Test // Test the method getVehicleRegNumberFromPastUsers() with recurrent user
    public void getVehicleRegNumberFromPastUsersTest() throws Exception {
        String vehicleRegNumber = inputReaderUtil.readVehicleRegistrationNumber();

        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        Ticket ticket = new Ticket();
        ticket.setInTime(new Date(System.currentTimeMillis() - (26 * 60 * 60 * 1000)));
        ticket.setOutTime(new Date(System.currentTimeMillis() - (25 * 60 * 60 * 1000)));
        ticket.setParkingSpot(parkingSpot);
        ticket.setPrice(1.5);
        ticket.setVehicleRegNumber("ABCDEF");
        ticketDAO.saveTicket(ticket);

        ticket.setInTime(new Date(System.currentTimeMillis() - (2 * 60 * 60 * 1000)));
        ticket.setOutTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
        ticket.setParkingSpot(parkingSpot);
        ticket.setPrice(1.5);
        ticket.setVehicleRegNumber("ABCDEF");
        ticketDAO.saveTicket(ticket);

        assertEquals(2, ticketDAO.getVehicleRegNumberFromPastUsers(vehicleRegNumber));
    }

    @Test // Test getVehicleRegNumberFromPastUsersTest() with no recurrent user
    public void getVehicleRegNumberFromPastUsersWithNoDataTest() throws Exception {
        String vehicleRegNumber = inputReaderUtil.readVehicleRegistrationNumber();

        assertEquals(0, ticketDAO.getVehicleRegNumberFromPastUsers("AA"));
    }

    @Test // Test the updateTicket() method
    public void updateTicketTest() throws Exception {

        String vehicleRegNumber = inputReaderUtil.readVehicleRegistrationNumber();

        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        Ticket ticket = new Ticket();
        ticket.setInTime(new Date(System.currentTimeMillis() - (3 * 60 * 60 * 1000)));
        ticket.setOutTime(null);
        ticket.setParkingSpot(parkingSpot);
        ticket.setPrice(0.0);
        ticket.setVehicleRegNumber("ABCDEF");
        ticketDAO.saveTicket(ticket);

        ticket.setPrice(1.5);
        ticket.setOutTime(new Date(System.currentTimeMillis() - (2 * 60 * 60 * 1000)));

        if (ticketDAO.updateTicket(ticket)) {
            parkingSpot.setAvailable(true);
        } else {
            parkingSpot.setAvailable(false);
        }
        assertTrue(parkingSpot.isAvailable());
    }

    @Test // Test the updateTicket() method with no data to update
    public void updateTicketWithNoDataTest() throws Exception {

        String vehicleRegNumber = inputReaderUtil.readVehicleRegistrationNumber();

        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        Ticket ticket = new Ticket();
        ticket.setInTime(new Date(System.currentTimeMillis() - (3 * 60 * 60 * 1000)));
        ticket.setOutTime(null);
        ticket.setParkingSpot(parkingSpot);
        ticket.setPrice(0.0);
        ticket.setVehicleRegNumber("ABCDEF");
        ticketDAO.saveTicket(ticket);

        if (ticketDAO.updateTicket(ticket)) {
            parkingSpot.setAvailable(true);
        } else {
            parkingSpot.setAvailable(false);
        }
        assertFalse(parkingSpot.isAvailable());
    }
}