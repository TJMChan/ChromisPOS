//    Chromis POS  - The New Face of Open Source POS
//    Copyright (c) 2015 
//    http://www.chromis.co.uk
//
//    This file is part of Chromis POS
//
//     Chromis POS is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    Chromis POS is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with Chromis POS.  If not, see <http://www.gnu.org/licenses/>.
package uk.chromis.pos.sales.restaurant;

import java.util.logging.Level;
import java.util.logging.Logger;
import uk.chromis.data.loader.Datas;
import uk.chromis.data.loader.PreparedSentence;
import uk.chromis.data.loader.SerializerReadString;
import uk.chromis.data.loader.SerializerReadInteger;
import uk.chromis.data.loader.SerializerWriteBasic;
import uk.chromis.data.loader.SerializerWriteString;
import uk.chromis.data.loader.Session;
import uk.chromis.data.loader.StaticSentence;
import uk.chromis.pos.forms.AppView;
import uk.chromis.pos.forms.DataLogicSystem;

/**
 *
 * @author JDL
 * 
 * Fixed by TJMChan on 2018-May-06
 * 
 */
public class RestaurantDBUtils {

    public final static int TRUE = 1;
    public final static int FALSE = 0;
    private Session m_s;
    private Object m_result;
    private AppView m_App;

    /**
     *
     */
    protected DataLogicSystem dlSystem;

    /**
     *
     * @param oApp
     */
    public RestaurantDBUtils(AppView oApp) {
        m_App = oApp;
        m_s = m_App.getSession();
    }

    /**
     *
     * @param newTable
     * @param ticketID
     */
    public void moveCustomer(String newTable, String ticketID) {
        String oldTable = getTableDetails(ticketID);
        if (countTicketIdInTable(ticketID) > 1) {
            setCustomerNameInTable(getCustomerNameInTable(oldTable), newTable);
            setWaiterNameInTable(getWaiterNameInTable(oldTable), newTable);
            setTicketIdInTable(ticketID, newTable);
            // remove the data for the old table 
            oldTable = getTableMovedName(ticketID);
            if ((oldTable != null) && (oldTable != newTable)) {
                clearCustomerNameInTable(oldTable);
                clearWaiterNameInTable(oldTable);
                clearTicketIdInTable(oldTable);
                clearTableMovedFlag(oldTable);
            } else {
                oldTable = getTableMovedName(ticketID);
                clearTableMovedFlag(oldTable);
            }
        } else {
            clearTableMovedFlag(oldTable);
        }
    }

    /**
     *
     * @param custName
     * @param tableName
     */
    public void setCustomerNameInTable(String custName, String tableName) {
        try {
            new StaticSentence(m_s, "UPDATE PLACES SET CUSTOMER=? WHERE NAME=?", new SerializerWriteBasic(new Datas[]{
                Datas.STRING,
                Datas.STRING
            })).exec(custName, tableName);
        } catch (Exception e) {
            Logger.getLogger(RestaurantDBUtils.class.getName()).log(Level.WARNING, null, e);
        }
    }

    /**
     *
     * @param custName
     * @param tableID
     */
    public void setCustomerNameInTableById(String custName, String tableID) {
        try {
            new StaticSentence(m_s, "UPDATE PLACES SET CUSTOMER=? WHERE ID=?", new SerializerWriteBasic(new Datas[]{
                Datas.STRING,
                Datas.STRING
            })).exec(custName,tableID);
        } catch (Exception e) {
            Logger.getLogger(RestaurantDBUtils.class.getName()).log(Level.WARNING, null, e);
        }
    }

    /**
     *
     * @param custName
     * @param ticketID
     */
    public void setCustomerNameInTableByTicketId(String custName, String ticketID) {
        try {
            new StaticSentence(m_s, "UPDATE PLACES SET CUSTOMER=? WHERE TICKETID=?", new SerializerWriteBasic(new Datas[]{
                Datas.STRING,
                Datas.STRING
            })).exec(custName,ticketID);
        } catch (Exception e) {
            Logger.getLogger(RestaurantDBUtils.class.getName()).log(Level.WARNING, null, e);
        }
    }

    /**
     *
     * @param tableName
     * @return
     */
    public String getCustomerNameInTable(String tableName) {
        try {
            m_result = new PreparedSentence(m_s,
                "SELECT CUSTOMER FROM PLACES WHERE NAME = ?",
                SerializerWriteString.INSTANCE,
                SerializerReadString.INSTANCE).find(tableName);
            return(m_result==null?"":(String)m_result);            
        } catch (Exception e) {
            Logger.getLogger(RestaurantDBUtils.class.getName()).log(Level.WARNING, null, e);
        }
        return "";
    }

