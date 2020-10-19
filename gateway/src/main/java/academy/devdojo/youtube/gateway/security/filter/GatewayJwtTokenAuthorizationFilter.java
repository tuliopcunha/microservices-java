package academy.devdojo.youtube.gateway.security.filter;

import academy.devdojo.youtube.core.property.JwtConfiguration;
import academy.devdojo.youtube.security.filter.JWTTokenAuthorizationFilter;
import academy.devdojo.youtube.security.token.converter.TokenConverter;
import academy.devdojo.youtube.security.util.SecurityContextUtil;
import com.netflix.zuul.context.RequestContext;
import com.nimbusds.jwt.SignedJWT;
import lombok.SneakyThrows;
import org.apache.commons.lang.StringUtils;
import org.springframework.lang.NonNull;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class GatewayJwtTokenAuthorizationFilter extends JWTTokenAuthorizationFilter {


    public GatewayJwtTokenAuthorizationFilter(JwtConfiguration jwtConfiguration, TokenConverter tokenConverter) {
        super(jwtConfiguration, tokenConverter);
    }

    @Override
    @SneakyThrows
    @SuppressWarnings("Duplicates")
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(jwtConfiguration.getHeader().getName());
        if(Objects.isNull(header) || !header.startsWith(jwtConfiguration.getHeader().getPrefix())){
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.replace(jwtConfiguration.getHeader().getPrefix(), "").trim();

        String signedToken = tokenConverter.decryptToken(token);
        tokenConverter.validadeSignature(signedToken);

        SecurityContextUtil.setSecurityContext(SignedJWT.parse(signedToken));

        if (jwtConfiguration.getType().equalsIgnoreCase("signed")){
            RequestContext.getCurrentContext().addZuulRequestHeader("Authorization", jwtConfiguration.getHeader().getPrefix() + signedToken);
        }

        filterChain.doFilter(request, response);

    }
}
