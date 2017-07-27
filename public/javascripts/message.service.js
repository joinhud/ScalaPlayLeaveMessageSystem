$.ajax({
    url: "/messages",
    beforeSend: function () {
        $("#preloader").show()
    },
    success: function (data) {
        $("#preloader").hide();
        $("#messages").empty();

        var messageList = JSON.parse(data);

        if(jQuery.isEmptyObject(messageList)) {
            $("#messages").append(createEmptyListInfoAlert());
        } else {
            $.each(messageList.reverse(), function (idx, obj) {
                var messageCard = createMessageCard(obj);
                $("#messages").append(messageCard);
            });
        }
    },
    error: function () {
        $("#preloader").hide();
        $("#messages").empty();

        $("#messages").append(createServerErrorAlert())
    }
});

function createMessageCard(messageData) {
    var msgPanel = document.createElement("div");
    msgPanel.classList.add('panel', 'panel-info');

    var msgPanelHeader = document.createElement("div");
    msgPanelHeader.classList.add('panel-heading');
    msgPanelHeader.innerHTML = "<span class=\"glyphicon glyphicon-user\"></span> " + messageData.userName;

    var msgPanelBody = document.createElement("div");
    msgPanelBody.classList.add('panel-body');
    msgPanelBody.innerHTML = messageData.messageText;

    var msgPanelFooter = document.createElement("div");
    msgPanelFooter.classList.add('panel-footer');
    msgPanelFooter.innerHTML = "Date: " + messageData.date;

    $(msgPanel).append(msgPanelHeader);
    $(msgPanel).append(msgPanelBody);
    $(msgPanel).append(msgPanelFooter);

    return msgPanel;
}

function createEmptyListInfoAlert() {
    var alert = document.createElement("div");
    alert.classList.add('alert', 'alert-info', 'text-center');
    alert.innerHTML = "<span class=\"glyphicon glyphicon-info-sign\"></span>  The message list is empty yet. Please leave your first message.";

    return alert;
}

function createServerErrorAlert() {
    var alert = document.createElement("div");
    alert.classList.add('alert', 'alert-danger', 'text-center');
    alert.innerHTML = "<span class=\"glyphicon glyphicon-minus-sign\"></span> Can not receive data from server because a server error has occurred. Try repeat again later.";
    return alert;
}