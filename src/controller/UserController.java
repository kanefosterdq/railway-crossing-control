package controller;

import dao.UserDAO;
import service.UserServiceImpl;

/*********** Controls all user-related operations. Control will be given to UserServiceImpl class *********/
public class UserController {								
															
	UserDAO user = new UserDAO();
	UserServiceImpl us = new UserServiceImpl();

	public void createAccount(int userType) {
		 us.addUser(userType);
	}

	public boolean loginCheck(int userType) throws Exception {

		return us.isAccountExist(userType);
	}

}
