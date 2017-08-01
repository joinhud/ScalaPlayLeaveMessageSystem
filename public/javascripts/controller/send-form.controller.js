'use strict';

angular
    .module('leaveMessageSystem')
    .controller('SendMessageFormController', ['$scope', '$http', 'PollingService', function($scope, $http, PollingService) {
        $scope.userNameRequiredError = "Required field";
        $scope.userNamePatternError = "Invalid name. Name must contains only alphabetical symbols and starts with capital letter.";
        $scope.messageTextRequiredError = "Required field";

        $scope.send = function(msgForm) {
            if (msgForm.$valid) {
                $http.put('/messages', JSON.stringify($scope.message)).then(
                    function successCallback(response) {
                        $scope.successfullySaveMessage = response.data;
                        $scope.message = {};
                        msgForm.$setPristine();
                        msgForm.$setUntouched();
                        PollingService.getMessages();
                    },
                    function errorCallback(response) {
                        if (response.status !== 422) {
                            $scope.failureSaveMessage = response.data;
                        } else {
                            response.data.forEach(function (error) {
                                if (error.pointer === 'userName') {
                                    if (error.constraint === 'required') {
                                        msgForm.uName.$error.required = true;
                                        $scope.userNameRequiredError = error.detail;
                                    } else {
                                        msgForm.uName.$error.pattern = true;
                                        $scope.userNamePatternError = error.detail;
                                    }
                                } else {
                                    msgForm.msgText.$error.required = true;
                                    $scope.messageTextRequiredError = error.detail;
                                }
                            });
                        }
                    }
                );
            }
        };
    }]);