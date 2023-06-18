package controller;

import java.io.IOException;

import service.RailwayCrossServiceImpl;

public class RailwayCrossingController {
	

	//controls railway crossing related operations
	//control will be given to RailwayServiceImpl class from here
	RailwayCrossServiceImpl rcimpl = new RailwayCrossServiceImpl();

	public void addRailwayCross() throws IOException {

		rcimpl.addRailwayCross();
	}

	public void displayData() {
		rcimpl.displayData();
		
	}

	public void searchRc() {
		rcimpl.searchRc();
		
	}

	public void sortRc() {
		rcimpl.sort();
		
	}

	public void updateRc() {
		rcimpl.updateRailwayCrossing();
		
	}

	public void deleteRc() {
		rcimpl.deleteRc();
		
	}

	
}
