package ru.lappi.users.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.lappi.users.controller.data.UserData;
import ru.lappi.users.service.UserServiceImpl;

import java.util.Optional;

/**
 * @author Nikita Gorodilov
 */
@RestController
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    UserServiceImpl userService;

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> register(@RequestParam(value = "username") String username,
                                            @RequestParam(value = "password") String password
    ) {
        try {
            userService.register(username, password);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> login(@RequestParam(value = "username") String username,
                                         @RequestParam(value = "password") String password
    ) {
        try {
            boolean login = userService.login(username, password);
            if (login) {
                return new ResponseEntity<>(true, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/getUserId", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Long> getUserId(@RequestParam(value = "username") String username) {
        try {
            Optional<Long> userId = userService.getUserId(username);
            return ResponseEntity.of(userId);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/getUserData", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<UserData> getUserData(@RequestParam(value = "username") String username) {
        try {
            Optional<UserData> userData = userService.getUserByUsername(username)
                    .map(UserData::new);
            return ResponseEntity.of(userData);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/getUserDataByUserId", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<UserData> getUserData(@RequestParam(value = "user_id") Long userId) {
        try {
            Optional<UserData> userData = userService.getUserById(userId)
                    .map(UserData::new);
            return ResponseEntity.of(userData);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
