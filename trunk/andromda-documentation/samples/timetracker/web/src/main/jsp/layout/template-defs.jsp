<tiles:definition
  id="default_layout_definition"
  page="/layout/default-layout.jsp"
  scope="request">
    <tiles:put name="style" type="string" />
    <tiles:put name="javascript" type="string" />
    <tiles:put name="body_includes" type="string" />
    <tiles:put name="topmenu" value="/layout/topmenu.jsp" />
    <tiles:put name="header" value="/layout/header.jsp" />
    <tiles:put name="menubar" value="/layout/menubar.jsp" />
</tiles:definition>

<tiles:definition
  id="login_layout_definition"
  page="/layout/login-layout.jsp"
  scope="request">
    <tiles:put name="header" value="/layout/header.jsp" />
</tiles:definition>