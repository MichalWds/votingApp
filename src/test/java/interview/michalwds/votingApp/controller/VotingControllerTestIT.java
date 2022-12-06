package interview.michalwds.votingApp.controller;

import interview.michalwds.votingApp.model.Role;
import interview.michalwds.votingApp.model.User;
import interview.michalwds.votingApp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static interview.michalwds.votingApp.model.Role.CANDIDATE;
import static interview.michalwds.votingApp.model.Role.VOTER;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class VotingControllerTestIT {

    private final String baseUrl = "/votingApp/vote";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @Test
    void testShouldVote() throws Exception {
        Optional<User> voter = Optional.of(new User(1L, "userVoter", VOTER, 0 , false ));
        Optional<User> candidate = Optional.of(new User(1L, "userCandidate", CANDIDATE, 0 , false ));

        when(userRepository.findByNameAndRoleEquals(voter.get().getName(), Role.VOTER)).thenReturn(voter);
        when(userRepository.findByNameAndRoleEquals(candidate.get().getName(), CANDIDATE)).thenReturn(candidate);
        mockMvc.perform(put(baseUrl + "/userVoter/userCandidate"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void testAlreadyVotedShouldNotVote() throws Exception {
        Optional<User> voter = Optional.of(new User(1L, "userVoter", VOTER, 0 , true ));
        Optional<User> candidate = Optional.of(new User(1L, "userCandidate", CANDIDATE, 0 , false ));

        when(userRepository.findByNameAndRoleEquals(voter.get().getName(), Role.VOTER)).thenReturn(voter);
        when(userRepository.findByNameAndRoleEquals(candidate.get().getName(), CANDIDATE)).thenReturn(candidate);
        mockMvc.perform(put(baseUrl + "/userVoter/userCandidate"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void testWrongCandidateDoesNotExist() throws Exception {
        Optional<User> voter = Optional.of(new User(1L, "userVoter", VOTER, 0 , false ));
        Optional<User> candidate = Optional.of(new User(1L, "notAUserCandidate", CANDIDATE, 0 , false ));


        when(userRepository.findByNameAndRoleEquals(voter.get().getName(), Role.VOTER)).thenReturn(voter);
        when(userRepository.findByNameAndRoleEquals(candidate.get().getName(), CANDIDATE)).thenReturn(candidate);
        mockMvc.perform(put(baseUrl + "/userVoter/userCandidate"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void testWrongVoterDoesNotExist() throws Exception {
        Optional<User> voter = Optional.of(new User(1L, "notAUserVoter", VOTER, 0 , false ));
        Optional<User> candidate = Optional.of(new User(1L, "userCandidate", CANDIDATE, 0 , false ));

        when(userRepository.findByNameAndRoleEquals(voter.get().getName(), Role.VOTER)).thenReturn(voter);
        when(userRepository.findByNameAndRoleEquals(candidate.get().getName(), CANDIDATE)).thenReturn(candidate);
        mockMvc.perform(put(baseUrl + "/userVoter/userCandidate"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}