package todos;

import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;

@WebServlet(name = "FileUploadEx", value = "/files")
@MultipartConfig
public class FileUploadEx extends HttpServlet {
   // @Override
   // protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    //}

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
       handleCors(response);
       try {
          Connection con= DataBaseUtil.getConnection();
           PrintWriter out = response.getWriter();

           Part filepart = request.getPart("file");

          BufferedReader bf= new BufferedReader(new InputStreamReader(filepart.getInputStream()));
          String line;
          while((line=bf.readLine())!=null){
              if (line.trim().isEmpty()) {
                  continue;
              }

              String[] str=line.split(",");

              if (str.length < 4) {
                  System.out.println("Skipping invalid line: " + line);
                  continue;
              }
              java.sql.Date duedate=null;
              SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
              java.util.Date utilDate=sdf.parse(str[2]);
              duedate=new java.sql.Date(utilDate.getTime());
              PreparedStatement ps=con.prepareStatement("insert into todo(title,description,duedate,assignee) values(?,?,?,?)");
              ps.setString(1, str[0]);
              ps.setString(2, str[1]);
              ps.setDate(3, duedate);
              ps.setString(4, str[3]);
              ps.executeUpdate();
          }
       }catch (Exception e){
           e.printStackTrace();
       }

    }
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Handle preflight requests
        handleCors(response);

    }
    private void handleCors(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
    }
}