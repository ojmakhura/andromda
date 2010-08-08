package org.andromda.timetracker.web.security;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;
import org.acegisecurity.userdetails.UserDetails;
import org.andromda.timetracker.vo.UserDetailsVO;
import org.andromda.timetracker.vo.UserRoleVO;

/**
 *
 */
public class UserDetailsImpl implements UserDetails {

    /**
     * 
     */
    private static final long serialVersionUID = 6988882087567807592L;
    private UserDetailsVO userDetailsVO;
    private GrantedAuthority[] authorities;

    /**
     * @param userDetailsVOIn
     */
    public UserDetailsImpl(UserDetailsVO userDetailsVOIn) {
        this.userDetailsVO = userDetailsVOIn;
        this.authorities = null; // lazily initialized
    }

    /**
     * @return userDetailsVO
     */
    public UserDetailsVO getUserDetailsVO() {
        return this.userDetailsVO;
    }

    /**
     * @see org.acegisecurity.userdetails.UserDetails#getUsername()
     */
    public String getUsername() {
        return this.userDetailsVO.getUsername();
    }

    /**
     * @see org.acegisecurity.userdetails.UserDetails#getPassword()
     */
    public String getPassword() {
        return this.userDetailsVO.getPassword();
    }

    /**
     * @see org.acegisecurity.userdetails.UserDetails#isAccountNonExpired()
     */
    public boolean isAccountNonExpired() {
        return this.userDetailsVO.isIsActive();
    }

    /**
     * @see org.acegisecurity.userdetails.UserDetails#isAccountNonLocked()
     */
    public boolean isAccountNonLocked() {
        return this.userDetailsVO.isIsActive();
    }

    /**
     * @see org.acegisecurity.userdetails.UserDetails#isCredentialsNonExpired()
     */
    public boolean isCredentialsNonExpired() {
        return this.userDetailsVO.isIsActive();
    }

    /**
     * @see org.acegisecurity.userdetails.UserDetails#isEnabled()
     */
    public boolean isEnabled() {
        return this.userDetailsVO.isIsActive();
    }

    /**
     * @see org.acegisecurity.userdetails.UserDetails#getAuthorities()
     */
    public GrantedAuthority[] getAuthorities() {

        // Authorities are initialized lazily
        if (this.authorities == null)
        {
            UserRoleVO[] roles = this.userDetailsVO.getRoles();
            this.authorities = new GrantedAuthorityImpl[roles.length];
            for (int i=0; i<roles.length; i++)
            {
                this.authorities[i] = new GrantedAuthorityImpl(roles[i].toString());
            }
        }
        return this.authorities;
    }
}