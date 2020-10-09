package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        long inHour = ticket.getInTime().getTime();
        long outHour = ticket.getOutTime().getTime();
        double duration = (double) (outHour - inHour) / 3600000;

        double carBill = Math.ceil(duration) * Fare.CAR_RATE_PER_HOUR;
        double carBillWithFee = Math.ceil(duration)  * Fare.CAR_RATE_PER_HOUR_WITH_FEE;
        double bikeBill = Math.ceil(duration) * Fare.BIKE_RATE_PER_HOUR;
        double bikeBillWithFee = Math.ceil(duration)  * Fare.BIKE_RATE_PER_HOUR_WITH_FEE;

        TicketDAO ticketDAO = new TicketDAO();
        int count = ticketDAO.getVehicleRegNumberFromPastUsers(ticket.getVehicleRegNumber());

        if (duration > 0.5) {
            switch (ticket.getParkingSpot().getParkingType()) {
                case CAR: {
                    if (count >= 2) {
                        ticket.setPrice(carBillWithFee);
                    } else {
                        ticket.setPrice(carBill);
                    }
                    break;
                }
                case BIKE: {
                    if (count >= 2) {
                        ticket.setPrice(bikeBillWithFee);
                    } else {
                        ticket.setPrice(bikeBill);
                    }
                    break;
                }
                default:
                    throw new IllegalArgumentException("Unknown Parking Type");
            }
        } else {
            ticket.setPrice(0.0);
        }
    }
}