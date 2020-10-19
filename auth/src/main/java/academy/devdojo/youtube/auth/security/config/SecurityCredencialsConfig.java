package academy.devdojo.youtube.auth.security.config;

import academy.devdojo.youtube.auth.security.filter.JwtUsernameAndPasswordAuthenticationFilter;
import academy.devdojo.youtube.core.property.JwtConfiguration;
import academy.devdojo.youtube.security.config.SecurityTokenConfig;
import academy.devdojo.youtube.security.filter.JWTTokenAuthorizationFilter;
import academy.devdojo.youtube.security.token.converter.TokenConverter;
import academy.devdojo.youtube.security.token.creator.TokenCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityCredencialsConfig extends SecurityTokenConfig {
    private final UserDetailsService userDetailsService;
    private final TokenCreator tokenCreator;
    private final TokenConverter tokenConverter;

    public SecurityCredencialsConfig(JwtConfiguration jwtConfiguration, @Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService, TokenCreator tokenCreator, TokenConverter tokenConverter){
        super(jwtConfiguration);
        this.tokenCreator = tokenCreator;
        this.userDetailsService = userDetailsService;
        this.tokenConverter = tokenConverter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtConfiguration, tokenCreator))
                .addFilterAfter(new JWTTokenAuthorizationFilter(jwtConfiguration, tokenConverter), UsernamePasswordAuthenticationFilter.class);
        super.configure(http);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {return new BCryptPasswordEncoder();}
}
