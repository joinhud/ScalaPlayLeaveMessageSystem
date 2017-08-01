'use strict';

angular.
    module('leaveMessageSystem').
    controller('MessageListController', ['$scope', 'PollingService', function ($scope, PollingService) {
        $scope.messageListError = {};
        $('preloader').hide();
        PollingService.getMessages();

        $scope.$on('got new data!', function (event, args) {
            $scope.messageListError.apperead = false;
            $scope.messages = args.data.reverse();
        });

        $scope.$on('failure get messages!', function (event, args) {
            $scope.messageListError.apperead = true;
            $scope.messageListError.detail = args.data;
        });
    }]);