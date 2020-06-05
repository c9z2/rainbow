
var stompClient = null;
var contactList = null;
var contactNow = null;
var signInUser = localStorage.getItem("userId");

Date.prototype.Format = function(fmt)   
{ //author: meizz   
  var o = {   
    "M+" : this.getMonth()+1,                 //月份   
    "d+" : this.getDate(),                    //日   
    "h+" : this.getHours(),                   //小时   
    "m+" : this.getMinutes(),                 //分   
    "s+" : this.getSeconds(),                 //秒   
    "q+" : Math.floor((this.getMonth()+3)/3), //季度   
    "S"  : this.getMilliseconds()             //毫秒   
  };   
  if(/(y+)/.test(fmt))   
    fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));   
  for(var k in o)   
    if(new RegExp("("+ k +")").test(fmt))   
  fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));   
  return fmt;   
}

function loadSession(){
    /*
        [
            {
                "remark":"",
                "userId":"",
                "avatar":"",
                "date":123,
            },
            {
                "remark":"",
                "userId":"",
                "avatar":"",
                "date":123,

            }
        ]
        
    */
    var sessionList = sessionStorage.getItem("session");
    if(sessionList){
        var list = $(".contact-list");
         session = JSON.parse(sessionList);
         for(var i=0;i<session.length;i++){
            var rows = session[i];
            var curcontact = $("<div class='contact'><div class='contact-avator'><img src='"+rows[i].avatar+"'/></div>"+
                                        "<input type='hidden' value='"+i+"'/>"+
                                        "<div class='contact-i'><div class='contact-name'><span>"+rows[i].remark+"</span><span>"+new Date(msg.date).Format("hh:mm")+"</span></div>"+
                                        "<div class='contact-nickname'><span>"+rows[i].id+"</span></div> </div></div>");
            list.apend(curcontact);

            if(i==0){
               $(".chat-contact a").html(rows[i].remark);
               $(".chat-contact span").html(rows[i].id);
               $(".chat-content div").remove();
               contactNow = curcontact;
            }

         }

    }
   


}


 function appendMessage(data){
        /**
        string msgId = 1;
        int32 msgType = 2;
        string content = 3;
        string sender = 4;
        string receiver = 5;
        string date = 6;
        */
        
    var msgStr = data.body;
    var msg = JSON.parse(msgStr);
    if(signInUser != msg.sender && msg.sender != contactNow.id){
        return;
    }
    var chatPop;
    if(msg.sender == signInUser){

        chatPop = $("<div class='me'><div class='chat-me'><span>"+msg.content+"</span><p>"+new Date(msg.date).Format("hh:mm")+"</p></div></div>");
    }else{
        chatPop = $("<div class='you'><div class='chat-you'><span>"+msg.content+"</span><p>"+new Date(msg.date).Format("hh:mm")+"</p></div></div>");
    }
    $(".chat-content").append(chatPop);
    $(".chat-content").scrollTop($(".chat-content")[0].scrollHeight);


 }

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    var socket = new SockJS('/ws/rainbow-ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        var userId = localStorage.getItem("userId");
        var sub = '/user/'+userId+'/message';
        stompClient.subscribe(sub, function (msg) {
            console.log("msg"+ msg);

            appendMessage(msg);
            cacheMessage(msg.body);
            // showGreeting(JSON.parse(greeting.body).content);
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendMsg(msg) {
    stompClient.send("/app/message", {}, JSON.stringify(msg));
}


function uuid() {
    var s = [];
    var hexDigits = "0123456789abcdef";
    for (var i = 0; i < 36; i++) {
        s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
    }
    s[14] = "4";  // bits 12-15 of the time_hi_and_version field to 0010
    s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);  // bits 6-7 of the clock_seq_hi_and_reserved to 01
    s[8] = s[13] = s[18] = s[23] = "-";
 
    var uuid = s.join("");
    return uuid;
}

//list contact
function listContact(){
    $.ajax({
        headers: {
            Authorization: "berarer "+localStorage.getItem("token")
        },
        type: "get",
        url:"/biz/contact/list",
        success: function (data) {
            if(data.code ==200){
                var rows = data.data;
                var list = $(".contact-list");
                list.children().filter("div").remove();
                for(var i=0; i<rows.length;i++){
                    var curcontact = $("<div class='contact'><div class='contact-avator'><img src='"+rows[i].avatar+"'/></div>"+
                                        "<input type='hidden' value='"+i+"'/>"+
                                        "<div class='contact-i'><div class='contact-name'><span>"+rows[i].remark+"</span></div>"+
                                        "<div class='contact-nickname'><span>"+rows[i].id+"</span></div> </div></div>");
                    list.append(curcontact);

                    curcontact.click(function(){
                        selectContact(this);
                    })
                }
                contactList = data.data;
            }
            
        }
    });

}

function loadChat(receiver){
    var chatList = sessionStorage.getItem("chatList");
    if(chatList){
        var chatsInStorage = JSON.parse(chatList);
        var chats = chatsInStorage[receiver];
        var chats = chatsInStorage[receiver];
        if(chats){
            for(var i=0;i<chats.length;i++){
            var row = chats[i];
            var chatPop;
            if(row.sender == signInUser){
                chatPop = $("<div class='me'><div class='chat-me'><span>"+row.content+"</span><p>"+new Date(row.date).Format("hh:mm")+"</p></div></div>");
            }else{
                chatPop = $("<div class='you'><div class='chat-you'><span>"+row.content+"</span><p>"+new Date(row.date).Format("hh:mm")+"</p></div></div>");
            }
            $(".chat-content").append(chatPop);
        }
        $(".chat-content").scrollTop($(".chat-content")[0].scrollHeight);
        }
        
    }
}

function selectContact(curcontact){
   var index = $(curcontact).children().filter("input").val();
   contactNow = contactList[index];
   $(".chat-contact a").html(contactNow.remark);

   $(".chat-contact span").html(contactNow.id);
   $(".chat-content div").remove();
    loadChat(contactNow.id);



}



function signInWS(){
    $.post("/ws/sys/signIn",
        {
            "token":localStorage.getItem("token"),
            "cid":uuid(),
            "clientType":"WEB"
        },
        function(data){
            console.log(data);
            
        });
}

function cacheMessage(msg){
    /*
        {
            "userId":[
                {
                string msgId = 1;
                int32 msgType = 2;
                string content = 3;
                string sender = 4;
                string receiver = 5;
                string date = 6;
                },
                {
                string msgId = 1;
                int32 msgType = 2;
                string content = 3;
                string sender = 4;
                string receiver = 5;
                string date = 6;
                },
            ],
            "userId":[
                {
                string msgId = 1;
                int32 msgType = 2;
                string content = 3;
                string sender = 4;
                string receiver = 5;
                string date = 6;
                },
                {
                string msgId = 1;
                int32 msgType = 2;
                string content = 3;
                string sender = 4;
                string receiver = 5;
                string date = 6;
                },
            ],
        }
    */
    var chatList = sessionStorage.getItem("chatList");
    msg = JSON.parse(msg);
    if(msg.receiver == signInUser){
        msg.receiver = msg.sender;
    }
    if(chatList){

        var chatsInStorage = JSON.parse(chatList);
        var chats = chatsInStorage[msg.receiver];
        if(chats){
            chats.push(msg);
        }else{
            chats = [msg];
        }
        chatsInStorage[msg.receiver] = chats;
        sessionStorage.setItem("chatList",JSON.stringify(chatsInStorage));

    }else{
        var chatsInStorage = new Object();
        chatsInStorage[msg.receiver] = [msg];
       
        sessionStorage.setItem("chatList",JSON.stringify(chatsInStorage));
    }
   
}

$(function () {
    
    signInWS();
    connect();
   

    $("#contact-load").click(function(){
        contactList = listContact();
    });



    // $( "#connect" ).click(function() {  });
    // $( "#disconnect" ).click(function() { disconnect(); });
    // $( "#send" ).click(function() { sendName(); });

    //   $("form").on('submit', function (e) {
    //     e.preventDefault();
    // });


    var typing = $(".input-typing");
    $(document).keyup(function(event){
      if(event.keyCode ==13){
        var content = typing.val();
        if(content == '' || content == null){
            return;
        }
        /**
        string msgId = 1;
        int32 msgType = 2;
        string content = 3;
        string sender = 4;
        string receiver = 5;
        string date = 6;
        */
        var msg = {
            "msgType":2,
            "content":content,
            "sender":localStorage.getItem("userId"),
            "receiver":contactNow.id
        }
        sendMsg(msg);
        typing.val(null);
        $(".chat-content").scrollTop($(".chat-content")[0].scrollHeight);

      }
    });
    
});


