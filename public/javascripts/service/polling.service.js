'use strict';

angular.
    module('leaveMessageSystem').
    service('PollingService', ['$http', '$rootScope', '$interval', function ($http, $rootScope, $interval) {
            var updatedData;
            var self = this;

        self.getMessages = function() {
                $('#preloader').show();
                $('#messages').hide();

                $http.get('/messages').then(function successCallback(response) {
                    updatedData = response.data;
                    $rootScope.$broadcast('got new data!', {data: updatedData});
                    $('#preloader').hide();
                    $('#messages').show();
                } , function failureCallback(response) {
                    $rootScope.$broadcast('failure get messages!', {data: response.data});
                    $('#preloader').hide();
                })
            };

            $interval(function () {
                self.getMessages()
            }, 7000)
        }]
    );