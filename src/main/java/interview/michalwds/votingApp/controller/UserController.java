package interview.michalwds.votingApp.controller;

import interview.michalwds.votingApp.exception.UserException;
import interview.michalwds.votingApp.model.User;
import interview.michalwds.votingApp.service.UserService;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/votingApp/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Optional<User>> findById(@PathVariable Long userId) throws UserException {
        Optional<User> findById = userService.findById(userId);
        if (findById.isPresent()) {
            return ResponseEntity.ok(findById);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/name/{userName}")
    public ResponseEntity<Optional<User>> findByName(@PathVariable String userName) throws UserException {
        Optional<User> user = userService.findByName(userName);
        if (user.isPresent()) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/candidates")
    public ResponseEntity<List<User>> findAllCandidates() {
        return ResponseEntity.ok(userService.findAllCandidates());
    }

    @GetMapping("/voters")
    public ResponseEntity<List<User>> findAllVoters() {
        return ResponseEntity.ok(userService.findAllVoters());
    }

    @GetMapping
    public ResponseEntity<List<User>> findAllUsers() throws UserException {
        return ResponseEntity.ok(userService.findAll());
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody User user) {
        Optional<User> users = Optional.ofNullable(userService.createUser(user));
        if (users.isPresent()) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatusCode.valueOf(409)).build();
        }
    }

    @DeleteMapping
    public ResponseEntity<List<User>> deleteAll() throws UserException {
        userService.deleteAll();
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws UserException {
        userService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}