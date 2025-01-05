package PgHstl;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;


public class Pg extends HttpServlet {
    
    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/MyPgHostel"; // Update with your database URL
    private static final String DB_USERNAME = "root"; // Your database username
    private static final String DB_PASSWORD = "password"; // Your database password

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get the person ID and person name from the request
        String personIdStr = request.getParameter("personId");
        String personName = request.getParameter("personName");
        
        // Initialize response content type
        response.setContentType("text/html");

        // Prepare PrintWriter for writing the response
        PrintWriter out = response.getWriter();
        
        // Validate input
        if (personIdStr == null || personName == null || personIdStr.isEmpty() || personName.isEmpty()) {
            out.println("<h3>Please enter valid Person ID and Name.</h3>");
            return;
        }

        int personId = Integer.parseInt(personIdStr);

        // SQL query to fetch the resident details
        String query = "SELECT * FROM MyPgHostel WHERE Person_id = ? AND Person_name = ?";
        
        // Try to fetch data from the database
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set parameters
            stmt.setInt(1, personId);
            stmt.setString(2, personName);

            // Execute the query
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                // Fetch the details from the ResultSet
                int roomNo = rs.getInt("Room_no");
                String feeStatus = rs.getString("Fee_status");
                long contactNumber = rs.getLong("contact_number");

                // Display the results
                out.println("<html><body>");
                out.println("<h2>Resident Details</h2>");
                out.println("<table border='1'>");
                out.println("<tr><th>Person ID</th><td>" + rs.getInt("Person_id") + "</td></tr>");
                out.println("<tr><th>Person Name</th><td>" + rs.getString("Person_name") + "</td></tr>");
                out.println("<tr><th>Room No</th><td>" + roomNo + "</td></tr>");
                out.println("<tr><th>Fee Status</th><td>" + feeStatus + "</td></tr>");
                out.println("<tr><th>Contact Number</th><td>" + contactNumber + "</td></tr>");
                out.println("</table>");
                out.println("</body></html>");
            } else {
                // If no result found
                out.println("<h3>No resident found with the given ID and Name.</h3>");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            out.println("<h3>Error while fetching details from the database.</h3>");
        }
    }

    // doGet method to handle GET requests (optional, for initial page)
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Display the form again if GET request is made
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        out.println("<html><body>");
        out.println("<h2>MyPgHostel - Resident Details</h2>");
        out.println("<form action='PgServlet' method='POST'>");
        out.println("<label for='personId'>Enter Person ID: </label><input type='text' name='personId'><br><br>");
        out.println("<label for='personName'>Enter Person Name: </label><input type='text' name='personName'><br><br>");
        out.println("<input type='submit' value='Submit'>");
        out.println("</form>");
        out.println("</body></html>");
    }
}
