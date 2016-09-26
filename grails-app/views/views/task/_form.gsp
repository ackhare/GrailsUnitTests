<%@ page import="com.Task" %>



<div class="fieldcontain ${hasErrors(bean: taskInstance, field: 'description', 'error')} ">
	<label for="description">
		<g:message code="task.description.label" default="Description" />
		
	</label>
	<g:textField name="description" value="${taskInstance?.description}"/>
</div>

