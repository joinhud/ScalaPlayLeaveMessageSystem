'use strict';

angular.
	module('sendMessage').
	component('sendMessageForm', {
		templateUrl: '/assets/javascripts/send-message-form/send-message-form.template.html',
		controller: ['$http', function SendMessageController($http) {
			var self = this;

			self.send = function(msgForm) {
				if (msgForm.$valid) {
					$http.put('/messages', JSON.stringify(self.message)).then(function success(data) {
						self.result = data;
					});
				}
			};
		}]
	});