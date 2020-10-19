package academy.devdojo.youtube.course.security.config;

import academy.devdojo.youtube.core.property.JwtConfiguration;
import academy.devdojo.youtube.security.config.SecurityTokenConfig;
import academy.devdojo.youtube.security.filter.JWTTokenAuthorizationFilter;
import academy.devdojo.youtube.security.token.converter.TokenConverter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityCredencialsConfig extends SecurityTokenConfig {
    private final TokenConverter tokenConverter;

    public SecurityCredencialsConfig(JwtConfiguration jwtConfiguration, TokenConverter tokenConverter){
        super(jwtConfiguration);
        this.tokenConverter = tokenConverter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterAfter(new JWTTokenAuthorizationFilter(jwtConfiguration, tokenConverter), UsernamePasswordAuthenticationFilter.class);
        super.configure(http);
    }


}
