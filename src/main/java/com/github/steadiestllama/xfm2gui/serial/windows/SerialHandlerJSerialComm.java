package com.github.steadiestllama.xfm2gui.serial.windows;

/*

This file is part of XFM2GUI

Copyright 2020 Lewis Sweeney

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
documentation files (the "Software"), to deal in the Software without restriction, including without limitation
the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE
USE OR OTHER DEALINGS IN THE SOFTWARE.

 */

/*
Some of the code in this class is inspired by code written by Richard Shipman
 */

import com.fazecast.jSerialComm.SerialPort;
import java.util.ArrayList;

public class SerialHandlerJSerialComm {
    SerialPort serialPort;
    private static final int BAUD_RATE = 500000;


    public SerialHandlerJSerialComm(SerialPort serialPort) {
        this.serialPort = serialPort;
    }

    /**
     * Generic method that sends the given bytes to the board
     * Used for all command methods
     *
     * @param bytes byte[] that contains the bytes to be sent to the board
     * @return returns any data that the board replies with
     */
    public byte[] sendCommand(byte[] bytes, int expectedDataBits) {
        if (serialPort != null) {

            if (!serialPort.isOpen()) {
                serialPort.openPort();
            }

            serialPort.writeBytes(bytes, bytes.length);
            byte[] data = null;
            if (expectedDataBits > 0) {
                data = getData(expectedDataBits);
            }
            serialPort.closePort();
            return data;
        } else {
            return new byte[1];
        }

    }

    /**
     * Method used to get data from the device once a command has been sent to the board
     *
     * @return byte[] which is translated by the program for display in the GUI
     */
    byte[] getData(int expectedDataBits) {
        ArrayList<Byte> byteArrayList = new ArrayList<>();

        if (serialPort == null) {
            return null;
        }

        int iter = 0;
        int prevBytesAvailable = 0;
        while (serialPort.bytesAvailable() < expectedDataBits && iter < 25) {
            // Empty to wait for data to be available

            if (prevBytesAvailable == serialPort.bytesAvailable()) {
                iter++;
            }
            prevBytesAvailable = serialPort.bytesAvailable();
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        while (serialPort.bytesAvailable() > 0) {
            byte newByte = readSingleByte();
            byteArrayList.add(newByte);
        }

        byte[] data = new byte[byteArrayList.size()];

        for (int i = 0; i < byteArrayList.size(); i++) {
            data[i] = byteArrayList.get(i);
        }

        return data;
    }

    private byte readSingleByte() {
        byte[] buffer = new byte[1];
        serialPort.readBytes(buffer, 1);
        return buffer[0];
    }

    // GETTERS AND SETTERS
    public SerialPort getSerialPort() {
        return serialPort;
    }

    public void setSerialPort(SerialPort serialPort) {
        this.serialPort = serialPort;
        if (this.serialPort != null) {
            this.serialPort.setComPortParameters(BAUD_RATE, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
        }
    }

}
