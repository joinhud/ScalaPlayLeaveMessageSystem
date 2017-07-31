'use strict';

angular
    .module('leaveMessageSystem',[])
    .controller('SendMessageFormController', ['$scope', '$http', function ($scope, $http) {
        $scope.send = function(msgForm) {
            if (msgForm.$valid) {
                $http.put('/messages', JSON.stringify($scope.message)).then(
                    function successCallback(response) {
                        /*TODO: success logic*/
                        $scope.result = response.data;
                    },
                    function errorCallback(response) {
                        $scope.result = response.data;
                    }
                );
            }
        };
    }]);