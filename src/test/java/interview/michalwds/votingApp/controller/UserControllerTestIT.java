package interview.michalwds.votingApp.controller;

import interview.michalwds.votingApp.model.User;
import interview.michalwds.votingApp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static interview.michalwds.votingApp.model.Role.CANDIDATE;
import static interview.michalwds.votingApp.model.Role.VOTER;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class UserControllerTestIT {

    private final String baseUrl = "/votingApp/users";
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @Mock
    private User voter;

    @Mock
    private User candidate;


    @BeforeEach
    private void setUp(){
        voter = new User(1L, "userVoter", VOTER, 0, false);
        candidate = new User(2L, "userCandidate", CANDIDATE, 0, false);
    }

    @Test
    void testShouldFindUserById() throws Exception {
        Optional<User> user = Optional.of(voter);

        when(userRepository.findById(user.get().getId())).thenReturn(user);
        mockMvc.perform(get(baseUrl + "/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("{\"id\":1,\"name\":\"userVoter\",\"role\":\"VOTER\",\"votes\":0,\"hasVoted\":false}")));
    }

    @Test
    void testShouldNotFindUserByIdThrowException() throws Exception {
        Optional<User> user = Optional.of(voter);

        when(userRepository.findById(user.get().getId())).thenReturn(Optional.empty());
        mockMvc.perform(get(baseUrl + "/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(equalTo("Not found any user. Cause: null")));
    }

    @Test
    void testShouldFindUserByName() throws Exception {
        Optional<User> user = Optional.of(voter);

        when(userRepository.findByName(user.get().getName())).thenReturn(user);
        mockMvc.perform(get(baseUrl + "/name/userVoter"))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("{\"id\":1,\"name\":\"userVoter\",\"role\":\"VOTER\",\"votes\":0,\"hasVoted\":false}")));
    }

    @Test
    void testShouldNotFindUserByNameThrowException() throws Exception {
        Optional<User> user = Optional.of(voter);

        when(userRepository.findByName(user.get().getName())).thenReturn(Optional.empty());
        mockMvc.perform(get(baseUrl + "/name/userVoter"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(equalTo("Not found any user. Cause: null")));
    }

    @Test
    void testShouldFindAllVoters() throws Exception {
        List<User> users = new ArrayList<>();
        users.add(voter);
        users.add(candidate);

        when(userRepository.findAll()).thenReturn(users);
        mockMvc.perform(get(baseUrl + "/voters"))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("[{\"id\":1,\"name\":\"userVoter\",\"role\":\"VOTER\",\"votes\":0,\"hasVoted\":false}]")));
    }

    @Test
    void testShouldFindAllCandidates() throws Exception {
        List<User> users = new ArrayList<>();
        users.add(voter);
        users.add(candidate);

        when(userRepository.findAll()).thenReturn(users);
        mockMvc.perform(get(baseUrl + "/candidates"))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("[{\"id\":2,\"name\":\"userCandidate\",\"role\":\"CANDIDATE\",\"votes\":0,\"hasVoted\":false}]")));
    }

    @Test
    void testShouldFindAllUsers() throws Exception {
        List<User> users = new ArrayList<>();
        users.add(voter);
        users.add(candidate);

        when(userRepository.findAll()).thenReturn(users);
        mockMvc.perform(get(baseUrl))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("[{\"id\":1,\"name\":\"userVoter\",\"role\":\"VOTER\",\"votes\":0,\"hasVoted\":false},{\"id\":2,\"name\":\"userCandidate\",\"role\":\"CANDIDATE\",\"votes\":0,\"hasVoted\":false}]")));
    }

    @Test
    void testShouldCreateUser() throws Exception {

        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":\"userVoter\",\"role\":\"VOTER\",\"votes\":0,\"hasVoted\":false}"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void testShouldNotCreateUser() throws Exception {

        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":\"notAUser1234\",\"role\":\"VOTER\",\"votes\":0,\"hasVoted\":false}"))
                .andDo(print())
                .andExpect(status().is(409));
    }

    @Test
    void testShouldDeleteAll() throws Exception {
        List<User> users = new ArrayList<>();
        users.add(voter);
        users.add(candidate);

        when(userRepository.findAll()).thenReturn(users);
        mockMvc.perform(delete(baseUrl))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("")));
    }

    @Test
    void testShouldNotDeleteAllEmptyList() throws Exception {
        List<User> users = new ArrayList<>();

        when(userRepository.findAll()).thenReturn(users);
        mockMvc.perform(delete(baseUrl))
                .andExpect(status().isNotFound())
                .andExpect(content().string(equalTo("Not found any user. Cause: null")));
    }

    @Test
    void testShouldDeleteById() throws Exception {
        Optional<User> user = Optional.of(voter);

        when(userRepository.findById(user.get().getId())).thenReturn(user);
        mockMvc.perform(delete(baseUrl + "/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("")));
    }

    @Test
    void testShouldNotDeleteById() throws Exception {
        Optional<User> user = Optional.of(new User());

        when(userRepository.findById(user.get().getId())).thenReturn(user);
        mockMvc.perform(delete(baseUrl + "/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(equalTo("Not found any user. Cause: null")));
    }
}