    /**
     *
     * @param tableId
     * @return
     */
    public String getCustomerNameInTableById(String tableId) {
        try {
            m_result = new PreparedSentence(m_s,
                "SELECT CUSTOMER FROM PLACES WHERE ID = ?",
                SerializerWriteString.INSTANCE,
                SerializerReadString.INSTANCE).find(tableId);
            return(m_result==null?"":(String)m_result);            
        } catch (Exception e) {
            Logger.getLogger(RestaurantDBUtils.class.getName()).log(Level.WARNING, null, e);
        }
        return "";
    }

    /**
     *
     * @param tableName
     */
    public void clearCustomerNameInTable(String tableName) {
        try {
            new StaticSentence(m_s, "UPDATE PLACES SET CUSTOMER=null WHERE NAME=?", SerializerWriteString.INSTANCE).exec(tableName);
        } catch (Exception e) {
            Logger.getLogger(RestaurantDBUtils.class.getName()).log(Level.WARNING, null, e);
        }
    }

    /**
     *
     * @param tableID
     */
    public void clearCustomerNameInTableById(String tableID) {
        try {
            new StaticSentence(m_s, "UPDATE PLACES SET CUSTOMER=null WHERE ID=?", SerializerWriteString.INSTANCE).exec(tableID);
        } catch (Exception e) {
            Logger.getLogger(RestaurantDBUtils.class.getName()).log(Level.WARNING, null, e);
        }
    }

    /**
     *
     * @param waiterName
     * @param tableName
     */
    public void setWaiterNameInTable(String waiterName, String tableName) {
        try {
            new StaticSentence(m_s, "UPDATE PLACES SET WAITER=? WHERE NAME=?", new SerializerWriteBasic(new Datas[]{
                Datas.STRING,
                Datas.STRING
            })).exec(waiterName,tableName);
        } catch (Exception e) {
            Logger.getLogger(RestaurantDBUtils.class.getName()).log(Level.WARNING, null, e);
        }
    }

    /**
     *
     * @param waiterName
     * @param tableID
     */
    public void setWaiterNameInTableById(String waiterName, String tableID) {
        try {
            new StaticSentence(m_s, "UPDATE PLACES SET WAITER=? WHERE ID=?", new SerializerWriteBasic(new Datas[]{
                Datas.STRING,
                Datas.STRING
            })).exec(waiterName, tableID);
        } catch (Exception e) {
            Logger.getLogger(RestaurantDBUtils.class.getName()).log(Level.WARNING, null, e);
        }
    }

    /**
     *
     * @param tableName
     * @return
     */
    public String getWaiterNameInTable(String tableName) {
        try {
            m_result = new PreparedSentence(m_s,
                "SELECT WAITER FROM PLACES WHERE NAME = ?",
                SerializerWriteString.INSTANCE,
                SerializerReadString.INSTANCE).find(tableName);
            return(m_result==null?"":(String)m_result);
        } catch (Exception e) {
            Logger.getLogger(RestaurantDBUtils.class.getName()).log(Level.WARNING, null, e);
        }
        return "";
    }

    /**
     *
     * @param tableID
     * @return
     */
    public String getWaiterNameInTableById(String tableID) {
        try {
            m_result = new PreparedSentence(m_s,
                "SELECT WAITER FROM PLACES WHERE ID= ?",
                SerializerWriteString.INSTANCE,
                SerializerReadString.INSTANCE).find(tableID);
            return(m_result==null?"":(String)m_result);
        } catch (Exception e) {
            Logger.getLogger(RestaurantDBUtils.class.getName()).log(Level.WARNING, null, e);
        }
        return "";
    }

    /**
     *
     * @param tableName
     */
    public void clearWaiterNameInTable(String tableName) {
        try {
            new StaticSentence(m_s, "UPDATE PLACES SET WAITER=null WHERE NAME=?", SerializerWriteString.INSTANCE).exec(tableName);
        } catch (Exception e) {
            Logger.getLogger(RestaurantDBUtils.class.getName()).log(Level.WARNING, null, e);
        }
    }

    /**
     *
     * @param tableID
     */
    public void clearWaiterNameInTableById(String tableID) {
        try {
            new StaticSentence(m_s, "UPDATE PLACES SET WAITER=null WHERE ID=?", SerializerWriteString.INSTANCE).exec(tableID);
        } catch (Exception e) {
            Logger.getLogger(RestaurantDBUtils.class.getName()).log(Level.WARNING, null, e);
        }
    }

    /**
     *
     * @param tableID
     * @return
     */
    public String getTicketIdInTable(String tableID) {
        try {
            m_result = new PreparedSentence(m_s,
                "SELECT TICKETID FROM PLACES WHERE ID = ?",
                SerializerWriteString.INSTANCE,
                SerializerReadString.INSTANCE).find(tableID);
            return(m_result==null?"":(String)m_result);      
        } catch (Exception e) {
            Logger.getLogger(RestaurantDBUtils.class.getName()).log(Level.WARNING, null, e);
        }
        return "";
    }

