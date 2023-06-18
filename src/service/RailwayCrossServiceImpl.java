package service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import dao.RailwayCrossingDAO;
import model.Address;
import model.RailwayCrossing;
import model.User;

/****** All railway crossing related operations have been implemented here.
 *Operations involving database are forwarded to RailwayCrossingDAO class ****/
public class RailwayCrossServiceImpl implements RailwayCrossService {

	Scanner sc = new Scanner(System.in);
	List<RailwayCrossing> rclist = new ArrayList<>();		//can take many railway crossing objects
	RailwayCrossingDAO rcdao = new RailwayCrossingDAO();

	public void addRailwayCross() throws IOException {      // getting railway crossing details from user

		Address address = new Address();
		User user = new User();
		HashMap<Integer, String> timings = new HashMap<>();
		boolean repeat = true;
		int i = 0;
		while (repeat) {

			System.out.println("\nEnter railway crossing details");
			rclist.add(new RailwayCrossing());
			System.out.print("\nName : ");
			// sc.nextLine();
			rclist.get(i).setName(sc.next());
			System.out.print("\nIs opened ? (true / false ): ");
			rclist.get(i).setOpened(sc.nextBoolean());
			System.out.println("\nEnter the address");
			System.out.print("\nBuilding No : ");
			address.setBuildingNo(sc.next());
			System.out.print("\nStreet : ");
			sc.nextLine();
			address.setStreet(sc.nextLine());
			System.out.print("\nCity : ");
			address.setCity(sc.nextLine());
			System.out.println("\nRailway Crossing station In-charge details");
			System.out.print("\nName : ");
			user.setName(sc.nextLine());
			System.out.print("\nEmail : ");
			user.setEmail(sc.nextLine().toLowerCase());
			user.setPassword(user.getEmail() + "@password");
			System.out.print("\nPhone : ");
			user.setPhone(sc.nextLine());

			rclist.get(i).setInchargeName(user);
			rclist.get(i).setAddress(address);

			System.out.println("\nFormat - Time(HHMM) <SPACE> Train name");
			boolean anotherEntry = true;
			while (anotherEntry) {
				int time = sc.nextInt();
				while (true) {
					if (time > 2400) {
						System.out.println("Please enter between 0000 to 2359");
					} else
						break;
				}
				timings.put(time, sc.nextLine());
				System.out.print("\nAdd another? (y/n) : ");
				if (sc.next().charAt(0) == 'n') {
					anotherEntry = false;
				}
			}
			rclist.get(i).setTimings(timings);

			rcdao.addNewRailwayCrossing(rclist.get(i), address, user, 2); // 2 is given--> admin //passing to
																			// RailwayCrossingDAO to store
			System.out.print("\nWant to add more data? (y / n) : ");
			if (sc.next().charAt(0) == 'n') {
				repeat = false;
			}
			i++;
			timings.clear(); // reusing for other objects

		}

	}

	public void displayData() {

		rcdao.displayRCData();

	}

	public void searchRc() {
		System.out.println("\nRailway Crossing ID : ");
		String crossing = sc.nextLine();
		try {
			rcdao.searchRailwayCrossing(crossing);
		} catch (Exception e) {

		}

	}

	public void sort() {
		try {
			rcdao.sortRailwayCrossing();
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	public void updateRailwayCrossing() {
		System.out.print("\nRailway Crossing ID : ");
		String crossing = sc.nextLine();
		System.out.print("\nUpdate the status to ? (opened or closed) : ");
		String status = sc.nextLine();

		try {
			rcdao.updateRailwayCrossing(crossing, status);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void deleteRc() {
		System.out.print("\nRailway Crossing ID : ");
		String crossing = sc.next();

		rcdao.deleteRailwayCrossing(crossing);

	}

}
