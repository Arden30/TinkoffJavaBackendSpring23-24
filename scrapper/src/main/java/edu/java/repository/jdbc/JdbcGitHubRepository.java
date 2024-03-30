package edu.java.repository.jdbc;

import edu.java.model.GitHubRepo;
import edu.java.repository.GitHubRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcGitHubRepository implements GitHubRepository {
    private final static String FIND_BY_ID = "SELECT * FROM github_repos WHERE link_id = ?";
    private final static String ADD_REPO = "INSERT INTO github_repos(link_id, stars, issues) VALUES (?, ?, ?)";
    private final static String SAVE_CHANGES = "UPDATE github_repos SET stars = ?, issues = ? WHERE link_id = ?";
    private final static String DELETE_LINK = "DELETE FROM github_repos WHERE link_id = ?";
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<GitHubRepo> findByLinkId(long linkId) {
        return jdbcTemplate.query(FIND_BY_ID, new BeanPropertyRowMapper<>(GitHubRepo.class), linkId).stream()
            .findFirst();
    }

    @Override
    public void addRepo(GitHubRepo gitHubRepo) {
        jdbcTemplate.update(ADD_REPO, gitHubRepo.getLinkId(), gitHubRepo.getStars(), gitHubRepo.getIssues());
    }

    @Override
    public void saveChanges(GitHubRepo gitHubRepo) {
        jdbcTemplate.update(SAVE_CHANGES, gitHubRepo.getStars(), gitHubRepo.getIssues(), gitHubRepo.getLinkId());
    }

    @Override
    public boolean delete(long linkId) {
        return jdbcTemplate.update(DELETE_LINK, linkId) >= 1;
    }
}
