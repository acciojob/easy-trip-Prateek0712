package com.driver.controllers;

import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.*;
@Repository
public class AirportRepository {
    HashMap<String, Airport>airportMap=new HashMap<>();
    HashMap<Integer, Flight>flightMap=new HashMap<>();
    HashMap<Integer, Passenger>passengerMap=new HashMap<>();
    HashMap<Integer,List<Integer>>flightData=new HashMap<>();
    HashMap<Integer,List<Integer>>passengerData=new HashMap<>();

    AirportRepository()
    {
    }
    /* <------------Post-------------> */
    public String addAirport(Airport ap)
    {
        airportMap.put(ap.getAirportName(),ap);
        return "SUCCESS";
    }
    public String addFlight(Flight fp)
    {
        if(flightMap.containsKey(fp.getFlightId())==false)
        {
            flightMap.put(fp.getFlightId(),fp);
            flightData.put(fp.getFlightId(),new ArrayList<>());
            return "SUCCESS";
        }
        else
        {
            return null;
        }
    }
    public String addPassenger(Passenger p)
    {
        if(passengerMap.containsKey(p.getPassengerId())==false)
        {
            passengerMap.put(p.getPassengerId(),p);
            passengerData.put(p.getPassengerId(), new ArrayList<>());
            return "SUCCESS";
        }
        else
        {
            return null;
        }
    }

    /* <------------------GET-------------------> */
    public String getLargestAirport()
    {
        if(airportMap.size()>0)
        {
            List<String> l=new ArrayList<>();
            int largest=Integer.MIN_VALUE;
            for(String key:airportMap.keySet())
            {
                largest=Math.max(largest,airportMap.get(key).getNoOfTerminals());
            }
            for(String key:airportMap.keySet())
            {
                if(largest==airportMap.get(key).getNoOfTerminals())
                {
                    l.add(key);
                }
            }
            Collections.sort(l);
            return  l.get(0);
        }
        else
        {
            return "";
        }
    }

    public double getShortestTimeTravelBetweenCities(City from, City to)
    {
        if(flightMap.size()==0)
        {
            return -1;
        }
        double time=Double.MAX_VALUE;
        for(int id:flightMap.keySet())
        {
            if(flightMap.get(id).getFromCity().equals(from) && flightMap.get(id).getFromCity().equals(to))
            {
                time=Math.min(time,flightMap.get(id).getDuration());
            }
        }
        if(time==Double.MAX_VALUE) return -1;
        return time;
    }

    /* <----------------------PUT---------------> */
    public String bookedTicket(int fid,int pid)
    {
        if(flightMap.containsKey(fid)==false || passengerMap.containsKey(pid)==false)
        {
            return "FAILURE";
        }
        if(flightData.get(fid).size()==flightMap.get(fid).getMaxCapacity())
        {
            return "FAILURE";
        }
        if(flightData.get(fid).contains(pid)==true) // change in 2nd  attempt false to true
        {
            return "FAILURE";
        }
        passengerData.get(pid).add(fid);
        flightData.get(fid).add(pid);
        return "SUCCESS";
    }
    public String cancelTicket(int fid,int pid)
    {
        if(flightMap.containsKey(fid)==false || passengerMap.containsKey(pid)==false)
        {
            return "FAILURE";
        }
        else if(flightData.get(fid).contains(pid)==false)
        {
            return "FAILURE";
        }
        else
        {
            flightData.get(fid).remove(pid);
            passengerData.get(pid).remove(fid);
            return "SUCCESS";
        }
        /*if(passengerData.get(pid).contains(fid)==false)  //change after 2nd Attempt this if  condition is commented  out
        {
            return "FAILURE";
        }*/
    }
    public String getTakeOffAirportNameByFlightId(int fid)
    {
        if(flightMap.containsKey(fid)==false)
        {
            return  null;
        }
        Flight f1=flightMap.get(fid);
        for(Airport ap:airportMap.values())
        {
            if(f1.getFromCity().equals(ap.getCity())) return ap.getAirportName();
        }
        return null;
    }
    public int  getFare(int fid)
    {
        if(flightMap.containsKey(fid)==false)
        {
            return  0;
        }
        return 3000+(flightData.get(fid).size()*50);
    }
    public int getNumberOfPeople(Date date,String apName)
    {
        if (flightData.size() == 0 || flightMap.size() == 0 || airportMap.size() == 0) return 0;
        int numberOfPeople = 0;

        for (Flight flight : flightMap.values()) {
            if ((date.equals(flight.getFlightDate()) == true) &&
                    (flight.getFromCity().equals(airportMap.get(apName).getCity()) || flight.getToCity().equals(airportMap.get(apName).getCity()))) {
                numberOfPeople += flightData.get(flight.getFlightId()).size();
            }
        }
        return numberOfPeople;

    }
    public int getBookingCount(int pid)
    {
        if(passengerData.containsKey(pid)==false)
        {
            return 0;
        }
        return passengerData.get(pid).size();
    }
    public int calcRevenue(int fid)
    {
        if(flightData.containsKey(fid)==true)
            return 3000+(flightData.get(fid).size()*50);
        else return 0;
    }
}
