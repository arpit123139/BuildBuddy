package com.example.Lovable.security;

import com.example.Lovable.dto.auth.JwtUserPrincipal;
import com.example.Lovable.entity.User;
import com.example.Lovable.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@RequiredArgsConstructor
@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    
    private final AuthUtil authUtil;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{

            final String requestTokenHeader=request.getHeader("Authorization");
            if(requestTokenHeader==null || !requestTokenHeader.startsWith("Bearer")){
                filterChain.doFilter(request,response);
                return;
            }
            String token=requestTokenHeader.split("Bearer ")[1];

            //Benefit of using this is that we are not making any DB call as explained in the module
            //the disadvantage is may be the user can become inactive or the role has been updated
            JwtUserPrincipal user = authUtil.verifyAccessToken(token);


            if(user!=null && SecurityContextHolder.getContext().getAuthentication()==null)
            {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=
                        new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());

                //passing details like user Ip address session id etc.. about the request
                usernamePasswordAuthenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

            }
            filterChain.doFilter(request,response);

        } catch (Exception ex) {
            throw ex;
//            handlerExceptionResolver.resolveException(request,response, null,ex);
        }
    }
}
