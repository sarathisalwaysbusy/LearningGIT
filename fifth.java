<%@page import="java.util.HashMap"%>
<%@page contentType="text/html" pageEncoding="UTF-8" session="false"%>
<%@taglib prefix="m"
		uri="http://schema.amazon.com/xmlns/matrix/nested-components-1.0"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="a" uri="aui"%>
<jsp:useBean id="cons" class="com.amazon.simplestack.cashmanageruiapp.webapp.constants.Constants" scope="page"/>

<a:gridRow>
<a:gridColumn gridUnits="1" textAlign="${'right'}">
		Station
</a:gridColumn>
<a:gridColumn gridUnits="3">
<a:dropdown nativeId="stationSelect" name="stationSelect"
		optionPrompt="Select a station">
<c:forEach items="${stationsList}" var="station">
<c:if test="${station.getName() ne cons.UNASSIGNED_STATION}">
<a:dropdownOption value="${station.getId()}">${station.getName()}</a:dropdownOption>
</c:if>
</c:forEach>
</a:dropdown>
</a:gridColumn>

<a:gridColumn gridUnits="1" textAlign="${'right'}">
		Select Date
</a:gridColumn>

<a:gridColumn gridUnits="3">
<a:calendar id="calendarStartInput" />
</a:gridColumn>

<a:gridColumn gridUnits="3">
<a:calendar id="calendarEndInput" />
</a:gridColumn>

</a:gridRow>
<a:gridRow spacingTop="${'base'}">
<a:gridColumn gridUnits="3" textAlign="${'right'}">
<a:button value="Submit" htmlButtonType="${'button'}" id="getDetailsButton">Get Deposit Details</a:button>
</a:gridColumn>
</a:gridRow>

<a:gridRow spacingTop="${'base'}">
<div class="dataTables_wrapper" id="tableListContainer"></div>
</a:gridRow>

<script>
	P.when('A').execute('aui-ready', function(A) {
			A.$("#getDetailsButton").click(function() {
			P.when('a-dropdown').execute(function(dropdown) {

			var stationID = dropdown.getSelect("stationSelect").val();
			if(isEmpty(stationID)){
			alert("Please select a station");
			return;
			}
			var calendarStartInput = getCalendarInputString(A, "calendarStartInput");
			var calendarEndInput = getCalendarInputString(A, "calendarEndInput");
			if(calendarStartInput == null || calendarEndInput == null){
			alert("Please enter a valid start and end dates");
			return;
			}
			//TODO: check for <=present date

			var dateDifference = getDateDifferenceBetweenTwoCalendarIds(A, "calendarStartInput", "calendarEndInput");
			showLoader(A);
			A.ajax("${siteconfig.reconciliationUpdateTable}", {
			method : 'get',
			params : {
			stationId : stationID
			},
			success : function(data) {
			hideLoader(A);
			A.$("#tableListContainer").html(data);
			},
			error : function() {
			hideLoader(A);
			alert('There was error in getting the data');
			}
			});
			});
			});
			});
</script>