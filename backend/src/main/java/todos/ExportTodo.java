package todos;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet(name = "ExportTodo", value = "/todoFile")
public class ExportTodo extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        handleCors(response);
        try {
            Connection con=DataBaseUtil.getConnection();
            PreparedStatement ps=con.prepareStatement("select * from todo");
            ResultSet rs=ps.executeQuery();
            BufferedWriter bw=new BufferedWriter(new FileWriter("/Users/sahithi/Documents/Tododata.csv",true));
            while(rs.next()){
                StringBuilder sb=new StringBuilder();
                sb.append(rs.getString(1));
                sb.append(",");
                sb.append(rs.getString(2));
                sb.append(",");
                sb.append(rs.getString(3));
                sb.append(",");
                sb.append(rs.getString(4));
                bw.write(sb.toString());
                bw.newLine();
            }
            bw.flush();
            response.getWriter().println("Data exported to CSV successfully.");
            response.setStatus(HttpServletResponse.SC_OK);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        handleCors(response);
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