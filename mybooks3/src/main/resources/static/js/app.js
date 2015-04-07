var app = angular.module('hello', [ 'ngRoute', 'ngResource' ]);



app.config(function($routeProvider, $httpProvider) {
			$routeProvider.when('/', {
				templateUrl : URLS.home,
				controller : 'home'
			}).when('/login', {
				templateUrl : URLS.login,
				controller : 'navigation'
			}).when('/books', {
				templateUrl : URLS.partialsList,
				controller : 'BookController'
			}).when('/edit/:bookId', {
				templateUrl : URLS.partialsEdit,
				controller : 'BookEditController'
			}).when('/create', {
				templateUrl : URLS.partialsCreate,
				controller : 'BookController'
			}).otherwise('/');

			$httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
		});





app.controller('navigation', function($rootScope, $scope, $http, $location, $route) {

	$scope.tab = function(route) {
		return $route.current && route === $route.current.controller;
	};

	var authenticate = function(credentials, callback) {

		var headers = credentials ? {
			authorization : "Basic "
					+ btoa(credentials.username + ":" + credentials.password)
		} : {};

		$http.get('user', {
			headers : headers
		}).success(function(data) {
			if (data.name) {
				$rootScope.authenticated = true;
			} else {
				$rootScope.authenticated = false;
			}
			callback && callback($rootScope.authenticated);
		}).error(function() {
			$rootScope.authenticated = false;
			callback && callback(false);
		});

	}

	authenticate();

	$scope.credentials = {};
	$scope.login = function() {
		authenticate($scope.credentials, function(authenticated) {
			if (authenticated) {
				console.log("Login succeeded")
				$location.path("/");
				$scope.error = false;
				$rootScope.authenticated = true;
			} else {
				console.log("Login failed")
				$location.path("/login");
				$scope.error = true;
				$rootScope.authenticated = false;
			}
		})
	};

	$scope.logout = function() {
		$http.post('logout', {}).success(function() {
			$rootScope.authenticated = false;
			$location.path("/");
		}).error(function(data) {
			console.log("Logout failed")
			$rootScope.authenticated = false;
		});
	}

});


app.controller('home', function($scope, $http) {
	$http.get('/resource/').success(function(data) {
		$scope.greeting = data;
	})
});



app.factory("BookFactory", function ($resource) {
    return $resource(URLS.books, {id: "@id"}, {
        update: {
            method: 'PUT'
        }
    });
});


app.controller("BookController", function ($scope, BookFactory, $location) {
	$scope.genres=["Comedy", "Drama", "Epic"];
	
	function init() {
        $scope.getBooks();
    }


    $scope.getBooks = function () {
        $scope.books = BookFactory.query();
    };

    $scope.deleteBook = function (book) {
        return book.$delete({}, function () {
            $scope.books.splice($scope.books.indexOf(book), 1);
            alert("Successfully deleted.");
        });
    };

    $scope.createBook = function () {
		if($scope.book == null || $scope.book.title == null || $scope.book.author == null || $scope.book.url == null || $scope.book.genre == null){
			alert("Insufficient Data! Please provide valid values.");
		} else {
	        var book = new BookFactory($scope.book);
	        book.$save({}, function() {
	            $location.path("/books");
	            alert("Successfully created.");
	        });
		}    	
    };

    init();
});

app.controller("BookEditController", function ($scope, BookFactory, $location, $routeParams) {
    function init() {
    	$scope.book = BookFactory.get({id:$routeParams.bookId})
    }

    $scope.updateBook = function() {
       var book = new BookFactory($scope.book);
       book.$update().then(function() {
    	   $location.path("/books");
    	   alert("Successfully updated.");
       }) ;
    }
    init();
});


//app Directive for confirm dialog box
app.directive('ngConfirmClick', [
	function(){
         return {
             link: function (scope, element, attr) {
                 var msg = attr.ngConfirmClick || "Are you sure?";
                 var clickAction = attr.confirmedClick;
                 element.bind('click',function (event) {
                     if ( window.confirm(msg) ) {
                         scope.$eval(clickAction);
                     }
                 });
             }
         };
 }]);
