<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
rest/FileUploadService/UploadMultipleImage
<form action="searchImage" method="POST"
		enctype="multipart/form-data">
		<input type="file" name="files" value="Browse" multiple/><br /> 
	
			<input type="submit" name="Upload" value="Upload" /><br />
	</form>
</body>
</html>