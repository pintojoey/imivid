package com.imago.services;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.JSONArray;
import org.json.JSONException;
import org.opencv.core.Core;

import com.imago.opencv.PictureAnalyzer;
import com.imago.opencv.PictureClusterer;


/**
 * Servlet implementation class SearchImage
 */
@WebServlet("/searchImage")
public class SearchImage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String UPLOAD_DIRECTORY = "/uploads";
	private static final int THRESHOLD_SIZE = 1024 * 1024 * 3; // 3MB
	private static final int MAX_FILE_SIZE = 1024 * 1024 * 10; // 10MB
	private static final int REQUEST_SIZE = 1024 * 1024 * 50; // 50MB
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchImage() {
        super();
        // TODO Auto-generated constructor stub
    }

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		JSONArray results=new JSONArray();
		String input_path="";
		String filePath=request.getRealPath("/")+"uploads/";
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
			 ServletFileUpload fileUpload = new ServletFileUpload();
			FileItemIterator items = fileUpload.getItemIterator(request);
			
			// iterates over form's fields
			while (items.hasNext()) {
				FileItemStream item =  items.next();
				// processes only fields that are not form fields
				if (!item.isFormField()) {
					InputStream stream = item.openStream();
					String fileName = new File(item.getName()).getName();
				
					File storeFile = new File(filePath+fileName);
					System.out.println(filePath+fileName);
					input_path="Fruits/"+fileName;
					
					 BufferedOutputStream output = null;

					    try {
					        output = new BufferedOutputStream(new FileOutputStream(storeFile, false));
					        int data = -1;
					        while ((data = stream.read()) != -1) {
					            output.write(data);
					        }
					    
					      results=PictureClusterer.searchImage(filePath+fileName);
					     
					    } finally {
					        stream.close();
					        output.close();
					    }

				}
			}
		} catch (Exception ex) {
			request.setAttribute("message", "There was an error: " + ex.getMessage());
			ex.printStackTrace();
		}
	
		 request.setAttribute("results", results);
		 request.setAttribute("input", input_path);
		    RequestDispatcher rd = getServletContext()
		                               .getRequestDispatcher("/results.jsp");
		    rd.forward(request, response);
		
	}

}
