package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ParkingSpotDAO {
    private static final Logger logger = LogManager.getLogger("ParkingSpotDAO");

    public DataBaseConfig dataBaseConfig = new DataBaseConfig();

    public int getNextAvailableSlot(ParkingType parkingType) throws ClassNotFoundException, IOException {
        Connection con = null;
        int result = -1;

        try {
            con = dataBaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.GET_NEXT_PARKING_SPOT);
            try {
                ps.setString(1, parkingType.toString());
                try (ResultSet rs = ps.executeQuery()){
                    if (rs.next()) {
                        result = rs.getInt(1);
                    }
                    dataBaseConfig.closeResultSet(rs);
                }
            } finally {
                dataBaseConfig.closePreparedStatement(ps);
                dataBaseConfig.closeConnection(con);
            }
        } catch (SQLException e) {
            logger.error("Error fetching next available slot", e);
        }
        return result;
    }

    public boolean updateParking(ParkingSpot parkingSpot) throws ClassNotFoundException, SQLException, IOException {
        // update the availability fo that parking slot
        Connection con = null;
        try {
            con = dataBaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.UPDATE_PARKING_SPOT);
            try {
                ps.setBoolean(1, parkingSpot.isAvailable());
                ps.setInt(2, parkingSpot.getId());
                int updateRowCount = ps.executeUpdate();
                return (updateRowCount == 1);
            } finally {
                dataBaseConfig.closePreparedStatement(ps);
                dataBaseConfig.closeConnection(con);
            }
        } catch (SQLException e) {
            logger.error("Error updating parking info", e);
            return false;
        }
    }

}
