package in.pickright.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.pickright.constants.UserApiConstants;
import in.pickright.constants.UserCommonMessages;
import in.pickright.dto.ApiResponse;
import in.pickright.dto.UserRequest;
import in.pickright.dto.UserResponse;
import in.pickright.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(path = UserApiConstants.USER_REQUEST_MAPPING)
@Slf4j
public class UserController {

	@Autowired
	private UserService userService;
	
	@Operation(summary = "This api is used to register new users.")
	@PostMapping(path = UserApiConstants.REGISTER_USER)
	public ResponseEntity<?> register(@RequestBody UserRequest user){
		log.info("Request recieved into register api with {} "+user);
		try {
			return new ResponseEntity<ApiResponse<UserResponse>>(new ApiResponse<UserResponse>(UserCommonMessages.SUCCESS, HttpStatus.CREATED.name(), String.valueOf(HttpStatus.CREATED.value()), userService.registerUser(user)), HttpStatus.CREATED);
		} catch (Exception e) {
			log.error("Problem occurred : {}",e.getMessage());
			return new ResponseEntity<ApiResponse<String>>(new ApiResponse<String>(UserCommonMessages.FAILED, HttpStatus.INTERNAL_SERVER_ERROR.name(), String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Operation(summary = "This api is used to login users.")
	@GetMapping(path = UserApiConstants.LOGIN_USER)
	public ResponseEntity<?> login(@RequestParam String emailOrMobileNo, @RequestParam String password){
		log.info("Request recieved into login api with {} "+emailOrMobileNo);
		try {
			return new ResponseEntity<ApiResponse<UserResponse>>(new ApiResponse<UserResponse>(UserCommonMessages.SUCCESS, HttpStatus.OK.name(), String.valueOf(HttpStatus.OK.value()), userService.loginUser(emailOrMobileNo,password)), HttpStatus.OK);
		} catch (Exception e) {
			log.error("Problem occurred : {}",e.getMessage());
			return new ResponseEntity<ApiResponse<String>>(new ApiResponse<String>(UserCommonMessages.FAILED, HttpStatus.INTERNAL_SERVER_ERROR.name(), String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Operation(summary = "This api is used to check user is new or old.")
	@GetMapping(path = UserApiConstants.CHECK_USER_BY+"/email")
	public ResponseEntity<?> checkEmailExists(@RequestParam String email){
		log.info("Request recieved into login api with {} "+email);
		try {
			return new ResponseEntity<ApiResponse<Boolean>>(new ApiResponse<Boolean>(UserCommonMessages.SUCCESS, HttpStatus.OK.name(), String.valueOf(HttpStatus.OK.value()), userService.existsByEmail(email)), HttpStatus.OK);
		} catch (Exception e) {
			log.error("Problem occurred : {}",e.getMessage());
			return new ResponseEntity<ApiResponse<String>>(new ApiResponse<String>(UserCommonMessages.FAILED, HttpStatus.INTERNAL_SERVER_ERROR.name(), String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Operation(summary = "This api is used to check user is new or old.")
	@GetMapping(path = UserApiConstants.CHECK_USER_BY+"/mobileNo")
	public ResponseEntity<?> checkMobileNoExists(@RequestParam String mobileNo){
		log.info("Request recieved into login api with {} "+mobileNo);
		try {
			return new ResponseEntity<ApiResponse<Boolean>>(new ApiResponse<Boolean>(UserCommonMessages.SUCCESS, HttpStatus.OK.name(), String.valueOf(HttpStatus.OK.value()), userService.existsByMobileNo(mobileNo)), HttpStatus.OK);
		} catch (Exception e) {
			log.error("Problem occurred : {}",e.getMessage());
			return new ResponseEntity<ApiResponse<String>>(new ApiResponse<String>(UserCommonMessages.FAILED, HttpStatus.INTERNAL_SERVER_ERROR.name(), String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Operation(summary = "This api is used to get user by provided id.")
	@GetMapping(path = UserApiConstants.GET_USER_BY_ID)
	public ResponseEntity<?> getUserById(@RequestParam("userId") String id){
		log.info("Request recieved into user get by id api with {} "+id);
		try {
			return new ResponseEntity<ApiResponse<UserResponse>>(new ApiResponse<UserResponse>(UserCommonMessages.SUCCESS, HttpStatus.OK.name(), String.valueOf(HttpStatus.OK.value()), userService.getUserById(id)), HttpStatus.OK);
		} catch (Exception e) {
			log.error("Problem occurred : {}",e.getMessage());
			return new ResponseEntity<ApiResponse<String>>(new ApiResponse<String>(UserCommonMessages.FAILED, HttpStatus.INTERNAL_SERVER_ERROR.name(), String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Operation(summary = "This api is used to delete user by provided id.")
	@DeleteMapping(path = UserApiConstants.DELETE_USER_BY_ID)
	public ResponseEntity<?> deleteUserById(@RequestParam("userId") String id){
		log.info("Request recieved into delete user by id api with {} "+id);
		try {
			userService.deleteUserById(id);
			return new ResponseEntity<ApiResponse<String>>(new ApiResponse<String>(UserCommonMessages.SUCCESS, HttpStatus.OK.name(), String.valueOf(HttpStatus.OK.value()), UserCommonMessages.DELETED_SUCCESSFULLY), HttpStatus.OK);
		} catch (Exception e) {
			log.error("Problem occurred : {}",e.getMessage());
			return new ResponseEntity<ApiResponse<String>>(new ApiResponse<String>(UserCommonMessages.FAILED, HttpStatus.INTERNAL_SERVER_ERROR.name(), String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Operation(summary = "This api is used to inactiv user by provided id.")
	@PatchMapping(path = UserApiConstants.INACTIVATE_USER_BY_ID)
	public ResponseEntity<?> inactiveUserById(@RequestParam("userId") String id){
		log.info("Request recieved into delete user by id api with {} "+id);
		try {
			return new ResponseEntity<ApiResponse<String>>(new ApiResponse<String>(UserCommonMessages.SUCCESS, HttpStatus.OK.name(), String.valueOf(HttpStatus.OK.value()), userService.activeInactiveUserById(id)), HttpStatus.OK);
		} catch (Exception e) {
			log.error("Problem occurred : {}",e.getMessage());
			return new ResponseEntity<ApiResponse<String>>(new ApiResponse<String>(UserCommonMessages.FAILED, HttpStatus.INTERNAL_SERVER_ERROR.name(), String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
