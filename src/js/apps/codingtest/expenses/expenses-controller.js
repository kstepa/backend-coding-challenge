"use strict";

/******************************************************************************************

Expenses controller

******************************************************************************************/

var app = angular.module("expenses.controller", []);

app.controller("ctrlExpenses", ["$rootScope", "$scope", "$filter", "config", "restalchemy", function ExpensesCtrl($rootScope, $scope, $filter, $config, $restalchemy) {
	// Update the headings
	$rootScope.mainTitle = "Expenses";
	$rootScope.mainHeading = "Expenses";

	// Update the tab sections
	$rootScope.selectTabSection("expenses", 0);

	var restExpenses = $restalchemy.init({ root: $config.apiroot }).at("expenses");

	$scope.errorMsg = null;
	$scope.dateOptions = {
		changeMonth: true,
		changeYear: true,
		dateFormat: "dd/mm/yy"
	};

	var loadExpenses = function() {
		// Retrieve a list of expenses via REST
		restExpenses.get().then(function(expenses) {
			$scope.expenses = expenses;
		});
	}

	var translateEuro = function() {
		if (typeof($scope.newExpense.amount) === "undefined") return {};
		var samt = $scope.newExpense.amount.trim().toUpperCase();
		var amt = parseFloat(samt.replace(/[^\d^\.]/g, ''));
		
		return {
			"amount": amt,
			"euro": samt.endsWith("EUR")
		}
	}

	$scope.saveExpense = function() {
		$scope.errorMsg = null;
		if ($scope.expensesform.$valid) {
			var data = _.merge({}, $scope.newExpense, translateEuro());
			// Post the expense via REST
			restExpenses.post(data).then(function() {
				// Reload new expenses list
				loadExpenses();
			}).error(function(data, status) {
				$scope.errorMsg = data.err;
			});
		}
	};

	$scope.clearExpense = function() {
		$scope.newExpense = {};
	};

	
	$scope.vat = function() {
		var tra = translateEuro();
		if (isNaN(tra.amount) || tra.amount < 0) return "";
		var vat = (0.2 * tra.amount).toFixed(2);
		var sym = tra.euro ? "&euro;" : "&pound;";
		return $filter("currency")(vat, sym);
	}

	// Initialise scope variables
	loadExpenses();
	$scope.clearExpense();
}]);
