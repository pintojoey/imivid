package com.imago.services;

import java.io.File;
import java.util.Iterator;
import java.util.List;
//
//
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
//import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
//

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.JSONArray;
import org.json.JSONObject;



@Path("/FileUploadService")


public class FileUploadService extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final String UPLOAD_DIRECTORY = "/uploads";
	private static final int THRESHOLD_SIZE = 1024 * 1024 * 3; // 3MB
	private static final int MAX_FILE_SIZE = 1024 * 1024 * 10; // 10MB
	private static final int REQUEST_SIZE = 1024 * 1024 * 50; // 50MB


	@Path("/UploadMultipleImages")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
    @POST
	public String uploadMultipleFiles(@Context HttpServletRequest request ) {
		JSONObject json=new JSONObject();
		JSONArray j_array=new JSONArray();
		if (!ServletFileUpload.isMultipartContent(request)) {
			return "Does not support!";
			
		}
		
		// configures some settings
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(THRESHOLD_SIZE);
		//factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
		
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setFileSizeMax(MAX_FILE_SIZE);
		upload.setSizeMax(REQUEST_SIZE);
		
		// constructs the directory path to store upload file
		String uploadPath = request.getRealPath(UPLOAD_DIRECTORY);
		// creates the directory if it does not exist
		File uploadDir = new File(uploadPath);
		if (!uploadDir.exists()) {
			uploadDir.mkdir();
		}
		
		try {
			// parses the request's content to extract file data
			List<FileItem> formItems = upload.parseRequest( request);
			Iterator<FileItem> iter = formItems.iterator();
			// iterates over form's fields
			while (iter.hasNext()) {
				FileItem item = (FileItem) iter.next();
				// processes only fields that are not form fields
				if (!item.isFormField()) {
					String fileName = new File(item.getName()).getName();
					String filePath=request.getRealPath("/")+"uploads/";
					File storeFile = new File(filePath+fileName);
					System.out.println(filePath+fileName);
					
					item.write(storeFile);
//					Image img=com.imago.graphics.Utilities.readImage(filePath+fileName);
//					System.out.println(img.getHeight()+" "+img.getWidth());
//					j_array.put(img.getHeight()+" "+img.getWidth());
				}
			}
		} catch (Exception ex) {
			request.setAttribute("message", "There was an error: " + ex.getMessage());
			ex.printStackTrace();
		}
		
		
		
		return json.toString();
		
		}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public static String test(){
		
		return "hello world";
	}

}
