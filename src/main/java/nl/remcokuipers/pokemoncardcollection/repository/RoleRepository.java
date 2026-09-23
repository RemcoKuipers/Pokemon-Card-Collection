package nl.remcokuipers.pokemoncardcollection.repository;

import nl.remcokuipers.pokemoncardcollection.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
