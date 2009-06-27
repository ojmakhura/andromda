package org.andromda.timetracker.web.security;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;
import org.acegisecurity.userdetails.UserDetails;
import org.andromda.timetracker.vo.UserDetailsVO;
import org.andromda.timetracker.vo.UserRoleVO;

public class UserDetailsImpl implements UserDetails {

    private UserDetailsVO userDetailsVO;
    private GrantedAuthority[] authorities;

    public UserDetailsImpl(UserDetailsVO userDetailsVO) {
        this.userDetailsVO = userDetailsVO;
        this.authorities = null; // lazily initialized
    }

    public UserDetailsVO getUserDetailsVO() {
        return userDetailsVO;
    }

    public String getUsername() {
        return userDetailsVO.getUsername();
    }

    public String getPassword() {
        return userDetailsVO.getPassword();
    }

    public boolean isAccountNonExpired() {
        return userDetailsVO.isIsActive();
    }

    public boolean isAccountNonLocked() {
        return userDetailsVO.isIsActive();
    }

    public boolean isCredentialsNonExpired() {
        return userDetailsVO.isIsActive();
    }

    public boolean isEnabled() {
        return userDetailsVO.isIsActive();
    }

    public GrantedAuthority[] getAuthorities() {

        // Authorities are initialized lazily
        if (authorities == null)
        {
            UserRoleVO[] roles = userDetailsVO.getRoles();
            authorities = new GrantedAuthorityImpl[roles.length];
            for (int i=0; i<roles.length; i++)
            {
                authorities[i] = new GrantedAuthorityImpl(roles[i].toString());
            }
        }
        return authorities;
    }
}