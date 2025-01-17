package in.pickright.service;

import in.pickright.dto.UserRequest;
import in.pickright.dto.UserResponse;

public interface UserService {

	UserResponse registerUser(UserRequest request);
	
	UserResponse loginUser(String emailOrMobile, String password);
	
	UserResponse getUserById(String id);
	
	boolean existsByEmail(String email);
	
	boolean existsByMobileNo(String mobileNo);
	
	void deleteUserById(String id);
	
	String activeInactiveUserById(String id);

}
