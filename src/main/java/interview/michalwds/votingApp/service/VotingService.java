package interview.michalwds.votingApp.service;

import interview.michalwds.votingApp.model.Role;
import interview.michalwds.votingApp.model.User;
import interview.michalwds.votingApp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class VotingService {

    private final UserRepository userRepository;

    public VotingService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String vote(String voterName, String candidateName) {
        Optional<User> voter = userRepository.findByNameAndRoleEquals(voterName, Role.VOTER);
        Optional<User> candidate = userRepository.findByNameAndRoleEquals(candidateName, Role.CANDIDATE);

        if (voter.isPresent() && candidate.isPresent()) {
            if (!voter.get().isHasVoted()) {
                voter.get().setHasVoted(true);
                int votes = candidate.get().getVotes();
                candidate.get().setVotes(++votes);

                userRepository.save(candidate.get());
                userRepository.save(voter.get());
                log.info("Vote has been made successfully.");
                return "Vote has been made successfully.";
            } else {
                log.info("User made his vote already.");
                return null;
            }
        } else {
            log.info("No such voter or candidate.");
            return null;

        }
    }
}
