/**
 * Created by kalaarentz on 4/30/17.
 */
var postings = angular.module('Docstar.Profile', ['ngRoute', 'ngResource', 'ui.bootstrap', 'Session']);

postings.config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/profile', {
        templateUrl: 'resources/views/shared/profile/profile.html',
        controller: 'Docstar.Profile.Controller'
    });
}]);

postings.controller('Docstar.Profile.Controller', ['$scope', '$resource', '$uibModal', 'Session.Service', function($scope, $resource, $modal, Session) {
    $scope.user = Session.user();
}]);