package com.shopall.admin.user;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shopall.common.entity.User;

import jakarta.servlet.http.HttpServletResponse;

public class UserCsvExporter {
	
	public void export(List<User> listUsers, HttpServletResponse response) throws IOException {
		
		DateFormat dateFormatter = new SimpleDateFormat("YYYY-MM-dd_HH-mm-ss");
		String timeStamp = dateFormatter.format(new Date());
		String fileName = "users_" + timeStamp + ".csv";
		
		response.setContentType("text/csv");
		
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=" + fileName;
		response.setHeader(headerKey, headerValue);
		
		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), 
				CsvPreference.STANDARD_PREFERENCE);
		
		String[]  csvHeader = {"User ID", "E-Mail", "First Name", "Last Name", "Roles", "Enabled"};
		String[] fieldMapping = {"id", "email", "firstName", "lastName", "roles", "enabled"};
		
		csvWriter.writeHeader(csvHeader);
		
		for (User user : listUsers) {
			csvWriter.write(user, fieldMapping);
		}
		
		csvWriter.close();
		
		
	}

}
