//    Chromis POS  - The New Face of Open Source POS
//    Copyright (c) (c) 2015-2016
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

package uk.chromis.pos.printer.escpos;

import uk.chromis.pos.printer.DeviceTicket;

/**
 * 
 * Implements the changes necessary to make a Wincor Nixdorf BA63 serial 
 * display work with the pos system.
 * 
 * Reference to the control sequences can be found in the operating
 * manual for the display at the following URL:
 * 
 * http://www.wincor-nixdorf.com/internet/cae/servlet/contentblob/766324/publicationFile/8243/BA63_USB_Display_Operating_Manual_english.pdf
 * 
 * @author jacobmellin
 */
public class DeviceDisplayBA63 extends DeviceDisplaySerial {
    
    private UnicodeTranslator trans;
    
    /**
     * 'Delete display' sequence
     */
    private static final byte[] BA63_VISOR_CLEAR = 
        {0x1B, 0x5B, 0x32, 0x4A}; // ESC '[', '2', 'J'

    /**
     * 'Position cursor' sequence with params 1,1
     */
    private static final byte[] BA63_VISOR_LINE1HOME =
        {0x1B, 0x5B, 0x31, 0x3B, 0x31, 0x48}; // ESC, '[', '1', ';' '1', 'H'

    /**
     * 'Position cursor' sequence with params 2,1
     */
    private static final byte[] BA63_VISOR_LINE2HOME =
        {0x1B, 0x5B, 0x32, 0x3B, 0x31, 0x48}; // ESC, '[', '2', ';' '1', 'H'

    /**
     * Beginning of 'Set country code' sequence (ESC, 'R').
     * 
     * The specific country code / code page must be still be written after the 
     * two bytes. Refer to the manual for a list of codes.
     */
    private static final byte[] BA63_BEGIN_SET_COUNTRY =
        {0x1B, 0x52}; // ESC, 'R'
    
    /**
     * 
     * @param display PrinterWritter
     * @param trans UnicodeTranslator
     */
    public DeviceDisplayBA63(PrinterWritter display, UnicodeTranslator trans) {
        this.trans = trans;
        init(display);
    }
    
    /**
     *
     */
    @Override
    public void initVisor() {
        
        display.write(BA63_BEGIN_SET_COUNTRY); // Begin 'Set country code'
        display.write(trans.getCodeTable()); // Sets the code / code page
        
        display.write(BA63_VISOR_CLEAR);
        display.write(BA63_VISOR_LINE1HOME);
        
        display.flush();
    }
        
    /**
     * 
     */
    @Override
    public void repaintLines() {
   
        display.write(BA63_VISOR_CLEAR);
        
        display.write(BA63_VISOR_LINE1HOME);
        display.write(trans.transString(DeviceTicket.alignLeft(m_displaylines.getLine1(), 20)));
        
        display.write(BA63_VISOR_LINE2HOME);
        display.write(trans.transString(DeviceTicket.alignLeft(m_displaylines.getLine2(), 20)));        
        
        display.flush();
    }
    
}
