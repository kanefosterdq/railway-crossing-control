package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import model.User;

/**************************
 * This has all the user related operations
 **************/
public class UserDAO {

	Scanner sc = new Scanner(System.in);
	DB db = DB.getDB();

	public void createUser(User user, int userType) {

		int uid = 0;
		String userAddQuery = "insert into user_table (name,email,phone,password,user_type)" + "values('"
				+ user.getName() + "','" + user.getEmail() + "','" + user.getPhone() + "','" + user.getPassword() + "'"
				+ "," + userType + ")";
		int result = db.executeSQL(userAddQuery);
		if (result > 0) {
			System.out.println("User added...");
		} else {
			System.out.println("User not added...");

		}

		// getting user_id from user table using the phone number(unique)
		String uidQuery = "Select uid from user_table where phone = '" + user.getPhone() + "'";
		ResultSet set = db.executeRetrieveQuery(uidQuery);
		try {
			if (set.next()) {
				uid = set.getInt("uid");
			}

		} catch (SQLException e) {
			System.err.println("Something went wrong! please try again later...");
		}

		String addressAddQuery = "INSERT INTO address_table(building_no,street,city,uid)" + "values("
				+ user.getAddress().getBuildingNo() + "," + "'" + user.getAddress().getStreet() + "','"
				+ user.getAddress().getCity() + "'," + uid + ")";

		result = db.executeSQL(addressAddQuery);
		if (result > 0) {
			System.out.println("Address added...");
		} else {
			System.out.println("Something went wrong! please try again later...");

		}
	}

	//check if the user account exists - if exist, it allows the user to do the operations
	public boolean isAccountPresent(int userType, String email, String password) {
		String passwordInDb = "";

		String passwordRetrievalQuery = " ";
		if (userType == 1) {
			passwordRetrievalQuery = "select password from user_table where email = '" + email + "'"
					+ "and user_type = 1";
		} else if (userType == 2) {
			passwordRetrievalQuery = "select password from user_table where email = '" + email + "'"
					+ "and user_type = 2";
		}

		ResultSet passwordSet = db.executeRetrieveQuery(passwordRetrievalQuery);
		try {
			if (passwordSet.next()) {
				passwordInDb = passwordSet.getString("password");
			}
			if (password.equals(passwordInDb)) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			System.out.println("Something went wrong! Please try again later...");
		}

		return false;

	}

}
