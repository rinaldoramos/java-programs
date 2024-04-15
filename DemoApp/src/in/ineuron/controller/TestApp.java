package in.ineuron.controller;

import java.io.IOException;
import java.io.ObjectInputFilter.Config;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class TestApp
 */
@WebServlet(urlPatterns = { "/TestApp" }, initParams = { @WebInitParam(name = "url", value = "jdbc:mysql:///ineuron"),
		@WebInitParam(name = "user", value = "root"),
		@WebInitParam(name = "password", value = "admin") }, loadOnStartup = 10)
public class TestApp extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private Connection connection = null;


	static {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("Driver loading...");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void init() throws ServletException {
		String url = getInitParameter("url");
		String user = getInitParameter("user");
		String password = getInitParameter("password");
		
		try {
			connection = DriverManager.getConnection(url, user, password);
			System.out.println("Connection established...");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (connection != null) {
			String sql = "INSERT INTO student (sname, sage, saddress) VALUES (?,?,?)";
			try(PreparedStatement prepareStatement = connection.prepareStatement(sql)){
				if (prepareStatement != null) {
					prepareStatement.setString(1, request.getParameter("name"));
					prepareStatement.setInt(2, Integer.parseInt(request.getParameter("age")));
					prepareStatement.setString(3, request.getParameter("address"));
				}
				
				if (prepareStatement != null) {
					int rowsAffected = prepareStatement.executeUpdate();
					
					PrintWriter out = response.getWriter();
					if (rowsAffected == 1) {
						out.println("<h1 style='color: green; text-align: center;'>REGISTRATION SUCCESFULL!</h1>");
					}else {
						out.println("<h1 style='color: red; text-align: center;'>REGISTRATION FAILED! Try again with link below...</h1>");
						out.println("<a href='./reg.html'>|REGISTRATION|</a>");
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void destroy() {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
















