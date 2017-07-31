'use strict';

angular
	.module('sendMessage')
	.component('sendMessageForm', {
		templateUrl: '/assets/javascripts/send-message-form/send-message-form.template.html',
        controller: ['$scope', '$http', function SendMessageController($scope, $http) {
            var self = this;
            self.message = {};
            self.send = function(msgForm) {
            	console.log('pressed');
                if (msgForm.$valid) {
                    $http.put('/messages', JSON.stringify(self.message)).then(function success(data) {
                        self.result = data;
                    });
                }
            };
        }]
	});