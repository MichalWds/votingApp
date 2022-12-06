package interview.michalwds.votingApp.service;

import interview.michalwds.votingApp.exception.UserException;
import interview.michalwds.votingApp.model.Role;
import interview.michalwds.votingApp.model.User;
import interview.michalwds.votingApp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findById(Long id) throws UserException {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            log.info("User: {} has been found.", user.get().getName());
            return user;
        } else {
            throw new UserException();
        }
    }

    public Optional<User> findByName(String name) throws UserException {
        Optional<User> user = userRepository.findByName(name);

        if (user.isPresent()) {
            return user;
        } else {
            throw new UserException();
        }
    }

    //add user (candidate or voter)
    public User createUser(User user) {
        try {
            //check if name contains ONLY alphabets
            if (user.getName().matches("[a-zA-Z]+")) {
                userRepository.save(user);
                log.info("User {} has been created.", user.getName());
                return user;
            } else {
                log.info("Invalid name. Name must contains only alphabets");
            }
        } catch (Exception ex) {
            log.error("Error while trying to add user. User with given name already exists.");
        }
        return null;
    }

    public List<User> findAllCandidates() {
        List<User> users = userRepository.findAll();
        return users.stream().filter(candidate -> candidate.getRole().equals(Role.CANDIDATE)).collect(Collectors.toList());
    }

    public List<User> findAllVoters() {
        List<User> users = userRepository.findAll();
        return users.stream().filter(voter -> voter.getRole().equals(Role.VOTER)).collect(Collectors.toList());
    }

    public List<User> findAll() throws UserException {
        List<User> users = userRepository.findAll();
        if (!users.isEmpty()) {
            return users;
        } else {
            throw new UserException();
        }
    }

    public void deleteAll() throws UserException {
        List<User> users = userRepository.findAll();
        if (!users.isEmpty()) {
            userRepository.deleteAll();
        } else {
            throw new UserException();
        }
    }

    public void deleteById(Long id) throws UserException {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.deleteById(id);
        } else {
            throw new UserException();
        }
    }
}