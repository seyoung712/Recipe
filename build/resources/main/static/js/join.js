function dupCheck() {
    //alert("중복체크");
    var userId = $("#userId").val();
    //alert("id = " + userId);
    if ( userId == "" ) {
        alert("아이디를 입력하세요.");
        return;
    }
    var data = { "userId" : userId }
    $.ajax ({
        url: "/view/join/dupCheck",
        data: data,
        type:"get",
        success: function ( result ) {
            //alert(result);
            //alert(result.dupCheckCnt);
            if ( result.dupCheckCnt == "0" ) {
                alert(userId + "는 사용가능한 아이디입니다.");
            } else {
                alert(userId + "는 이미 사용중인 아이디입니다.");
            }
        }
    });
}
function authSend() {
    //alert("인증번호전송");
    var phone = $("#phone").val();
    if ( phone == "" ) {
        alert("휴대폰 번호를 입력하고 인증번호 전송을 클릭하세요.");
        return;
    }
    var data = { "phone" : phone }
    $.ajax ({
        url: "/view/join/authSend",
        data: data,
        type:"get",
        success: function ( result ) {
            if ( result.result == "1" ) {
                alert("인증번호가 전송되었습니다.");
            } else {
                alert("오류입니다. 다시 시도하세요.");
            }
        }
    });
}

function authCheck() {
    //alert("인증번호체크");
    var phone = $("#phone").val();
    var authNo = $("#certifi").val();
    if ( phone == "" || authNo == "" ) {
        alert("인증번호를 입력하세요.");
        return;
    }
    var data = { "phone" : phone, "authNo" : authNo }
    $.ajax ({
        url: "/view/join/checkAuthNo",
        data: data,
        type:"get",
        success: function ( result ) {
            if ( result.result == "1" ) {
                alert("인증이 완료되었습니다.");
            } else {
                alert("인증번호가 올바르지 않습니다.");
            }
        }
    });
}

function regOk() {
    con = confirm("가입하시겠습니까?");
    if ( con ) {
        var userId = $("#userId").val();
        var userNm = $("#userNm").val();
        var userPw = $("#userPw").val();
        var phone = $("#phone").val();
        var email = $("#email").val();

        var data = { "phone" : phone, "email" : email, "userId" : userId, "userNm" : userNm, "userPw" : userPw}
        $.ajax ({
            url: "/view/join/joinOk",
            data: data,
            type:"post",
            success: function ( result ) {
                if ( result.result == "1" ) {
                    alert("가입이 완료되었습니다.");
                    document.location.href = "/home";
                } else {
                    alert("가입에 실패하였습니다. 다시시도하세요.");
                }
            }
        });
    }
}
function loginOk() {
    var userId = $("#userId").val();
    var userPw = $("#userPw").val();

    if ( userId == "" || userPw == "" ) {
        alert("아이디/비밀번호를 입력하세요.");
        return;
    }

    var data = { "userId" : userId, "userPw" : userPw}
    $.ajax ({
        url: "/view/join/loginProc",
        data: data,
        type:"post",
        success: function ( result ) {
            if ( result.result == "1" ) {
                document.location.href = "/home";
            } else {
                alert("로그인에 실패하였습니다. 아이디/비밀번호를 확인하세요.");
            }
        }
    });
}

