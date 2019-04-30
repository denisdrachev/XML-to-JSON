package pack.http;

//import jdk.internal.org.xml.sax.InputSource;
//import org.eclipse.jetty.server.handler.DefaultHandler;
//import org.w3c.dom.Document;
//import org.w3c.dom.Document;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.log4j.BasicConfigurator;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;
import pack.jaxb.Envelope;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
        import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import org.apache.log4j.Logger;

//@WebServlet(name = "XmlParserServlet", urlPatterns = "/123")
//@WebServlet("/s2")
public class XmlParserServlet extends HttpServlet {

//    Logger logger = Logger.getLogger(Envelope.class);
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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();


//        RequestDispatcher view = req.getRequestDispatcher("templates/index.html");
//        view.forward(req, resp);

        out.write("<!doctype html>\n" +
                "<html lang=\"en\">\n" +
                "  <head>\n" +
                "    <!-- Required meta tags -->\n" +
                "    <meta charset=\"utf-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">\n" +
                "\n" +
                "    <!-- Bootstrap CSS -->\n" +
                "    <link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css\" integrity=\"sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T\" crossorigin=\"anonymous\">\n" +
                "\n" +
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
//        out.write("<div style=\"display: none;\" class=\"alert alert-success xml-success\" role=\"alert\">\n" +
//                "</div>");
        out.write("<div style=\"display: none;\" class=\"alert alert-success json-success\" role=\"alert\">\n" +
                "</div>");
        out.write("<div style=\"display: none;\" class=\"alert alert-success send-success\" role=\"alert\">\n" +
                "</div>");
        out.write("<div style=\"display: none;\" class=\"alert alert-danger xml-error\" role=\"alert\">\n" +
                "</div>");
        out.write("<div style=\"display: none;\" class=\"alert alert-danger json-error\" role=\"alert\">\n" +
                "</div>");
        out.write("<div style=\"display: none;\" class=\"alert alert-danger send-error\" role=\"alert\">\n" +
                "</div>");
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();

        BasicConfigurator.configure();
        Envelope person = null;

        try {

            JAXBContext jaxbContext = JAXBContext.newInstance(Envelope.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            StringReader reader = new StringReader(req.getParameter("param1"));
            person = (Envelope) unmarshaller.unmarshal(reader);
//            out.write("XML parsing completed successfully</br>");
            out.write("{\"xml\": { \"error\": 0, \"msg\":\"XML parsing success!\"}");

//            System.out.println("PERSON: " + person.toString());
//            out.write("PERSON: " + person.toString() + "\n\n");
//            System.out.println(person.toString());


//            XmlMapper xmlMapper = new XmlMapper();
//            String xml = xmlMapper.writeValueAsString(person);
//            out.write("\n\n" + xml);
//            System.out.println(xml);


            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
            String json = mapper.writeValueAsString(person);
            out.write(",\"json\": { \"error\": 0, \"msg\":\"JSON parsing success!\"}");
            logger.info(json);

            out.write(sendToPort(json));

        } catch (JAXBException je) {

            logger.error("Unmarshalling error", je);
            out.write("{\"xml\": { \"error\": 1, \"msg\": \"XML parsing error!\"}");

        } catch (JsonMappingException jme)
        {
            logger.error("Error convert to json", jme);
            out.write("{\"json\": { \"error\": 1, \"msg\":\"JSON convert error!\"}");

        } finally {
            person = null;
            out.write("}");

        }


    }

    public boolean validate(final Schema schema, final String xml){
        try {
//            doValidate(xml);
            XMLReader parser = XMLReaderFactory.createXMLReader();
            parser.setContentHandler(new DefaultHandler());
            InputSource source = new InputSource(new ByteArrayInputStream(xml.getBytes()));
            parser.parse(source);
            return true;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (org.xml.sax.SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String sendToPort(String data) throws IOException {

        Socket socket = null;
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


            socket = new Socket(this.getDestAddr(), this.getDestPort());
            OutputStream output = socket.getOutputStream();
            output.write(buf.array());
            sb.append("0, \"msg\" : \"Send package success!\"}");
        } catch (Exception e) {
            sb.append("1, \"msg\" : \"Send package error!\"}");
            logger.info("Error sending bytes!");
        } finally {
            return sb.toString();
        }
    }


}







