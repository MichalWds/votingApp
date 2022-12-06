package interview.michalwds.votingApp.service;

import interview.michalwds.votingApp.exception.UserException;
import interview.michalwds.votingApp.model.User;
import interview.michalwds.votingApp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static interview.michalwds.votingApp.model.Role.CANDIDATE;
import static interview.michalwds.votingApp.model.Role.VOTER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private User userVoter;

    @Mock
    private User userCandidate;

    private final List<User> users = new ArrayList<>();

    @InjectMocks
    private UserService userService;


    @BeforeEach
    public void setUp() {
        userVoter = new User(1L, "userVoter", VOTER, 0, false);
        userCandidate = new User(2L, "userCandidate", CANDIDATE, 0, false);
        users.add(userVoter);
        users.add(userCandidate);
    }

    @Test
    public void testFindAllUCandidatesCorrectly() {
        when(userRepository.findAll()).thenReturn((users));

        List<User> users = userService.findAllCandidates();

        assertThat(users).isNotEmpty();
        assertThat(users).size().isGreaterThan(0);
        assertThat(users).size().isEqualTo(1);
        assertThat(users.get(0).getRole()).isEqualTo(CANDIDATE);
    }

    @Test
    public void testFindAllVotersCorrectly() {
        when(userRepository.findAll()).thenReturn((users));

        List<User> users = userService.findAllVoters();

        assertThat(users).isNotEmpty();
        assertThat(users).size().isGreaterThan(0);
        assertThat(users).size().isEqualTo(1);
        assertThat(users.get(0).getRole()).isEqualTo(VOTER);
    }

    @Test
    public void testFindByNameCorrectly() throws UserException {
        when(userRepository.findByName("userVoter")).thenReturn(Optional.of(userVoter));

        Optional<User> user = userService.findByName("userVoter");

        assertThat(user).isNotEmpty();
        assertThat(user.get().getName()).isEqualTo("userVoter");
    }

    @Test
    public void testFindByNameThrowUserException() {
        assertThatExceptionOfType(UserException.class).isThrownBy(() -> userService.findByName(userVoter.getName()));
    }

    @Test
    public void testCreateCorrectly() {
        when(userRepository.save(userCandidate)).thenReturn(userCandidate);

        User user = userService.createUser(userCandidate);

        verify(userRepository, times(1)).save(user);
        assertThat(user.getId()).isEqualTo(2);
    }

    @Test
    public void testCreateUserFailsWrongName() {
        userVoter.setName("notAName12345");

        User user = userService.createUser(userVoter);

        verify(userRepository, times(0)).save(user);
    }

    @Test
    public void testFindByIdCorrectly() throws UserException {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userVoter));

        Optional<User> user = userService.findById(1L);

        assertThat(user).isNotEmpty();
        assertThat(user.get().getName()).isEqualTo("userVoter");
    }

    @Test
    public void testFindByIdThrowUserException() {
        assertThatExceptionOfType(UserException.class).isThrownBy(() -> userService.findById(userVoter.getId()));
    }

    @Test
    public void testFindAllUsersCorrectly() throws UserException {
        when(userRepository.findAll()).thenReturn((users));

        List<User> users = userService.findAll();

        assertThat(users).isNotEmpty();
        assertThat(users).size().isGreaterThan(0);
    }

    @Test
    public void testFindAllUsersThrowUserException() {
        assertThatExceptionOfType(UserException.class).isThrownBy(() -> userService.findAll());
    }

    @Test
    public void testDeleteUserById() throws UserException {
        when(userRepository.findById(userVoter.getId())).thenReturn(Optional.of(userVoter));

        userService.deleteById(userVoter.getId());

        verify(userRepository, times(1)).deleteById(userVoter.getId());
    }

    @Test
    public void testDeleteUserByIdThrowUserException() {
        assertThatExceptionOfType(UserException.class).isThrownBy(() -> userService.deleteById(userVoter.getId()));
    }

    @Test
    public void testDeleteAllUsers() throws UserException {
        when(userRepository.findAll()).thenReturn(users);

        userService.deleteAll();

        verify(userRepository, times(1)).deleteAll();
    }

    @Test
    public void testDeleteAllUsersThrowUserException() {
        assertThatExceptionOfType(UserException.class).isThrownBy(() -> userService.deleteAll());
    }
}
