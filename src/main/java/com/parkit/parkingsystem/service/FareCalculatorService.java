package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    private static double twoDigitsAfterVirgule(double number, double decimal) {
        return ((Math.round(number * decimal)) / decimal);
    }

    public void calculateFare(Ticket ticket, boolean fee){
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

        if (duration > 0.5) {
            switch (ticket.getParkingSpot().getParkingType()) {
                case CAR: {
                    if (fee) {
                        ticket.setPrice(twoDigitsAfterVirgule(carBillWithFee, 100.0));
                    } else {
                        ticket.setPrice(twoDigitsAfterVirgule(carBill, 100.0));
                    }
                    break;
                }
                case BIKE: {
                    if (fee) {
                        ticket.setPrice(twoDigitsAfterVirgule(bikeBillWithFee, 100.0));
                    } else {
                        ticket.setPrice(twoDigitsAfterVirgule(bikeBill, 100.0));
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