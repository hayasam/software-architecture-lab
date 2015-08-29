var sharedServices = angular.module('sharedServices', ['ui.bootstrap']);

sharedServices.service("AppService", function($rootScope, $location, $http){
	
	var genres;
	var systemRoles;
	var pageSize = 5;
	
	this.init = function() {
		$http.get('/app_data').success(function(data, status, headers, config) {
			if (data.genres) {
				genres = data.genres;
			} else {
				console.log("No Genres data.");
			}
			if (data.systemRoles) {
				systemRoles = data.systemRoles;
			} else {
				console.log("No SystemRoles data.");
			}
		}).error(function(data, status, headers, config) {
			NotificationService.statusBarError(data.message);
			console.log("Data initialization failed");
		});
		
	};
	
	this.getGenres = function() {
		return genres;
	};
	
	this.getSystemRoles = function() {
		return systemRoles;
	};
	
	this.getPageSize = function() {
		return pageSize;
	};
 
});


sharedServices.service('NotificationService', ['$modal', '$rootScope', '$location',

    function ($modal, $rootScope, $location) {
        var modalDefaults = {
        		backdrop: true,
        		keyboard: true,
        		modalFade: true,
        		templateUrl: 'modal'
        };

        var modalOptions = {
        		closeButtonText: 'Close',
//        		actionButtonText: 'OK',
        		headerText: 'MyBooks',
        		bodyText: 'Perform this action?'
        };

      this.showModal = function (customModalDefaults, customModalOptions) {
          if (!customModalDefaults) customModalDefaults = {};
          customModalDefaults.backdrop = 'static';
          return this.show(customModalDefaults, customModalOptions);
      };

      this.show = function (customModalDefaults, customModalOptions) {
          //Create temp objects to work with since we're in a singleton service
          var tempModalDefaults = {};
          var tempModalOptions = {};

          //Map angular-ui modal custom defaults to modal defaults defined in service
          angular.extend(tempModalDefaults, modalDefaults, customModalDefaults);

          //Map modal.html $scope custom properties to defaults defined in service
          angular.extend(tempModalOptions, modalOptions, customModalOptions);

          if (!tempModalDefaults.controller) {
              tempModalDefaults.controller = function ($scope, $modalInstance) {
                  $scope.modalOptions = tempModalOptions;
                  $scope.modalOptions.ok = function (result) {
                      $modalInstance.close(result);
                  };
                  $scope.modalOptions.close = function (result) {
                      $modalInstance.dismiss('cancel');
                  };
              }
          }

          return $modal.open(tempModalDefaults).result;
      };
      
      
      this.dialogBoxInfo = function (message) {
          var customModalOptions = {
          		closeButtonText: 'Close',
          		bodyText: message
          };
    	  
          return this.showModal({}, customModalOptions);
      };
                  
	  	$rootScope.$on("$routeChangeStart", function (event, next, current) {
			if ($rootScope.messageSuccessOnNextPage) {
				$rootScope.messageSuccessOnNextPage = null;
			} else {
				$rootScope.messageSuccess = null;
			}
			if ($rootScope.messageErrorOnNextPage) {
				$rootScope.messageErrorOnNextPage = null;
			} else {
				$rootScope.messageError = null;
			}
		});

	  	this.statusBarSuccess = function (message) {
	  		$rootScope.messageSuccess = message;
	  	}
	  	
	  	this.statusBarSuccessNextPage = function (message) {
	  		$rootScope.messageSuccessOnNextPage = true;
            $rootScope.messageSuccess = message;
	  	}
	  	
	  	this.statusBarError = function (message) {
	  		$rootScope.messageError = message;
	  	}
	  	
  }]);
          
          
