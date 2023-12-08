package main.java.com.parkit.parkingsystem.service;

import main.java.com.parkit.parkingsystem.constants.Fare;
import main.java.com.parkit.parkingsystem.model.Ticket;
//import java.time.Duration;

public class FareCalculatorService {
	
	
	

    public void calculateFare(Ticket ticket, boolean discount){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }
        
        long inMili = ticket.getInTime().getTime();
        long outMili = ticket.getOutTime().getTime();

        //TODO: Some tests are failing here. Need to check if this logic is correct
        long duree = outMili - inMili;
        long duration = duree / 60000 ;
     
        switch (ticket.getParkingSpot().getParkingType()){
        case CAR: {
        	if (duration<=30)  {
        		ticket.setPrice(0);
        	}else {if(discount==false){
        		ticket.setPrice(duration *  Fare.CAR_RATE_PER_HOUR/60);
        	}else {
        		ticket.setPrice(0.95*duration *  Fare.CAR_RATE_PER_HOUR/60);
        	}
        	}
        
        break;
        }
    case BIKE: {
    	if (duration<=30) {
    		ticket.setPrice(0);
    	}else {if(discount==false) {
    		ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR/60);
    	}else {
    		ticket.setPrice(0.95*duration * Fare.BIKE_RATE_PER_HOUR/60);
    	}
    	}
    	break;
    
    }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
    }
    
public void calculateFare(Ticket ticket ){
	
	calculateFare( ticket,false);
}
}

