package dao;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import model.Address;
import model.RailwayCrossing;
import model.User;

public class RailwayCrossingDAO {
	UserDAO udao = new UserDAO();
	DB db = DB.getDB();

	// To add the details to database
	public synchronized void addNewRailwayCrossing(RailwayCrossing railwayCrossing, Address address, User user,
			int userType) {
		HashMap<Integer, String> trains = railwayCrossing.getTimings();
		int uid = 0;
		String userAddQuery = "insert into user_table (name,email,phone,password,user_type)" + "values('"
				+ railwayCrossing.getInchargeName().getName() + "','" + railwayCrossing.getInchargeName().getEmail()
				+ "','" + railwayCrossing.getInchargeName().getPhone() + "','"
				+ railwayCrossing.getInchargeName().getPassword() + "'" + "," + userType + ")";
		int result = db.executeSQL(userAddQuery);
		if (result > 0) {
			System.out.println("User added...");
		} else {
			System.out.println("User not added...");

		}

		// getting user_id from user table using the phone number(unique)
		String uidQuery = "Select uid from user_table where phone = '" + railwayCrossing.getInchargeName().getPhone()
				+ "'";
		ResultSet set = db.executeRetrieveQuery(uidQuery);
		try {
			if (set.next()) {
				uid = set.getInt("uid");
			}

		} catch (SQLException e) {
			System.err.println("Something went wrong! please try again later...");
		}

		String addressAddQuery = "INSERT INTO address_table(building_no,street,city,uid)" + "values("
				+ railwayCrossing.getAddress().getBuildingNo() + "," + "'" + railwayCrossing.getAddress().getStreet()
				+ "','" + railwayCrossing.getAddress().getCity() + "'," + uid + ")";

		result = db.executeSQL(addressAddQuery);
		if (result > 0) {
			System.out.println("Address added...");
		} else {
			System.out.println("Something went wrong! please try again later...");

		}
		String retrieveAid = "select aid from address_table where uid = " + uid;
		try {
			if (set.next()) {
				uid = set.getInt("uid");
			}

		} catch (SQLException e) {
			System.err.println("Something went wrong! please try again later...");
		}
		set = db.executeRetrieveQuery(retrieveAid);
		int aid = -1;
		try {
			if (set.next()) {
				aid = set.getInt("aid");
			}

		} catch (SQLException e) {
			System.err.println("Something went wrong! please try again later...");
		}

		String insertRc = "Insert into rc_table(rc_id,isOpened,aid)" + "values('" + railwayCrossing.getName() + "','"
				+ railwayCrossing.isOpened() + "'," + aid + ")";

		result = db.executeSQL(insertRc);
		if (result > 0) {
			System.out.println("Railway crossing added...");
		} else {
			System.out.println("Railway crossing not added...");

		}
		String addTrainQuery = "";
		Iterator it = trains.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry train = (Map.Entry) it.next();
			int tNo = (int) train.getKey();
			String tName = (String) train.getValue();
			addTrainQuery = "insert into trains_table(time,train_name,rc_id)" + "values(" + tNo + ",'" + tName + "','"
					+ railwayCrossing.getName() + "')";
			db.executeSQL(addTrainQuery);
		}

	}

	//Retrieving date from database and printing it
	public void displayRCData() {
		String rcRetrievalQuery = "select * from rc_table";
		ResultSet rcList = db.executeRetrieveQuery(rcRetrievalQuery);
		System.out.println("\"**********  Railway Crossing List  **********\"");
		System.out.println("RC_ID\t\t\tIsOpened\n");
		try {
			while (rcList.next()) {

				System.out.print(rcList.getString("rc_id") + "\t\t\t");
				System.out.print(rcList.getString("isOpened"));
				System.out.println();
			}
		} catch (Exception e) {
			System.out.println("Something went wrong! Please try again..");
		}
	}

	// User can search for the status of particular railway crossing
	public void searchRailwayCrossing(String rc_id) {
		String searchRcQuery = "select * from rc_table where rc_id = '" + rc_id + "'";
		ResultSet record = db.executeRetrieveQuery(searchRcQuery);
		System.out.println("\nRC_ID\t\tIsOpened?\n");
		try {
			while (record.next()) {

				System.out.print(record.getString("rc_id") + "\t\t");
				System.out.print(record.getString("isOpened") + "\n");
			}
		} catch (SQLException e) {

			System.out.println("Something went wrong! Please try again later...");
		}

	}

	// Admin can update the status of railway crossing
	public synchronized void updateRailwayCrossing(String crossing, String status) throws IOException {
		boolean newStatus = false;
		if (status.equalsIgnoreCase("opened")) {
			newStatus = true;
		}
		String updateRcQuery = "update rc_table set isOpened = '" + newStatus + "'" + "where rc_id = '" + crossing
				+ "'";
		int result = db.executeSQL(updateRcQuery);
		if (result > 0) {
			System.out.println("Updated successfully!");
		} else {
			System.err.println("Something went wrong! Please try again...");
		}

	}

	//deletes a railway crossing from data base = with the related user and address details
	synchronized public void deleteRailwayCrossing(String crossing) {
		int aid = 0;
		int uid = 0;
		ResultSet rset = null;
		String aidRetrievalQuery = "select aid from rc_table where rc_id = '" + crossing + "'";
		rset = db.executeRetrieveQuery(aidRetrievalQuery);
		try {
			while (rset.next()) {
				aid = rset.getInt("aid");
			}

		} catch (SQLException e) {
			System.err.println("Something went wrong! please try again later...");
		}
		String uidRetrievalQuery = "select uid from address_table where aid = " + aid;
		rset = db.executeRetrieveQuery(uidRetrievalQuery);
		try {
			if (rset.next()) {
				uid = rset.getInt("uid");
			}

		} catch (SQLException e) {
			System.err.println("Something went wrong! please try again later...");
		}
		String delRcQuery = "delete from user_table where uid = " + uid;

		int result = db.executeSQL(delRcQuery);
		if (result >= 0) {
			System.out.println("Raliway crossing " + crossing + " has been deleted successfully!");
		} else {
			System.out.println("Something went wrong! Please try again later...");
		}
	}

	// Sorting the railway crossings based on the train timings of the railway crossing
	public void sortRailwayCrossing() {
		// select * from rc_table left join trains_table on rc_table.rc_id =
		// trains_table.rc_id
		String sortRcQuery = "select * from rc_table left join trains_table on rc_table.rc_id = trains_table.rc_id";
		ResultSet sortList = db.executeRetrieveQuery(sortRcQuery);
		try {
			while (sortList.next()) {

				System.out.print(sortList.getString("rc_id") + "\t\t");
				System.out.print(sortList.getString("train_name") + "\t");
				System.out.print(sortList.getInt("time") + "\t\t");
				String status = sortList.getString("isOpened");
				if (status.equals("true")) {
					System.out.print("Opened");
				} else
					System.out.print("Closed");
				System.out.println();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
