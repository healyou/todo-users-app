package ru.lappi.users.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.lappi.users.service.UserServiceImpl;

/**
 * @author Nikita Gorodilov
 */
@RestController
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    UserServiceImpl userService;

    @PostMapping(value = "register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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

    @PostMapping(value = "login", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Boolean> login(@RequestParam(value = "username") String username,
                                         @RequestParam(value = "password_hash") String passwordHash
    ) {
        try {
            boolean login = userService.login(username, passwordHash);
            return new ResponseEntity<>(login, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }

    }
}
