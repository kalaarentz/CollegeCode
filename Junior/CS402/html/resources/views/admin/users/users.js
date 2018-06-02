/**
 * Created by kalaarentz on 5/1/17.
 */
users = angular.module('Docstar.Users', ['ngRoute', 'ngResource', 'Session']);

users.config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/admin/users', {
        templateUrl: 'resources/views/admin/users/users.html',
        controller: 'Docstar.Users.Controller'
    });
}]);

users.controller('Docstar.Users.Controller', ['$scope', '$resource', 'Session.Service', function($scope, $resource, Session) {
    var Users = $resource('api/v1/admin/users/:uid', {});
    $scope.users = Users.query( );

    $scope.hasRole = function( user, role ) {
        for( var i = 0; i < user.roles.length; i++) {
            if( user.roles[i].name === role ) return true;
        }
        return false;
    }

}]);
