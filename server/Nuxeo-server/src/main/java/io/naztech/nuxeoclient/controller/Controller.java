package io.naztech.nuxeoclient.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.nazdaqTechnologies.core.message.Message;
import com.nazdaqTechnologies.core.message.MessageContentType;
import com.nazdaqTechnologies.core.message.MessageHeader;
import com.nazdaqTechnologies.core.message.model.Status;
import com.nazdaqTechnologies.core.message.model.StatusType;
import com.nazdaqTechnologies.core.message.processor.json.gson.GsonJsonMessageProcessor;

import io.naztech.nuxeoclient.constants.Constants;
import io.naztech.nuxeoclient.service.AttachmentDownloadFromMailService;
import io.naztech.nuxeoclient.service.ServiceCoordinator;
import io.naztech.nuxeoclient.utility.EmailSearchTerm;

/**
 * Controller for all HTTP/HTTPS actions and process through service classes
 * 
 * @author Md. Mahbub Hasan Mohiuddin
 * @since 2019-10-17
 */

@RestController
@RequestMapping(Constants.DOCUTECH)
public class Controller {
	private static Logger log = LoggerFactory.getLogger(Controller.class);
	private static final String DOCUMENT = "/Document";
	private static final String DOWNLOAD = "/download";
	
	@Autowired
	GsonJsonMessageProcessor messageProcessor;

	@Autowired
	ServiceCoordinator serviceCoordinator;
	
	@Autowired
	private EmailSearchTerm est;
	
	@Autowired
	private AttachmentDownloadFromMailService atcMail;
	
	@Value("${out.folder.path}")
	private String outFolderPath;
	
	@Value("${no.file.path}")
	private String noFilePath;
	
	@PostMapping(Constants.JSON_REQUEST)
	public String handleJsonRequest(@RequestBody String json) {

		Message<?> request = null;

		MessageHeader requestHeaders = null;

		String responseString = null;

		Message<?> dataMsg = null;
		Message<?> response = null;

		String errorString = null;

		String serviceName = null;

		Map<String, Object> statusMsgHeader = new HashMap<String, Object>();

		statusMsgHeader.put(MessageHeader.CONTENT_TYPE, MessageContentType.STATUS);

		try {
			request = messageProcessor.processMessage(json);

			log.debug("Recieved Request {}", json);

			if (request != null && request.getHeader().getContentType() != MessageContentType.EXCEPTION.toString()) {
				requestHeaders = request.getHeader();

				serviceName = requestHeaders.getDestination();

				if (serviceName != null) {

					log.debug("Source [{}] Destination [{}]", requestHeaders.getSource(), serviceName);

					/**
					 * @author mahbub.hasan
					 *         validating request
					 */
					validateRequest(requestHeaders, request);

					/**
					 * @author mahbub.hasan
					 *         sending message to service coordinator
					 */
					dataMsg = serviceCoordinator.service(request);

					if (dataMsg == null) {
						response = handleErrResponse(request);
					}
					else {

						log.debug("Response string:{}", new Gson().toJson(dataMsg.getPayload()));

						response = handleSuccessResponse(request, dataMsg);
					}
				}
			}
		}
		catch (Exception ex) {
			log.error("error with request {}", ex);
			errorString = ex.getLocalizedMessage();

			List<Status> statusList = new ArrayList<Status>();
			statusList.add(new Status(StatusType.ERROR, errorString));
			response = messageProcessor.createResponseMessage(request, statusList, statusMsgHeader);
		}

		responseString = messageProcessor.toJson(response);

		return responseString;
	}

	/**
	 * @author mahbub.hasan
	 *         validates the http json request
	 */
	private void validateRequest(MessageHeader requestHeaders, Message<?> msg) throws Exception {

		StringBuffer sb = new StringBuffer();

		if (requestHeaders.getContentType() == null) {
			sb.append("Missing ContentType");
		}

		if (requestHeaders.getActionType() == null) {
			sb.append("Missing ActionType");
		}

		if (sb.length() > 0) {
			throw new Exception(sb.toString());
		}
	}

