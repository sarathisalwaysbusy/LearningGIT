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
<<<<<<< HEAD
		A.$("#submitButton").click();
=======
		//A.$("#submitButton").click();
		A.$(lolololol);
>>>>>>> 3

		A.$("[data-action='a-popover-close']").click();
		},
		error : function() {
		A.$(".loader").hide();
		event.$target.context.disabled = false;
		alert('There was error in getting the data');
		}
		});
});