/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robotrader.adapter.core;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

/**
 *
 * @author aav
 */
public interface MoexProxy {
    @GET
    @Path("engines/stock/markets/shares/securities/{securityCode}/candles.xml")
    String getCandles(@PathParam("securityCode") String securityCode, @QueryParam("from") String from, @QueryParam("till") String till, @QueryParam("interval") int interval);
    
    @GET
    @Path("engines/stock/markets/shares/boards/TQBR/securities.xml")
    String getSecurities();
}
