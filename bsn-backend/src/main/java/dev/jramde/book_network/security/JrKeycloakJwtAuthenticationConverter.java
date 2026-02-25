package dev.jramde.book_network.security;

import org.hibernate.sql.ast.tree.expression.Collation;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JrKeycloakJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    /**
     * @param jwtSource
     * @return
     */
    @Nullable
    @Override
    public AbstractAuthenticationToken convert(Jwt jwtSource) {
        return new JwtAuthenticationToken(
                jwtSource,
                Stream.concat(new JwtGrantedAuthoritiesConverter()
                                .convert(jwtSource).stream(),
                        extractResourceRoles(jwtSource).stream()
                ).collect(Collectors.toSet())
        );
    }

    /**
     * This method converts the keycloak roles to spring security roles.
     * @param jwt : Jwt object where we extract token resources.
     * @return set of roles.
     */
    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {
        var resourceAccess = new HashMap<>(jwt.getClaim("resource_access"));
        var eternal = (Map<String, List<String>>) resourceAccess.get("account");
        var roles = eternal.get("roles");

        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.replace("-", "_")))
                .collect(Collectors.toSet());
    }
}
