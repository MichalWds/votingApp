package interview.michalwds.votingApp.controller;

import interview.michalwds.votingApp.service.VotingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/votingApp/vote")
public class VotingController {

    private final VotingService votingService;

    public VotingController(VotingService votingService) {
        this.votingService = votingService;
    }

    @PutMapping("/{voterName}/{candidateName}")
    public ResponseEntity<String> vote(@PathVariable String voterName, @PathVariable String candidateName) {

        return votingService.vote(voterName, candidateName) != null ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }
}