package com.example.demo;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

@RestController
public class SQResponseProcessor {
	private static final String CLASS_NAME = "SQResponseProcessor";

	@RequestMapping(value = "/responseProcessor", method = RequestMethod.POST)
	public String responseProcessor(@RequestBody String responsexml) {
		final String METHOD_NAME = "responseProcessor(@RequestBody String responsexml)";
		// logging
		
		
		

/*    String name = "test11";
    if (!file.isEmpty()) {
        try {
            byte[] bytes = file.getBytes();
            BufferedOutputStream stream = 
                    new BufferedOutputStream(new FileOutputStream(new File(name + "-uploaded")));
            stream.write(bytes);
            stream.close();
            System.out.println( "You successfully uploaded " + name + " into " + name + "-uploaded !");
        } catch (Exception e) {
        	System.out.println( "You failed to upload " + name + " => " + e.getMessage());
        }
    } else {
    	System.out.println( "You failed to upload " + name + " because the file was empty.");
    }*/

    
    


		try {
			
		
		/*	String ManageIncidentNotification =StringUtils.substringBetween(responsexml,"<dns:ManageIncidentNotification>",
					"</dns:ManageIncidentNotification>");*/
			//(<dns:ManageIncidentNotification.*</dns:ManageIncidentNotification>).*
			
			String pattern = ".*(<dns:ManageIncidentNotification.*:ManageIncidentNotification>).*";

		      // Create a Pattern object
		      Pattern r = Pattern.compile(pattern,Pattern.DOTALL);

		      // Now create matcher object.
		      String manageIncidentNotification = "";
		      Matcher m = r.matcher(responsexml);
		      if (m.find()) {
		    	  manageIncidentNotification =  m.group(1);
		    	 // System.out.println("********** "+manageIncidentNotification);
		      }
		        
		      
		      responsexml =  responsexml.replaceAll("(?s)<dns:ManageIncidentNotification.*</dns:ManageIncidentNotification>","");
		      
		      responsexml =  responsexml.replaceAll("(?s)<eb:Manifest.*</eb:Manifest>", manageIncidentNotification);
		      responsexml = responsexml.replace("<root>", "").replace("</root>", "").trim();
		      responsexml = responsexml.replace("\r\n\r\n","\r\n");
			
			Document doc = convertStringToDocument(responsexml);
			/*String manageIncidentNotification = doc.getElementsByTagName("dns:ManageIncidentNotification").
					item(0).getTextContent();
			
			NodeList manifestNodes = doc.getElementsByTagName("eb:Manifest");
			if (null != manifestNodes && manifestNodes.getLength() != 0) {
				Node manifestNode = manifestNodes.item(0);
				Element element = (Element) manifestNode;
				
			   // doc.renameNode(manifestNode, null, "dns:ManageIncidentNotification");
				doc.renameNode(element, element.getNamespaceURI(), "dns:ManageIncidentNotification");
			    
			    
			    element.setTextContent(manageIncidentNotification);*/
			//}
			System.out.println("responsxml =============. " + responsexml);
			
			NodeList list = doc.getElementsByTagName("SOAP-ENV:Fault");
			if (list.getLength() == 0) { // success
				System.out.println("success flow");
				NodeList list1 = doc.getElementsByTagName("eb:ConversationId");
				if (list1 != null && list1.getLength() > 0) {
			        NodeList subList = list1.item(0).getChildNodes();

			        if (subList != null && subList.getLength() > 0) {
			        	String conversationId  = subList.item(0).getNodeValue();
			        	System.out.println("conversationId ====================> "+conversationId);
			        	String[] array = conversationId.split("-");
			        	if(array.length>0) {
			        	String consumerId = array[1];
			        	System.out.println("consumerId ====================> "+consumerId);
			        	}
			        }
			    }
			
			
				
			} else {
				System.out.println("failure flow");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// logging
		return responsexml;
	}

	

	
	private static Document convertStringToDocument(String xmlStr) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new InputSource(new StringReader(xmlStr)));
			return doc;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	
	
	@RequestMapping(value="/upload", method=RequestMethod.POST)
    public @ResponseBody String handleFileUpload( 
            @RequestParam("file") MultipartFile file){
            String name = "test11";
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream = 
                        new BufferedOutputStream(new FileOutputStream(new File(name + "-uploaded")));
                stream.write(bytes);
                stream.close();
                return "You successfully uploaded " + name + " into " + name + "-uploaded !";
            } catch (Exception e) {
                return "You failed to upload " + name + " => " + e.getMessage();
            }
        } else {
            return "You failed to upload " + name + " because the file was empty.";
        }
    }

}
