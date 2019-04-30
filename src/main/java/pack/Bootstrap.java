    package pack;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import pack.http.XmlParserServlet;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.eclipse.jetty.server.*;

import javax.xml.bind.JAXBException;


public class Bootstrap {

    private String httpPort;

    private String destAddr;

    private String destPort;

    public Bootstrap() {
        this.httpPort = "8080";
        this.destAddr = "localhost";
        this.destPort = "1234";
        readProperty();
    }

    public String getHttpPort() {
        return httpPort;
    }

    public void setHttpPort(String httpPort) {
        if (httpPort != null && httpPort.trim().length() > 0)
            this.httpPort = httpPort;
    }

    public String getDestAddr() {
        return destAddr;
    }

    public void setDestAddr(String destAddr) {
        if (destAddr != null && destAddr.trim().length() > 0)
            this.destAddr = destAddr;
    }

    public String getDestPort() {
        return destPort;
    }

    public void setDestPort(String destPort) {
        if (destPort != null && destPort.trim().length() > 0)
            this.destPort = destPort;
    }

    public void readProperty() {

        try {
            List<String> lines = Files.readAllLines(Paths.get("config.property"), StandardCharsets.UTF_8);

            for (String line : lines) {
                String[] split = line.split("=");
                if (split.length == 2)
                {
                    if (split[0].equals("http.port"))
                        setHttpPort(split[1]);
                    if (split[0].equals("tcp.dest.addr"))
                        setDestAddr(split[1]);
                    if (split[0].equals("tcp.dest.port"))
                        setDestPort(split[1]);
                }
            }
        }
        catch (IOException ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    public static void main(String [] args)  throws JAXBException {

        Bootstrap el = new Bootstrap();

        Server server = new Server(Integer.valueOf(el.getHttpPort()));
//
//        ServletHandler handler = new ServletHandler();
//
//        handler.addServletWithMapping(XmlParserServlet.class, "/");
//        server.setHandler(handler);


        ServletContextHandler context = new ServletContextHandler();
//        context.setContextPath("/");

        XmlParserServlet xmlParserServlet = new XmlParserServlet(el.getDestAddr(), el.getDestPort());
        ServletHolder servletHolder = new ServletHolder(xmlParserServlet);
        context.addServlet(servletHolder, "");

        // Option 2: Using ServletContext attribute
//        context.setAttribute("my.greeting", "you");
//        context.addServlet(GreetingServlet.class, "/greetings/*");

        server.setHandler(context);


        try {
            server.start();
            server.join();
        } catch (Throwable t) {
            t.printStackTrace(System.err);
        }
    }
}
