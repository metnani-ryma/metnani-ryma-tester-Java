package test.java.com.parkit.parkingsystem.integration;

import main.java.com.parkit.parkingsystem.dao.ParkingSpotDAO;

import main.java.com.parkit.parkingsystem.dao.TicketDAO;
import main.java.com.parkit.parkingsystem.model.ParkingSpot;
import main.java.com.parkit.parkingsystem.constants.Fare;
import main.java.com.parkit.parkingsystem.model.Ticket;
import test.java.com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import test.java.com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import main.java.com.parkit.parkingsystem.service.ParkingService;
import main.java.com.parkit.parkingsystem.constants.ParkingType;
import main.java.com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import java.util.Date;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;



@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

	private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
	private static ParkingSpotDAO parkingSpotDAO;
	private static TicketDAO ticketDAO;
	private static DataBasePrepareService dataBasePrepareService;
	private final String vehicleRegNumber = "ABCDEF";

	@Mock
	private static InputReaderUtil inputReaderUtil;

	@BeforeAll
	private static void setUp() throws Exception{
		parkingSpotDAO = new ParkingSpotDAO();
		parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
		ticketDAO = new TicketDAO();
		ticketDAO.dataBaseConfig = dataBaseTestConfig;
		dataBasePrepareService = new DataBasePrepareService();
	}

	@BeforeEach
	private void setUpPerTest() throws Exception {

		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
		dataBasePrepareService.clearDataBaseEntries();
	}


	@Test
	public void testParkingACar(){
		when(inputReaderUtil.readSelection()).thenReturn(1);

		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		parkingService.processIncomingVehicle();
		Ticket ticket = ticketDAO.getTicket(vehicleRegNumber);

		assertNotNull(ticket);
		assertEquals(ParkingType.CAR, ticket.getParkingSpot().getParkingType());
		assertFalse(ticket.getParkingSpot().isAvailable());

	}

	@Test public void testParkingABike(){
		when(inputReaderUtil.readSelection()).thenReturn(2);

		ParkingService parkingService = new ParkingService(inputReaderUtil,parkingSpotDAO, ticketDAO); parkingService.processIncomingVehicle();
		Ticket ticket = ticketDAO.getTicket(vehicleRegNumber);

		assertNotNull(ticket); assertEquals(ParkingType.BIKE,ticket.getParkingSpot().getParkingType());
		assertFalse(ticket.getParkingSpot().isAvailable());

	}


	@Test
	public void testParkingLotExit() {

		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

		Ticket ticket = new Ticket();
		ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
		ticket.setVehicleRegNumber(vehicleRegNumber);
		ticket.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, false));
		ticketDAO.saveTicket(ticket);
		
		parkingService.processExitingVehicle();

		ticket = ticketDAO.getTicket(vehicleRegNumber);

		assertTrue(ticket.getPrice() >= 0);
		assertNotNull(ticket.getOutTime());
	}

	@Test
	public void testParkingLotExitRecurringUser() {

		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

		Ticket firstTicket = new Ticket();
		firstTicket.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, false));
		firstTicket.setVehicleRegNumber(vehicleRegNumber);
		firstTicket.setInTime(new Date(System.currentTimeMillis() - 4 * (60 * 60 * 1000)));
		firstTicket.setOutTime(new Date(System.currentTimeMillis() - 3 * (60 * 60 * 1000)));
		firstTicket.setPrice(1.5);
		ticketDAO.saveTicket(firstTicket);

		Ticket secondTicket = new Ticket();
		secondTicket.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, false));
		secondTicket.setVehicleRegNumber(vehicleRegNumber);
		secondTicket.setInTime(new Date(System.currentTimeMillis() - 2 * (60 * 60 * 1000)));
		ticketDAO.saveTicket(secondTicket);

		parkingService.processExitingVehicle();

		secondTicket = ticketDAO.getTicket(vehicleRegNumber);
		long duration = (secondTicket.getOutTime().getTime() - secondTicket.getInTime().getTime()) / (60000);	
		double expectedFare = 0.95*Fare.CAR_RATE_PER_HOUR * duration/60;
		assertEquals(expectedFare, secondTicket.getPrice(), 0.1);

	}

}
