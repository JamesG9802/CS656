/*
 * Copyright (c) 2013, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 

import org.bitlet.weupnp.*;
import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class VerboseEchoServer {
    public static void main(String[] args) throws IOException {
        
        if (args.length != 1) {
            System.err.println("Usage: Verbosejava EchoServer <port number>");
            System.exit(1);
        }
        
        int portNumber = Integer.parseInt(args[0]);

        try {
            GatewayDiscover discover = new GatewayDiscover();
            System.out.println("Looking for Gateway Devices...");
            discover.discover();
            GatewayDevice d = discover.getValidGateway();

            if (null != d) {
                System.out.println("Found gateway device: " + d.getModelName());
                InetAddress localAddress = d.getLocalAddress();
                System.out.println("Using local address: " + localAddress);

                // This actually opens the port on your router
                if (d.addPortMapping(portNumber, portNumber, localAddress.getHostAddress(), "TCP", "EchoServer")) {
                    System.out.println("UPnP Port Mapping successful on port " + portNumber);
                    System.out.println("External IP: " + d.getExternalIPAddress());
                } else {
                    System.out.println("Port mapping failed.");
                }
            } else {
                System.out.println("No valid UPnP gateway device found.");
            }
        } catch (Exception e) {
            System.err.println("UPnP error: " + e.getMessage());
        }

        System.out.println(String.format("%s: %sAwaiting client on port <%d>%s", Time.now(), Ansi.BLUE, portNumber, Ansi.RESET));
        try (
            ServerSocket serverSocket =
                new ServerSocket(portNumber);
            Socket clientSocket = serverSocket.accept();     
            PrintWriter out =
                new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8), true);                   
            BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
        ) {
            System.out.println(String.format("%s: %sClient connected.%s", Time.now(), Ansi.GREEN, Ansi.RESET));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
            	System.out.println(String.format("%s: The client sent: %s%s%s", Time.now(), Ansi.GREEN, inputLine, Ansi.RESET));
            	
            	//	Modifying message;
            	StringBuilder message = new StringBuilder();

                for (int offset = 0, index = 0; offset < inputLine.length(); ) {
                    int codePoint = inputLine.codePointAt(offset);

                    int modified =
                        // (index % 2 == 0)
                        //     ? Character.toUpperCase(codePoint)
                        //     : Character.toLowerCase(codePoint);
                        Character.toUpperCase(codePoint);

                    message.appendCodePoint(modified);

                    offset += Character.charCount(codePoint);
                    index++;
                }

            	System.out.println(String.format("%s: Server will respond: %s%s%s\n", Time.now(), Ansi.PURPLE, message.toString(), Ansi.RESET));

                out.println(message.toString());
            }

            System.out.println(String.format("Client disconnected."));
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }
}