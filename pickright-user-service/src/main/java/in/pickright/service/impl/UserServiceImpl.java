package in.pickright.service.impl;

import static in.pickright.helper.UserFieldValidation.validateFields;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.pickright.constants.UserCommonMessages;
import in.pickright.dto.UserRequest;
import in.pickright.dto.UserResponse;
import in.pickright.entity.User;
import in.pickright.mapper.UserMapper;
import in.pickright.respository.UserRepository;
import in.pickright.service.UserService;
import in.pickright.utils.PasswordUtils;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserResponse registerUser(UserRequest request) {
		log.info("Request recieved into user service.");
		validateFields(request);
		log.info("Request fields are validated.");
		isAlreadyRegistered(request);
		log.info("Requested user is new user.");
		User user = UserMapper.convertUserReqToUser(request);
		log.info("Request converted to User.");
		User savedUser = userRepository.save(user);
		log.info("User registered succesfully.");
		return UserMapper.convertUserToUserRes(savedUser);
	}

	@Override
	public UserResponse loginUser(String emailOrMobile, String password) {
		log.info("Request recieved into user service.");
		loginValidation(emailOrMobile, password);
		log.info("Login validation done.");
		User user;
		if (Boolean.TRUE.equals(isEmail(emailOrMobile))) {
			user = userRepository.findByEmailAndIsDeletedFalseAndIsActiveTrue(emailOrMobile)
					.orElseThrow(() -> new RuntimeException("Email is " + UserCommonMessages.NOT_REGISTERED));
			log.info("User data is fetched with email : {}", emailOrMobile);
		} else {
			user = userRepository.findByMobileNoAndIsDeletedFalseAndIsActiveTrue(emailOrMobile)
					.orElseThrow(() -> new RuntimeException("Mobile number is " + UserCommonMessages.NOT_REGISTERED));
			log.info("User data is fetch with mobile no : {}", emailOrMobile);
		}

		if (Boolean.FALSE.equals(PasswordUtils.isMatched(password, user.getPassword()))) {
			log.error(UserCommonMessages.INCORRECT_PASSWORD);
			throw new RuntimeException(UserCommonMessages.INCORRECT_PASSWORD);
		}
		
		log.info("User response is ready to send");
		return UserMapper.convertUserToUserRes(user);
	}

	private void loginValidation(String emailOrMobile, String password) {
		if (emailOrMobile == null || emailOrMobile.isEmpty()) {
			log.error(UserCommonMessages.EMAIL_OR_MOBILE_NO_REQUIRED);
			throw new RuntimeException(UserCommonMessages.EMAIL_OR_MOBILE_NO_REQUIRED);
		}
		if (password == null || password.isEmpty()) {
			log.error(UserCommonMessages.PASSWORD_REQUIRED);
			throw new RuntimeException(UserCommonMessages.PASSWORD_REQUIRED);
		}

	}

	private boolean isEmail(String emailOrMobile) {
		if (Boolean.TRUE.equals(emailOrMobile.contains("@"))) {
			log.info("User is trying to login with email : {}", emailOrMobile);
			return Boolean.TRUE;
		} else {
			log.info("User is trying to login with mobile no : {}", emailOrMobile);
			return Boolean.FALSE;
		}
	}

	private void isAlreadyRegistered(UserRequest request) {
		log.info("Request recieved into user service.");
		if (Boolean.TRUE.equals(userRepository.existsByEmailAndIsDeletedFalseAndIsActiveTrue(request.getEmail()))) {
			log.error("{} is already registered.", request.getEmail());
			throw new RuntimeException("Email is " + UserCommonMessages.ALREADY_REGISTERED);
		}

		if (Boolean.TRUE
				.equals(userRepository.existsByMobileNoAndIsDeletedFalseAndIsActiveTrue(request.getMobileNo()))) {
			log.error("{} is already registered.", request.getMobileNo());
			throw new RuntimeException("Mobile number is " + UserCommonMessages.ALREADY_REGISTERED);
		}

	}

	@Override
	public boolean existsByEmail(String email) {
		log.info("Request recieved into user service.");
		return userRepository.existsByEmailAndIsDeletedFalseAndIsActiveTrue(email);
	}

	@Override
	public boolean existsByMobileNo(String mobileNo) {
		log.info("Request recieved into user service.");
		return userRepository.existsByMobileNoAndIsDeletedFalseAndIsActiveTrue(mobileNo);
	}

	@Override
	public UserResponse getUserById(String id) {
		log.info("Request recieved into user service.");
		if (id == null || Boolean.TRUE.equals(id.isEmpty())) {
			log.error(UserCommonMessages.USER_ID_REQUIRED);
			throw new RuntimeException(UserCommonMessages.USER_ID_REQUIRED);
		}
		User user = userRepository.findByIdAndIsDeletedFalseAndIsActiveTrue(id)
				.orElseThrow(() -> new RuntimeException(UserCommonMessages.NOT_FOUND));
		return UserMapper.convertUserToUserRes(user);
	}

	@Override
	public void deleteUserById(String id) {
		log.info("Request recieved into user service.");
		User user = userRepository.findByIdAndIsDeletedFalseAndIsActiveTrue(id).orElseThrow(
				() -> new RuntimeException(UserCommonMessages.NOT_FOUND + " or " + UserCommonMessages.ALREADY_DELETED));
		user.setIsDeleted(Boolean.TRUE);
		User deletedUser = userRepository.save(user);
		log.info("User assosiated with {} deleted succesfully.", deletedUser.getEmail());
	}

	@Override
	public String activeInactiveUserById(String id) {
		String msg = "";
		log.info("Request recieved into user service.");
		User user = userRepository.findByIdAndIsDeletedFalse(id)
				.orElseThrow(() -> new RuntimeException(UserCommonMessages.NOT_FOUND));
		if (Boolean.TRUE.equals(user.getIsActive())) {
			user.setIsActive(Boolean.FALSE);
			msg = UserCommonMessages.INACTIVED_SUCCESSFULLY;
			log.info(UserCommonMessages.INACTIVED_SUCCESSFULLY);
		} else {
			user.setIsActive(Boolean.TRUE);
			msg = UserCommonMessages.ACTIVED_SUCCESSFULLY;
			log.info(UserCommonMessages.ACTIVED_SUCCESSFULLY);
		}
		userRepository.save(user);
		return msg;
	}

}
