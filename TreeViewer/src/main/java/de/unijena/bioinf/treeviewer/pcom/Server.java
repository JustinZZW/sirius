/*
 *  This file is part of the SIRIUS library for analyzing MS and MS/MS data
 *
 *  Copyright (C) 2013-2015 Kai Dührkop
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with SIRIUS.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.unijena.bioinf.treeviewer.pcom;

import de.unijena.bioinf.treeviewer.FileListPane;
import de.unijena.bioinf.treeviewer.MainFrame;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Server implements Runnable {

    private volatile boolean closed;
    private final ServerSocket socket;
    private final FileListPane pane;

    final public static int PORT = 3721;

    public Server(FileListPane pane) throws IOException {
        closed = false;
        socket = new ServerSocket(PORT);
        socket.setSoTimeout(1000);
        this.pane = pane;
        new Thread(this).start();
    }

    public void close() {
        closed = true;
    }


    @Override
    public void run() {
        while (!closed) {
            try {
                final Socket client = socket.accept();
                final IOConnection connection = new IOConnection(pane);
                connection.readInputInParallel(client.getInputStream(), client.getOutputStream());
            } catch (SocketTimeoutException timeout) {
                // listen again, as long as not closed
            } catch (IOException e) {
                MainFrame.logger.severe("Error while listening for incomming requests:" + e.getMessage());
            }
        }
        try {
            socket.close();
        } catch (IOException e) {
            MainFrame.logger.severe("Error while closing server:" + e.getMessage());
        }
    }

    public static boolean isProcessStillRunning() {
        final boolean[] isanyonethere = new boolean[]{false};
        final Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final Socket client = new Socket("localhost", PORT);
                    final Protocol protocol = new Protocol(client.getInputStream(), client.getOutputStream());
                    protocol.sendPingRequest();
                    final Protocol.Response response = protocol.getResponse();
                    if (response.cmd == Protocol.CMD.PONG) {
                        isanyonethere[0] = true;
                    } else System.out.println("Get nothing: " + response.cmd);
                    client.close();
                } catch (IOException e) {
                    System.out.println("Fehler: " + e.getMessage());
                    isanyonethere[0] = false;
                }

            }
        });
        t.start();
        try {
            t.join(100);
        } catch (InterruptedException e) {
            System.out.println("Zeit vorbei");
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return isanyonethere[0];
    }
}
