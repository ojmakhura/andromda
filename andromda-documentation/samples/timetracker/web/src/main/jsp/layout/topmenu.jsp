<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<a class="menu" href="/timetracker/j_security_logout">
    Log out [<security:authentication operation="username"/>]
</a>