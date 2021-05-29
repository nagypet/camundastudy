/*
 * Copyright 2020-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package hu.perit.orchestrator.auth;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import hu.perit.spvitamin.spring.config.SpringContext;
import hu.perit.spvitamin.spring.security.AuthenticatedUser;
import hu.perit.spvitamin.spring.security.auth.AuthorizationService;
import hu.perit.spvitamin.spring.security.auth.filter.FilterAuthenticationException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Peter Nagy
 */

@Slf4j
public class PostAuthenticationFilter extends OncePerRequestFilter
{

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException
    {

        try
        {
            AuthorizationService authorizationService = SpringContext.getBean(AuthorizationService.class);

            AuthenticatedUser authenticatedUser = authorizationService.getAuthenticatedUser();
            if (!authenticatedUser.isAnonymous())
            {
                log.debug(String.format("Authentication succeeded for user: '%s'", authenticatedUser.toString()));

                // Mapping of user roles to privileges
                AuthenticatedUser authenticatedUserWithPrivileges = AuthenticatedUser.builder() //
                    .username(authenticatedUser.getUsername()) //
                    .authorities(mapRolesToPrivileges(authenticatedUser.getAuthorities())) //
                    .build();
                log.debug(String.format("Roles mapped to privileges: '%s'", authenticatedUserWithPrivileges.toString()));

                authorizationService.setAuthenticatedUser(authenticatedUserWithPrivileges);
            }

            filterChain.doFilter(request, response);
        }
        catch (AuthenticationException ex)
        {
            SecurityContextHolder.clearContext();
            HandlerExceptionResolver resolver = SpringContext.getBean("handlerExceptionResolver", HandlerExceptionResolver.class);
            if (resolver.resolveException(request, response, null, ex) == null)
            {
                throw ex;
            }
        }
        catch (Exception ex)
        {
            SecurityContextHolder.clearContext();
            HandlerExceptionResolver resolver = SpringContext.getBean("handlerExceptionResolver", HandlerExceptionResolver.class);
            if (resolver.resolveException(request, response, null, new FilterAuthenticationException("Authentication failed!", ex)) == null)
            {
                throw ex;
            }
        }
    }


    private Collection<? extends GrantedAuthority> mapRolesToPrivileges(Collection<? extends GrantedAuthority> authorities)
    {
        Set<GrantedAuthority> permissions = new HashSet<>();

        // filtering only ROLEs
        List<? extends GrantedAuthority> roles = authorities.stream().filter(a -> a.getAuthority().startsWith("ROLE_")).collect(
            Collectors.toList());
        for (GrantedAuthority role : roles)
        {
            // Adding the role itself
            permissions.add(role);

            // Adding permissions
            if (("ROLE_" + Role.ADMIN.name()).equals(role.getAuthority()))
            {
                permissions.add(new SimpleGrantedAuthority(Permissions.BOOK_READ_ACCESS.name()));
                permissions.add(new SimpleGrantedAuthority(Permissions.BOOK_WRITE_ACCESS.name()));
                permissions.add(new SimpleGrantedAuthority(Permissions.AUTHOR_READ_ACCESS.name()));
            }

            if (("ROLE_" + Role.PUBLIC.name()).equals(role.getAuthority()))
            {
                permissions.add(new SimpleGrantedAuthority(Permissions.BOOK_READ_ACCESS.name()));
                permissions.add(new SimpleGrantedAuthority(Permissions.AUTHOR_READ_ACCESS.name()));
            }
        }

        return permissions;
    }
}
