package easycbt2.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import easycbt2.model.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, String> {
}