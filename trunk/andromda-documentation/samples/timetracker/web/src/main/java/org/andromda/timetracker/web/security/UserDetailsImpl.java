package org.andromda.timetracker.web.security;


import java.util.ArrayList;
import java.util.List;
import org.andromda.timetracker.vo.UserDetailsVO;
import org.andromda.timetracker.vo.UserRoleVO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 */
public class UserDetailsImpl implements UserDetails
{
    /**
     * 
     */
    private static final long serialVersionUID = 6988882087567807592L;
    private UserDetailsVO userDetailsVO;
    private List<GrantedAuthority> authorities;

    /**
     * @param userDetailsVOIn
     */
    public UserDetailsImpl(UserDetailsVO userDetailsVOIn)
    {
        this.userDetailsVO = userDetailsVOIn;
        this.authorities = null; // lazily initialized
    }

    /**
     * @return userDetailsVO
     */
    public UserDetailsVO getUserDetailsVO()
    {
        return this.userDetailsVO;
    }

    /**
     * @see org.springframework.security.core.userdetails.UserDetails#getUsername()
     */
    public String getUsername()
    {
        return this.userDetailsVO.getUsername();
    }

    /**
     * @see org.springframework.security.core.userdetails.UserDetails#getPassword()
     */
    public String getPassword()
    {
        return this.userDetailsVO.getPassword();
    }

    /**
     * @see org.springframework.security.core.userdetails.UserDetails#isAccountNonExpired()
     */
    public boolean isAccountNonExpired()
    {
        return this.userDetailsVO.isIsActive();
    }

    /**
     * @see org.springframework.security.core.userdetails.UserDetails#isAccountNonLocked()
     */
    public boolean isAccountNonLocked()
    {
        return this.userDetailsVO.isIsActive();
    }

    /**
     * @see org.springframework.security.core.userdetails.UserDetails#isCredentialsNonExpired()
     */
    public boolean isCredentialsNonExpired()
    {
        return this.userDetailsVO.isIsActive();
    }

    /**
     * @see org.springframework.security.core.userdetails.UserDetails#isEnabled()
     */
    public boolean isEnabled()
    {
        return this.userDetailsVO.isIsActive();
    }

    /**
     * @see org.springframework.security.core.userdetails.UserDetails#getAuthorities()
     */
    public List<GrantedAuthority> getAuthorities()
    {
        // Authorities are initialized lazily
        if (this.authorities == null)
        {
            //List<UserRoleVO> roles = this.userDetailsVO.getRoles();
            UserRoleVO[] roles = this.userDetailsVO.getRoles();
            //this.authorities = new GrantedAuthorityImpl[roles.length];
            this.authorities = new ArrayList<GrantedAuthority>(roles.length);
            //for (UserRoleVO role : roles)
            for (int i=0; i<roles.length; i++)
            {
                //this.authorities[i] = new GrantedAuthorityImpl(roles[i].toString());
                //this.authorities.add(new GrantedAuthorityImpl(role.toString()));
                this.authorities.add(new GrantedAuthorityImpl(roles[i].toString()));
            }
        }
        return this.authorities;
    }
}