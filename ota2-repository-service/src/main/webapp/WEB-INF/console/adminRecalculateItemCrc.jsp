<%--

    Copyright (C) 2014 OpenTravel Alliance (info@opentravel.org)

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
<form id="confirmForm" action="${pageContext.request.contextPath}/console/adminRecalculateItemCrc.html" method="POST">
	<span class="confirmMessage">Recalculate the CRC for repository item "${item.filename}".  Are you sure?</span>
	<p><br>
	<input name="baseNamespace" type="hidden" value="${item.baseNamespace}" />
	<input name="filename" type="hidden" value="${item.filename}" />
	<input name="version" type="hidden" value="${item.version}" />
	<input name="confirmRecalculate" type="hidden" value="true" />
	<input type="submit" value="Recalculate Item CRC" class="formButton" />
</form>
