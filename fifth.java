<%@page import="java.util.HashMap"%>
<%@page contentType="text/html" pageEncoding="UTF-8" session="false"%>
<%@taglib prefix="m"
		uri="http://schema.amazon.com/xmlns/matrix/nested-components-1.0"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="a" uri="aui"%>
<jsp:useBean id="cons" class="com.amazon.simplestack.cashmanageruiapp.webapp.constants.Constants" scope="page"/>

<a:gridRow>
<a:gridColumn gridUnits="3" textAlign="${'right'}">
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
<a:gridColumn gridUnits="3" textAlign="${'right'}">
		Date
</a:gridColumn>
<a:gridColumn gridUnits="3" position="${'last'}">
<a:calendar id="calendarInput" />
</a:gridColumn>
</a:gridRow>
<a:gridRow spacingTop="${'base'}">
<a:gridColumn gridUnits="6" textAlign="${'center'}">
<a:button value="Submit" htmlButtonType="${'button'}" id="submitButton">Submit to Bank</a:button>
</a:gridColumn>
<a:gridColumn gridUnits="6" position="${'last'}"
		textAlign="${'center'}">
<a:button id="historyButton" value="History" htmlButtonType="${'button'}">History</a:button>
</a:gridColumn>
</a:gridRow>

<a:gridRow spacingTop="${'base'}">
<div class="dataTables_wrapper" id="tableListContainer"></div>
</a:gridRow>

<script>
	P.when('A').execute('aui-ready', function(A) {
			A.$("#submitButton").click(function() {
			P.when('a-dropdown').execute(function(dropdown) {

			var stationID = dropdown.getSelect("stationSelect").val();
			if(isEmpty(stationID)){
			alert("Please select a station");
			return;
			}

			//A.$("#tableListContainer").html("");
			A.$("#tableListContainer").empty();
			showLoader(A);

			A.ajax("${siteconfig.reconciliationSubmitToBankTable}", {
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

			A.$("#historyButton").click(function() {
			P.when('a-dropdown').execute(function(dropdown) {

			var stationID = dropdown.getSelect("stationSelect").val();
			if(isEmpty(stationID)){
			alert("Please select a station");
			return;
			}

			var calendarInput = getCalendarInputString(A, "calendarInput");
			if(calendarInput == null){
			alert("Please enter a valid date");
			return;
			}

			var dateDifference = getDateDifferenceFromToday(A, "calendarInput");
			if(dateDifference < 0 || dateDifference > ${siteconfig.maxDaysDifference}){
		alert("The date cannot be earlier than ${siteconfig.maxDaysDifference} days or in the future");
		return;
		}

		A.ajax("${siteconfig.reconciliationHistoryTable}", {
		method : 'get',
		params : {
		stationId : stationID,
		paymentDate : calendarInput
		},
		success : function(data) {
		A.$("#tableListContainer").html(data);
		},
		error : function() {
		alert('There was error in getting the data');
		}
		});
		});
		});


		A.declarative('reconciliation-submittobank-submit', 'click', function(event){

		var cashSubmitDate = event.data.cashSubmitDate;
		var depositNumber = A.$("#modalDepositNumber_" + cashSubmitDate).val();
		var depositNumberConfirm = A.$("#modalDepositNumberConfirm_" + cashSubmitDate).val();
		if(isEmpty(depositNumber)){
		alert("Please enter the deposit slip number");
		return;
		}
		if(depositNumber != depositNumberConfirm){
		alert("The entered deposit numbers do not match");
		return;
		}

		//Things to do before making a server request
		event.$target.context.disabled = true;
		A.$(".loader").show();

		A.ajax("${siteconfig.submitReconciliationFormData}", {
		method : 'get',
		params : {
		stationId : A.$("#modalStationId_" + cashSubmitDate).val(),
		depositAmountUnit : A.$("#modalDepositAmountUnit_" + cashSubmitDate).val(),
		depositAmountValue : A.$("#modalDepositAmountValue_" + cashSubmitDate).val(),
		depositSlipNumber : A.$("#modalDepositNumber_" + cashSubmitDate).val(),
		shipmentCount : A.$("#modalShipmentCount_" + cashSubmitDate).val(),
		depositerLogin: A.$("#modalDepositerLogin_" + cashSubmitDate).val(),
		mposDepositAmountUnit : A.$("#modalMPOSDepositAmountUnit_" + cashSubmitDate).val(),
		mposDepositAmountValue : A.$("#modalMPOSDepositAmountValue_" + cashSubmitDate).val(),
		mposShipmentCount : A.$("#modalMPOSShipmentCount_" + cashSubmitDate).val(),
		cashSubmitDate : A.$("#modalCashSubmitDate_" + cashSubmitDate).val()
		},
		success : function(data) {
		A.$(".loader").hide();
		alert("Data saved!");
		A.$("#submitButton").click();

		//A.$("[data-action='a-popover-close']").click();
		A.$("#modalNoButton").click();
		},
		error : function() {
		A.$(".loader").hide();
		event.$target.context.disabled = false;
		alert('There was error in getting the data');
		}
		});
		});

		A.declarative('reconciliation-submittobank-download-excel', 'click', function(event){
		var data = event.data;
		document.location.href = "${siteconfig.reconciliationSubmitToBankDownloadExcel}" +
		"?stationId=" + encodeURIComponent(data.stationId) +
		"&stationName=" + encodeURIComponent(data.stationName) +
		"&cashSubmitDate=" + encodeURIComponent(data.cashSubmitDate);
		});

		A.declarative('reconciliation-history-download-excel', 'click', function(event){
		var data = event.data;
		document.location.href = "${siteconfig.reconciliationHistoryDownloadExcel}" +
		"?depositId=" + encodeURIComponent(data.depositId) +
		"&stationName=" + encodeURIComponent(data.stationName) +
		"&depositSlipNumber=" + encodeURIComponent(data.depositSlipNumber);
		});
		});
</script>