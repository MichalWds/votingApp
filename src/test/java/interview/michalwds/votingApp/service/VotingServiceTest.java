package interview.michalwds.votingApp.service;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import interview.michalwds.votingApp.model.User;
import interview.michalwds.votingApp.repository.UserRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static interview.michalwds.votingApp.model.Role.CANDIDATE;
import static interview.michalwds.votingApp.model.Role.VOTER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class VotingServiceTest {

    @Mock
    private Appender<ILoggingEvent> mockAppender;
    @Mock
    private UserRepository userRepository;

    @Mock
    private User userVoter;

    @Mock
    private User userCandidate;

    @InjectMocks
    private VotingService votingService;


    @BeforeEach
    public void setUp() {
        Logger logger = (Logger) LoggerFactory.getLogger(VotingService.class.getName());
        logger.addAppender(mockAppender);
        userVoter = new User(1L, "userVoter", VOTER, 0, false);
        userCandidate = new User(2L, "userCandidate", CANDIDATE, 0, false);
    }

    @Test
    public void testVoteSuccessfully() {

        when(userRepository.findByNameAndRoleEquals(userVoter.getName(), VOTER)).thenReturn(Optional.of(userVoter));
        when(userRepository.findByNameAndRoleEquals(userCandidate.getName(), CANDIDATE)).thenReturn(Optional.of(userCandidate));

        String response = votingService.vote(userVoter.getName(), userCandidate.getName());

        verify(userRepository, times(1)).findByNameAndRoleEquals("userVoter", VOTER);
        verify(userRepository, times(1)).findByNameAndRoleEquals("userCandidate", CANDIDATE);
        verify(userRepository, times(1)).save(userVoter);
        verify(userRepository, times(1)).save(userCandidate);

        assertThat(userVoter.isHasVoted()).isEqualTo(true);
        assertThat(userCandidate.getVotes()).isGreaterThan(0);
        assertThat(userCandidate.getVotes()).isEqualTo(1);
        assertThat(response).isEqualTo("Vote has been made successfully.");
    }

    @Test
    public void testVoteFailsNoSuchVoterOrCandidate() {

        when(userRepository.findByNameAndRoleEquals(userVoter.getName(), VOTER)).thenReturn(Optional.empty());
        when(userRepository.findByNameAndRoleEquals(userCandidate.getName(), CANDIDATE)).thenReturn(Optional.of(userCandidate));

        String response = votingService.vote(userVoter.getName(), userCandidate.getName());

        verify(userRepository, times(1)).findByNameAndRoleEquals("userVoter", VOTER);
        verify(userRepository, times(1)).findByNameAndRoleEquals("userCandidate", CANDIDATE);
        verify(userRepository, times(0)).save(userVoter);
        verify(userRepository, times(0)).save(userCandidate);

        //verify logger
        verify(mockAppender).doAppend(ArgumentMatchers.argThat(argument -> {
            assertThat(argument.getMessage(), containsString("No such voter or candidate."));
            assertThat(argument.getLevel(), Matchers.is(Level.INFO));
            return true;
        }));

        assertThat(response).isEqualTo(null);
    }

    @Test
    public void testVoteFailsVoteHasBeenMade() {
        userVoter.setHasVoted(true);

        when(userRepository.findByNameAndRoleEquals(userVoter.getName(), VOTER)).thenReturn(Optional.of(userVoter));
        when(userRepository.findByNameAndRoleEquals(userCandidate.getName(), CANDIDATE)).thenReturn(Optional.of(userCandidate));

        String response = votingService.vote(userVoter.getName(), userCandidate.getName());

        verify(userRepository, times(1)).findByNameAndRoleEquals("userVoter", VOTER);
        verify(userRepository, times(1)).findByNameAndRoleEquals("userCandidate", CANDIDATE);
        verify(userRepository, times(0)).save(userVoter);
        verify(userRepository, times(0)).save(userCandidate);

        //verify logger
        verify(mockAppender).doAppend(ArgumentMatchers.argThat(argument -> {
            assertThat(argument.getMessage(), containsString("User made his vote already."));
            assertThat(argument.getLevel(), Matchers.is(Level.INFO));
            return true;
        }));

        assertThat(response).isEqualTo(null);
    }
}