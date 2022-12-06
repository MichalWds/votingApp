package interview.michalwds.votingApp.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
@Entity(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    private Role role;

    private int votes;

    private boolean hasVoted = false;

    public User(Long id, String name, Role role, int votes, boolean hasVoted) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.votes = votes;
        this.hasVoted = hasVoted;
    }
}
