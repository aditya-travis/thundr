<%--

    This file is a component of thundr, a software library from 3wks.
    Read more: http://www.3wks.com.au/thundr
    Copyright (C) 2013 3wks, <thundr@3wks.com.au>

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
<%@ tag body-content="empty" trimDirectiveWhitespaces="true" description=""%>
<%@ attribute name="gson" required="true" type="com.google.gson.Gson" description="A Gson instance to perform the JSON conversion with" %>
<%@ attribute name="value" required="true" type="java.lang.Object" description="Object to convert to JSON." %>
<%@ taglib prefix="t" uri="http://threewks.com/thundr/tags" %>
<%
	out.print(gson.toJson(value));
%>