	/**
	 * @author mahbub.hasan
	 *         handles the error response when there no response from service classes
	 */
	private Message<?> handleErrResponse(Message<?> request) {

		String serviceName = null;

		Message<?> response = null;

		Map<String, Object> statusMsgHeader = new HashMap<String, Object>();
		statusMsgHeader.put(MessageHeader.CONTENT_TYPE, MessageContentType.STATUS);

		try {
			String errorMsg = "no response received from service -> " + serviceName;
			log.error(errorMsg);
			List<Status> statusList = new ArrayList<Status>();
			statusList.add(new Status(StatusType.ERROR, errorMsg));

			response = messageProcessor.createResponseMessage(request, statusMsgHeader, statusMsgHeader);
		}
		catch (Exception e) {
			log.error("Caught Error {} / {}", e, e);
		}

		return response;
	}

	/**
	 * @author mahbub.hasan
	 *         handles the success responses from service classes
	 * @param Message
	 *            request and Message data
	 */
	private Message<?> handleSuccessResponse(Message<?> request, Message<?> dataMsg) {

		Message<?> response = null;

		List<Status> statusMsgList = new ArrayList<Status>();
		statusMsgList.add(new Status(StatusType.OK));

		Map<String, Object> statusMsgHeader = new HashMap<String, Object>();
		statusMsgHeader.put(MessageHeader.CONTENT_TYPE, MessageContentType.STATUS);

		Message<List<Status>> statusMsg = messageProcessor.createResponseMessage(request, statusMsgList, statusMsgHeader);

		List<Message<?>> msgBody = new ArrayList<Message<?>>();
		msgBody.add(statusMsg);
		msgBody.add(dataMsg);

		Map<String, Object> finalMsgHeader = new HashMap<String, Object>();
		finalMsgHeader.put(MessageHeader.CONTENT_TYPE, MessageContentType.MULTI);

		response = messageProcessor.createResponseMessage(request, msgBody, finalMsgHeader);

		return response;
	}

	/**
	 * @since 2019-12-22
	 * @author mahbub.hasan
	 * Gets the file from path and uploads to GUI
	 * @param Message servlet request & response
	 */
	@RequestMapping(value = DOCUMENT)
	public void documents(HttpServletRequest request, HttpServletResponse response) throws Exception {

		FileInputStream in = null;
		File fFile = null;
		File file = new File(outFolderPath);
		String fileName = request.getParameter("fileName");
		
		OutputStream out = response.getOutputStream();
		
		//First clear the file result list
		List<String> result = new ArrayList<>();
		result.clear();
		
		est.setResult(result);
		
		// Search files in directory and add to list
		List<String> foundFile = est.search(file, fileName);
		
		if (foundFile.parallelStream().findFirst().isPresent()) {
			
			for(String path: foundFile) {
				
				log.info("File path: {}", path);
				
				fFile = new File(path);
			}
		}
		else {
			fFile = new File(noFilePath);
		}
				
		try {
			in = new FileInputStream(fFile);
		} catch (FileNotFoundException e) {
			log.error("Error loading file from file system: {}", e.getMessage());
		}
		
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "filename=\"" + file.getName() + "\"");
		
		IOUtils.copy(in, out);

		out.flush();
		in.close();
		out.close();

		log.info("Sending file to UI: {}", file.getName());
	}
	
	/**
	 * @since 2020-12-22
	 * @author mahbub.hasan
	 * Gets the file from path and uploads to GUI
	 * @param Message servlet request & response
	 * @throws ParseException 
	 */
	@RequestMapping(value = DOWNLOAD)
	public String attachmentDownload(@RequestBody String json) throws ParseException{
		atcMail.downloadAttachments();
		
		return "OK";
	}
	
	@GetMapping(value = "/rid")
	private String testr() {
		// TODO Auto-generated method stub
		return "I am work";

	}
}
