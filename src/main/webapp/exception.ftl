<html lang="en">
<#include "base.ftl">
<#macro title>Exception Details</#macro>

<#macro content>
    <h2>Exception Details: </h2><br>
    <strong>Status code:</strong>${statusCode}<br>
    <strong>URI:</strong>${uri}<br>
    <#if message??><strong>Message:</strong>${message}<br></#if>
</#macro>

</html>