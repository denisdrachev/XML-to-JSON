package pack.http;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;
import pack.jaxb.Envelope;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;

import org.apache.log4j.Logger;

public class XmlParserServlet extends HttpServlet {

    static Logger logger = Logger.getLogger(Envelope.class);

    private String destAddr;

    private Integer destPort;


    public XmlParserServlet() {
    }

    public XmlParserServlet(String destAddr, String destPort) {
        this.destAddr = destAddr;
        this.destPort = Integer.parseInt(destPort);
    }

    public String getDestAddr() {
        return destAddr;
    }

    public void setDestAddr(String destAddr) {
        this.destAddr = destAddr;
    }

    public Integer getDestPort() {
        return destPort;
    }

    public void setDestPort(Integer destPort) {
        this.destPort = destPort;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        out.write("<!doctype html>\n" +
                "<html lang=\"en\">\n" +
                "  <head>\n" +
                "    <meta charset=\"utf-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">\n" +
                "    <link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css\" integrity=\"sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T\" crossorigin=\"anonymous\">\n" +
                "    <title>XML to JSON</title>\n" +
                "  </head> <body> <div class=\"container\" style=\"padding-top: 50px;\">");
        out.write("<script src='https://code.jquery.com/jquery-latest.min.js'></script>");
        out.write("<h1>XML to JSON</h1>\n");
        out.write("<form id='target' action='XmlParserServlet' method='post'>");
        out.write("<div class='form-group'>");
        out.write("<textarea class='form-control' id='e1' rows='12' name='xml'></textarea>");
        out.write("</div>");
        out.write("<button id=\"submit\" type='submit' class='btn btn-primary'>Submit </button>");
        out.write("<button style=\"display: none;\" id=\"loading\" class=\"btn btn-primary\" type=\"button\" disabled>\n" +
                "  <span class=\"spinner-border spinner-border-sm\" role=\"status\" aria-hidden=\"true\"></span>\n" +
                "  Loading...\n" +
                "</button>");
        out.write(" </form></div>");
        out.write("<div class=\"container\" style=\"margin-top: 16px;\">");
        out.write("<div style=\"display: none;\" class=\"alert alert-success json-success\" role=\"alert\"></div>");
        out.write("<div style=\"display: none;\" class=\"alert alert-success send-success\" role=\"alert\"></div>");
        out.write("<div style=\"display: none;\" class=\"alert alert-danger xml-error\" role=\"alert\"></div>");
        out.write("<div style=\"display: none;\" class=\"alert alert-danger json-error\" role=\"alert\"></div>");
        out.write("<div style=\"display: none;\" class=\"alert alert-danger send-error\" role=\"alert\"></div>");
        out.write("</div>");
        out.write("</body><script>" +
                "$( \"#target\" ).submit(function( event ) {\n" +
                " $(\"#submit\").hide(); $(\"#loading\").show();" +
                "   $(\".xml-error\").hide(); $(\".json-error\").hide(); $(\".send-error\").hide();" +
                "   $(\".xml-success\").hide(); $(\".json-success\").hide(); $(\".send-success\").hide();" +
                "  $.post(\"/\",\n" +
                "  {\n" +
                "    param1: $(this).find(\"textarea\").val(),\n" +
                "  },\n" +
                "  onAjaxSuccess).fail(onAjaxError);\n" +
                "  event.preventDefault();\n" +
                "});" +
                "function onAjaxSuccess(data)\n" +
                "{\n" +
                " var json = $.parseJSON(data); \n" +
                "   if (json.hasOwnProperty(\"json\")) {    if (json.json.error == 0)\n" +
                "{\n" +
                "        $(\".json-success\").show();\n" +
                "   $(\".json-success\").text(json.json.msg);\n" +
                "} else {\n" +
                "        $(\".json-error\").show();\n" +
                "   $(\".json-error\").text(json.json.msg);\n" +
                "}}" +
                "   if (json.hasOwnProperty(\"xml\")) { " +
                "   if (json.xml.error != 0)\n" +
                "   {\n" +
                "        $(\".xml-error\").show();\n" +
                "   $(\".xml-error\").text(json.xml.msg);\n" +
                "}}\n " +
                "   if (json.hasOwnProperty(\"send\")) {    if (json.send.error == 0)\n" +
                "{\n" +
                "        $(\".send-success\").show();\n" +
                "   $(\".send-success\").text(json.send.msg);\n" +
                "} else {\n" +
                "        $(\".send-error\").show();\n" +
                "   $(\".send-error\").text(json.send.msg);\n" +
                "}}" +
                " $(\"#submit\").show(); $(\"#loading\").hide();" +
                "};" +
                "function onAjaxError(xhr, status, error)\n" +
                "{\n" +
                "   alert(status);\n" +
                "};\n" +
                "</script>" +
                "\n" +
                "</html>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        PropertyConfigurator.configure("src/log4j.properties");


        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Envelope.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            StringReader reader = new StringReader(req.getParameter("param1"));
            Envelope person = (Envelope) unmarshaller.unmarshal(reader);
            out.write("{\"xml\": { \"error\": 0, \"msg\":\"XML parsing success!\"}");

            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
            String json = mapper.writeValueAsString(person);
            out.write(",\"json\": { \"error\": 0, \"msg\":\"JSON parsing success!\"}");
            logger.info(json);

            out.write(sendToAddrPort(json));

        } catch (JAXBException je) {

            logger.error("XML parsing error!");
            out.write("{\"xml\": { \"error\": 1, \"msg\": \"XML parsing error!\"}");

        } catch (JsonMappingException jme)
        {
            logger.error("JSON convert error!");
            out.write("{\"json\": { \"error\": 1, \"msg\":\"JSON convert error!\"}");

        } finally {
            out.write("}");
        }
    }

    public String sendToAddrPort(String data) {

        StringBuffer sb = new StringBuffer(",\"send\":{ \"error\":");

        try {
            ByteBuffer buf = ByteBuffer.allocate(12 + data.length() * 2);
            buf.put("FFBBCCDD".getBytes());

            buf.put((byte) (data.length()));
            buf.put((byte) (data.length() >> 8));
            buf.put((byte) (data.length() >> 16));
            buf.put((byte) (data.length() >> 24));

            buf.put(data.getBytes("UTF-16LE"));

            logger.info(DatatypeConverter.printHexBinary(buf.array()));


            Socket socket = new Socket(this.getDestAddr(), this.getDestPort());
            OutputStream output = socket.getOutputStream();
            output.write(buf.array());
            sb.append("0, \"msg\" : \"Send package success!\"}");
        } catch (Exception e) {
            sb.append("1, \"msg\" : \"Send package error!\"}");
            logger.error("Send package error!");
        } finally {
            return sb.toString();
        }
    }
}







