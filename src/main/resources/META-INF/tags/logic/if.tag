<%--

    This file is a component of thundr, a software library from 3wks.
    Read more: http://3wks.github.io/thundr/
    Copyright (C) 2014 3wks, <thundr@3wks.com.au>

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

            http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

--%>
<%@ tag trimDirectiveWhitespaces="true"%>
<%@ tag import="java.util.List" %>
<%@ tag import="java.util.ArrayList" %>
<%@ tag import="java.util.UUID" %>
<%@ tag import="java.util.LinkedHashMap" %>
<%@ attribute name="condition" required="true" %>
<%@ attribute name="suppressVersion" required="false" type="java.lang.Boolean" description="If set to true, scripts will not append the application version as a request parameter. This is useful to keep resources inline with application deployments" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- 
Some nasty scriplet love lets us have nicer if/else if/else blocks. Just close your eyes and act like nothing is wrong...

We maintain a stack of booleans shared across the if/else/elseif tags. 
This allows nested if statements to function, and gives access to the evaluation conditions
to the other tags. 
--%>
<c:set var="evaluated" value="${false}"/>
<c:if test="${condition}"><c:set var="evaluated" value="${true}"/></c:if>
<% 
	List<Boolean> logicStack = (List<Boolean>)getJspContext().getAttribute("logicTagStack", PageContext.REQUEST_SCOPE);
	if(logicStack == null){
		logicStack = new ArrayList<Boolean>();
		getJspContext().setAttribute("logicTagStack", logicStack, PageContext.REQUEST_SCOPE);
	}
	boolean evaluated = Boolean.TRUE.equals(getJspContext().getAttribute("evaluated"));
	logicStack.add(evaluated);
	int currentSize = logicStack.size();
	if(evaluated){ 
%>
<jsp:doBody/>
<% 
	}
	while(logicStack.size() > currentSize){
		logicStack.remove(currentSize);
	}
%>