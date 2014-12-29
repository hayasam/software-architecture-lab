package org.sm.lab.mybooks2.security.permission;

import org.sm.lab.mybooks2.security.AppPermission;
import org.sm.lab.mybooks2.security.UserDetailsImpl;
import org.springframework.security.core.Authentication;

public class ProfileOwnerPermission extends AppPermission {
	
	@Override
    public boolean isAllowed(Authentication authentication, Object targetDomainObject) {
		boolean hasPermission = false;
		if (isAuthenticated(authentication) && isValidId(targetDomainObject)) {
        	String readerId = (String) targetDomainObject;
        	
        	UserDetailsImpl userDetails = getUserDetails(authentication);
            if ((readerId.equals(userDetails.getId())) || userDetails.isAdmin()) {
                hasPermission = true;
            }
        }
        
        return hasPermission;
    }

}