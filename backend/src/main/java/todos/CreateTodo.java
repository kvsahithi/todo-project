package todos;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

@WebServlet(name = "CreateTodo", value = "/todos/*")
public class CreateTodo extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        handleCors(response);
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String pathinfo = request.getPathInfo();
        String id=(pathinfo!=null&&pathinfo.length()>1)?pathinfo.substring(1):null;
        Connection con=null;
        PreparedStatement pst=null;
        ResultSet rs=null;

        try {
            con=DataBaseUtil.getConnection();
            if(id!=null){
                pst=con.prepareStatement("select * from todo where id=?");
                pst.setInt(1,Integer.parseInt(id));
            }else {
                pst = con.prepareStatement("select * from todo order by id asc limit ? offset ?");
                int limit = Integer.parseInt(request.getParameter("limit"));
                int offset = Integer.parseInt(request.getParameter("offset"));
                pst.setInt(1,limit);
                pst.setInt(2,offset);

            }
            rs=pst.executeQuery();
            JsonArray jsonArray=new JsonArray();
            while(rs.next()){
                JsonObject jsonObject=new JsonObject();
                jsonObject.addProperty("id",rs.getString("id"));
                jsonObject.addProperty("title",rs.getString("title"));
                jsonObject.addProperty("description",rs.getString("description"));
                jsonObject.addProperty("duedate",rs.getString("duedate"));
                jsonObject.addProperty("assignee",rs.getString("assignee"));
                jsonArray.add(jsonObject);
            }
            response.setStatus(HttpServletResponse.SC_OK);
            out.println(jsonArray.toString());

        }catch(Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
         handleCors(response);
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        StringBuilder sb=new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while((line=reader.readLine())!=null){
            sb.append(line);
        }
        String json=sb.toString();
        JsonObject jsonobject=JsonParser.parseString(json).getAsJsonObject();

        String title=jsonobject.get("title").getAsString();
        String description=jsonobject.get("description").getAsString();
        String datestring=jsonobject.get("duedate").getAsString();
        String assignee=jsonobject.get("assignee").getAsString();
        int generatedId=-1;
        try{
            Date duedate=null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date utilDate=sdf.parse(datestring);
            duedate=new Date(utilDate.getTime());
            Connection con=DataBaseUtil.getConnection();
            PreparedStatement ps=con.prepareStatement("INSERT INTO todo(title, description, duedate, assignee) values(?,?,?,?) returning id");

            ps.setString(1,title);
            ps.setString(2,description);
            ps.setDate(3,duedate);
            ps.setString(4,assignee);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                generatedId = rs.getInt(1);
            }
            response.setStatus(HttpServletResponse.SC_OK);
        }catch (Exception e){
            e.printStackTrace();
            JsonObject error=new JsonObject();
            error.addProperty("error", "Database connection problem: " + e.getMessage());
            out.print(error.toString());
        }
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("id", generatedId);
        jsonResponse.addProperty("title", title);
        jsonResponse.addProperty("description", description);
        jsonResponse.addProperty("dueDate", datestring);
        jsonResponse.addProperty("assignee", assignee);
        out.write(jsonResponse.toString());
    }
   protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
         handleCors(response);
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String ids = request.getPathInfo().substring(1);
        int id = Integer.parseInt(ids);
        try {
            Connection con=DataBaseUtil.getConnection();
            PreparedStatement ps=con.prepareStatement("DELETE FROM todo WHERE id = ?");
            ps.setInt(1,id);
            int rowsDeleted=ps.executeUpdate();
            if (rowsDeleted > 0) {
                response.setStatus(HttpServletResponse.SC_OK); // Set status to 200 OK
                JsonObject jsonResponse = new JsonObject();
                jsonResponse.addProperty("message", "Todo updated successfully");
                out.write(jsonResponse.toString());
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND); // Set status to 404 Not Found if no rows were updated
                JsonObject errorResponse = new JsonObject();
                errorResponse.addProperty("error", "Todo not found with id: " + id);
                out.write(errorResponse.toString());
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         handleCors(response);
         response.setContentType("application/json");
         PrintWriter out = response.getWriter();
         StringBuilder sb1=new StringBuilder();
         BufferedReader reader1 = request.getReader();
         String line1;
         while((line1=reader1.readLine())!=null){
             sb1.append(line1);
         }
         String json1=sb1.toString();
         JsonObject jsonobject1=JsonParser.parseString(json1).getAsJsonObject();
         String title=jsonobject1.get("title").getAsString();
         String description=jsonobject1.get("description").getAsString();
         String datestring=jsonobject1.get("duedate").getAsString();
         String assignee=jsonobject1.get("assignee").getAsString();
         String ids=request.getPathInfo().substring(1);

        if (ids == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing ID");
            return;
        }
        int id = Integer.parseInt(ids);
         try{
             Date duedate=null;
             SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
             java.util.Date utilDate=sdf.parse(datestring);
             duedate=new Date(utilDate.getTime());
             Connection con=DataBaseUtil.getConnection();
             PreparedStatement ps=con.prepareStatement("UPDATE todo SET title = ?, description = ?, duedate = ?, assignee=? WHERE id = ?");
             ps.setString(1,title);
             ps.setString(2,description);
             ps.setDate(3,duedate);
             ps.setString(4,assignee);
             ps.setInt(5,id);
             int rowsUpdated = ps.executeUpdate();
             if (rowsUpdated > 0) {
                 response.setStatus(HttpServletResponse.SC_OK); // Set status to 200 OK
                 JsonObject jsonResponse = new JsonObject();
                 jsonResponse.addProperty("message", "Todo updated successfully");
                 out.write(jsonResponse.toString());
             } else {
                 response.setStatus(HttpServletResponse.SC_NOT_FOUND); // Set status to 404 Not Found if no rows were updated
                 JsonObject errorResponse = new JsonObject();
                 errorResponse.addProperty("error", "Todo not found with id: " + id);
                 out.write(errorResponse.toString());
             }
         }
         catch (Exception e){
             e.printStackTrace();
         }
    }

    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Handle preflight requests
         handleCors(response);
        response.setStatus(HttpServletResponse.SC_OK);
    }
    private void handleCors(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
    }
}