package com.dossantosh.springfirstmicroservise.common.global.events.audit;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import com.dossantosh.springfirstmicroservise.common.security.custom.auth.models.UserContextService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    private final UserContextService userContextService;

    @Override
    public Optional<String> getCurrentAuditor() {
        try {
            return Optional.ofNullable(userContextService.getUsername());
        } catch (IllegalStateException ex) {
            return Optional.empty();
        }
    }
}