    /**
     *
     * @param ticketID
     * @param tableName
     */
    public void setTicketIdInTable(String ticketID, String tableName) {
        try {
            new StaticSentence(m_s, "UPDATE PLACES SET TICKETID=? WHERE NAME=?", new SerializerWriteBasic(new Datas[]{
                Datas.STRING,
                Datas.STRING
            })).exec(ticketID, tableName);
        } catch (Exception e) {
            Logger.getLogger(RestaurantDBUtils.class.getName()).log(Level.WARNING, null, e);
        }
    }

    /**
     *
     * @param tableName
     */
    public void clearTicketIdInTable(String tableName) {
        try {
            new StaticSentence(m_s, "UPDATE PLACES SET TICKETID=null WHERE NAME=?", SerializerWriteString.INSTANCE).exec(tableName);
        } catch (Exception e) {
            Logger.getLogger(RestaurantDBUtils.class.getName()).log(Level.WARNING, null, e);
        }
    }

    /**
     *
     * @param tableID
     */
    public void clearTicketIdInTableById(String tableID) {
        try {
            new StaticSentence(m_s, "UPDATE PLACES SET TICKETID=null WHERE ID=?", SerializerWriteString.INSTANCE).exec(tableID);
        } catch (Exception e) {
            Logger.getLogger(RestaurantDBUtils.class.getName()).log(Level.WARNING, null, e);
        }
    }

    /**
     *
     * @param ticketID
     * @return
     */
    public Integer countTicketIdInTable(String ticketID) {
        try {
            m_result = new PreparedSentence(m_s,
                "SELECT COUNT(*) AS RECORDCOUNT FROM PLACES WHERE TICKETID = ?",
                SerializerWriteString.INSTANCE,
                SerializerReadInteger.INSTANCE).find(ticketID);
            return (Integer) m_result;
        } catch (Exception e) {
            Logger.getLogger(RestaurantDBUtils.class.getName()).log(Level.WARNING, null, e);
        }

        return 0;
    }

    /**
     *
     * @param ticketID
     * @return
     */
    public String getTableDetails(String ticketID) {
        try {
            m_result=new PreparedSentence(m_s,
                "SELECT NAME FROM PLACES WHERE TICKETID = ?",
                SerializerWriteString.INSTANCE,
                SerializerReadString.INSTANCE).find(ticketID);
            return(m_result==null?"":(String)m_result);
        } catch (Exception e) {
            Logger.getLogger(RestaurantDBUtils.class.getName()).log(Level.WARNING, null, e);
        }
        return "";
    }

    /**
     *
     * @param tableID
     */
    public void setTableMovedFlag(String tableID) {
        try {
            new StaticSentence(m_s, "UPDATE PLACES SET TABLEMOVED=" + TRUE + " WHERE ID=?", SerializerWriteString.INSTANCE).exec(tableID);
        } catch (Exception e) {
            Logger.getLogger(RestaurantDBUtils.class.getName()).log(Level.WARNING, null, e);
        }
    }

    /**
     *
     * @param ticketID
     * @return
     */
    public String getTableMovedName(String ticketID) {
        try {
            m_result = new PreparedSentence(m_s,
                "SELECT NAME FROM PLACES WHERE TICKETID = ? AND TABLEMOVED = " + TRUE,
                SerializerWriteString.INSTANCE,
                SerializerReadString.INSTANCE).find(ticketID);
            return (String)m_result;
        } catch (Exception e) {
            Logger.getLogger(RestaurantDBUtils.class.getName()).log(Level.WARNING, null, e);
        }
        return null;
    }

    /**
     *
     * @param ticketID
     * @return
     */
    public Boolean getTableMovedFlag(String ticketID) {
        try {
            m_result= new PreparedSentence(m_s,
                "SELECT TABLEMOVED FROM PLACES WHERE TICKETID = ?",
                SerializerWriteString.INSTANCE,
                SerializerReadInteger.INSTANCE).find(ticketID);
            return ((Integer)m_result==1)?true:false;
            
        } catch (Exception e) {
            Logger.getLogger(RestaurantDBUtils.class.getName()).log(Level.WARNING, null, e);
        }
        return (false);
    }

    /**
     *
     * @param tableName
     */
    public void clearTableMovedFlag(String tableName) {
        try {
            new StaticSentence(m_s, "UPDATE PLACES SET TABLEMOVED=" + FALSE + " WHERE NAME=?", SerializerWriteString.INSTANCE).exec(tableName);
        } catch (Exception e) {
            Logger.getLogger(RestaurantDBUtils.class.getName()).log(Level.WARNING, null, e);
        }
    }

}
