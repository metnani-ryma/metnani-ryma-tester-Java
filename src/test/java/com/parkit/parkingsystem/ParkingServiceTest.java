package test.java.com.parkit.parkingsystem;

import main.java.com.parkit.parkingsystem.constants.ParkingType;
import main.java.com.parkit.parkingsystem.dao.ParkingSpotDAO;
import main.java.com.parkit.parkingsystem.dao.TicketDAO;
import main.java.com.parkit.parkingsystem.model.ParkingSpot;
import main.java.com.parkit.parkingsystem.model.Ticket;
import main.java.com.parkit.parkingsystem.service.ParkingService;
import main.java.com.parkit.parkingsystem.util.InputReaderUtil;
//import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

    private static ParkingService parkingService;

    @Mock
    private static InputReaderUtil inputReaderUtil;
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    @Mock
    private static TicketDAO ticketDAO;
    

	/*
	 * @BeforeEach private void setUpPerTest() { try {
	 * 
	 * //when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
	 * 
	 * 
	 * } catch (Exception e) { e.printStackTrace(); throw new
	 * RuntimeException("Failed to set up test mock objects"); } }
	 */

    @Test
    public void processExitingVehicleTest(){
    	
try {
            
        	
            
       
    	when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
    	ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		Ticket ticket = new Ticket();
		ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
		ticket.setParkingSpot(parkingSpot);
		ticket.setVehicleRegNumber("ABCDEF");
		parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

		
		when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
		when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
		when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
		when(ticketDAO.getNbTicket(anyString())).thenReturn(1);

		parkingService.processExitingVehicle();

		verify(ticketDAO, Mockito.times(1)).getTicket("ABCDEF");
		verify(ticketDAO, Mockito.times(1)).getNbTicket("ABCDEF");
		verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
		verify(ticketDAO, Mockito.times(1)).updateTicket(ticket);
} catch (Exception e) {
    e.printStackTrace();
    throw  new RuntimeException("Failed to set up test mock objects");
}
       
    }
    @Test
	public void testProcessExitingVehicleUnableUpdate() {
    	
try {
            
        	when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
           
            
        
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        
        Ticket ticket = new Ticket();
        
        ticket.setInTime(new Date(System.currentTimeMillis() - (60*60*1000)));
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        
		
		when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
		when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(false); 
		
		parkingService.processExitingVehicle();

		verify(ticketDAO, Mockito.times(1)).getTicket("ABCDEF");
		verify(ticketDAO, Mockito.times(1)).updateTicket(ticket);
		verify(ticketDAO, Mockito.times(1)).getNbTicket("ABCDEF");
		verify(parkingSpotDAO, never()).updateParking(any(ParkingSpot.class));
} catch (Exception e) {
    e.printStackTrace();
    throw  new RuntimeException("Failed to set up test mock objects");
}
	}
    
    
    @Test
    public void testProcessIncomingVehicle() {
    	
        
    	 try {
    		    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
    	        
    	        Ticket ticket = new Ticket();
    	        
    	        ticket.setInTime(new Date(System.currentTimeMillis() - (60*60*1000)));
    	        ticket.setParkingSpot(parkingSpot);
    	        ticket.setVehicleRegNumber("ABCDEF");
    	        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
    	    	
    	        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
    	    	when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
    	    	when(inputReaderUtil.readSelection()).thenReturn(1);
    	    	when(ticketDAO.saveTicket(any(Ticket.class))).thenReturn(true);    	
    	    	when(ticketDAO.getNbTicket(anyString())).thenReturn(1);
    	    	
    	    	parkingService.processIncomingVehicle();

    	    	verify(inputReaderUtil, Mockito.times(1)).readSelection();
    	    	verify(ticketDAO, Mockito.times(1)).getNbTicket(anyString());
    	    	verify(ticketDAO, Mockito.times(1)).saveTicket(any(Ticket.class));
    	    	
             
         	
            
             
         } catch (Exception e) {
             e.printStackTrace();
             throw  new RuntimeException("Failed to set up test mock objects");
         }
        
    }
    
    
    @Test
	public void testGetNextParkingNumberIfAvailable() {

		

		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		Ticket ticket = new Ticket();
		ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
		ticket.setParkingSpot(parkingSpot);
		ticket.setVehicleRegNumber("ABCDEF");
		parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		
		when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
		when(inputReaderUtil.readSelection()).thenReturn(1);

		parkingService.getNextParkingNumberIfAvailable();

		verify(inputReaderUtil, Mockito.times(1)).readSelection();
		verify(parkingSpotDAO, Mockito.times(1)).getNextAvailableSlot(ParkingType.CAR);

	}

	@Test
	public void testGetNextParkingNumberIfAvailableParkingNumberNotFound() {

		

		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		Ticket ticket = new Ticket();
		ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
		ticket.setParkingSpot(parkingSpot);
		ticket.setVehicleRegNumber("ABCDEF");
		parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		
		when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(0);
		when(inputReaderUtil.readSelection()).thenReturn(1);

		parkingService.getNextParkingNumberIfAvailable();

		verify(inputReaderUtil, Mockito.times(1)).readSelection();
		verify(parkingSpotDAO, Mockito.times(1)).getNextAvailableSlot(ParkingType.CAR);

	}

	@Test
	public void testGetNextParkingNumberIfAvailableParkingNumberWrongArgument() {

		

		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		Ticket ticket = new Ticket();
		ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
		ticket.setParkingSpot(parkingSpot);
		ticket.setVehicleRegNumber("ABCDEF");
		 parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		when(inputReaderUtil.readSelection()).thenReturn(3);

		parkingService.getNextParkingNumberIfAvailable();

		verify(inputReaderUtil, Mockito.times(1)).readSelection();

	}


}
