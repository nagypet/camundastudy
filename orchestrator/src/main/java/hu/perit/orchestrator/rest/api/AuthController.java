package hu.perit.orchestrator.rest.api;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RestController;

import hu.perit.spvitamin.core.took.Took;
import hu.perit.spvitamin.spring.auth.AuthorizationToken;
import hu.perit.spvitamin.spring.logging.AbstractInterfaceLogger;
import hu.perit.spvitamin.spring.rest.api.AuthApi;
import hu.perit.spvitamin.spring.security.AuthenticatedUser;
import hu.perit.spvitamin.spring.security.auth.AuthorizationService;
import hu.perit.spvitamin.spring.security.auth.jwt.JwtTokenProvider;
import hu.perit.spvitamin.spring.security.auth.jwt.TokenClaims;

@RestController
public class AuthController extends AbstractInterfaceLogger implements AuthApi
{

    private final JwtTokenProvider tokenProvider;
    private final AuthorizationService authorizationService;

    protected AuthController(JwtTokenProvider tokenProvider, AuthorizationService authorizationService, HttpServletRequest httpRequest)
    {
        super(httpRequest);
        this.authorizationService = authorizationService;
        this.tokenProvider = tokenProvider;
    }


    @Override
    public AuthorizationToken authenticateUsingGET(String processID)
    {
        AuthenticatedUser authenticatedUser = this.authorizationService.getAuthenticatedUser();
        this.traceIn(processID, authenticatedUser.getUsername(), this.getMyMethodName(), 1);

        try (Took took = new Took(processID))
        {
            AuthorizationToken token = tokenProvider.generateToken(authenticatedUser.getUsername(),
                new TokenClaims(authenticatedUser.getUserId(), authenticatedUser.getAuthorities()));
            this.traceOut(processID, authenticatedUser.getUsername(), this.getMyMethodName(), 1);
            return token;
        }
        catch (Throwable ex)
        {
            this.traceOut(processID, authenticatedUser.getUsername(), this.getMyMethodName(), 1, ex);
            throw ex;
        }
    }


    @Override
    protected String getSubsystemName()
    {
        return "auth-controller";
    }

}
