package academy.devdojo.youtube.security.filter;

import academy.devdojo.youtube.core.property.JwtConfiguration;
import academy.devdojo.youtube.security.token.converter.TokenConverter;
import academy.devdojo.youtube.security.util.SecurityContextUtil;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JWTTokenAuthorizationFilter extends OncePerRequestFilter {
    protected final JwtConfiguration jwtConfiguration;
    protected final TokenConverter tokenConverter;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(jwtConfiguration.getHeader().getName());
        if(Objects.isNull(header) || !header.startsWith(jwtConfiguration.getHeader().getPrefix())){
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.replace(jwtConfiguration.getHeader().getPrefix(), "").trim();

        SecurityContextUtil.setSecurityContext(StringUtils.equalsIgnoreCase("signed", jwtConfiguration.getType()) ? validate(token) : decryptValidating(token));

        filterChain.doFilter(request, response);

    }

    @SneakyThrows
    private SignedJWT decryptValidating(String encryptedToken){
        String decryptToken = tokenConverter.decryptToken(encryptedToken);
        tokenConverter.validadeSignature(decryptToken);
        return SignedJWT.parse(decryptToken);
    }

    @SneakyThrows
    private SignedJWT validate(String signedToken){
        tokenConverter.validadeSignature(signedToken);
        return SignedJWT.parse(signedToken);
    }
}
