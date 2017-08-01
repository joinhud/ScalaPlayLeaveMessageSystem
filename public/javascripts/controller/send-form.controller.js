'use strict';

angular
    .module('leaveMessageSystem',[])
    .controller('SendMessageFormController', ['$scope', '$http', function ($scope, $http) {
        $scope.send = function(msgForm) {
            if (msgForm.$valid) {
                $http.put('/messages', JSON.stringify($scope.message)).then(
                    function successCallback(response) {
                        $scope.result = response.data;
                        $scope.message = {};
                        msgForm.$setPristine();
                        msgForm.$setUntouched();
                    },
                    function errorCallback(response) {
                        if (response.status !== 422) {
                            $scope.result = response.data;
                        } else {
                            response.data.forEach(function (error) {
                                if (error.pointer === 'userName') {
                                    if (error.constraint === 'required') {
                                        msgForm.uName.$error.required = true;
                                    } else {
                                        msgForm.uName.$error.pattern = true;
                                    }
                                } else {
                                    msgForm.msgText.$error.required = true;
                                }
                            });
                        }
                    }
                );
            }
        };
    }]);