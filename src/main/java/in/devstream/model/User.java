package in.devstream.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "users", indexes = {
    @Index(unique = true, columnList = "email")})
public class User implements UserDetails {

    @Id
    private String id;

    private String email;

    private String password;

    private String fullname;

    private boolean enabled;

    // https://stackoverflow.com/questions/42334475/spring-jpa-users-roles-authentication-how-do-i-avoid-duplicate-role-entries
    @OneToMany(mappedBy = "role", cascade = CascadeType.DETACH, orphanRemoval = false)
    private Set<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authoritiesSet = new HashSet<>();

        for (Role role : this.roles) {
            authoritiesSet.add(new SimpleGrantedAuthority(role.getRole()));
        }

        List<GrantedAuthority> authoritiesList = new ArrayList<>(authoritiesSet);
        return authoritiesList;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
