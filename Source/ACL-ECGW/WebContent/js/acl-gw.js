$(function() {
    resizeWindow();
    $(window).resize(function() {
        resizeWindow();
    });
});

function resizeWindow() {
    if ($(window).width() <= 768) {
        $("img").parent().addClass("text_center");
    } else {
        $("img").parent().removeClass("text_center");
    }

    if ($(window).width() <= 568) {
        $("#chkAgree").parent().addClass("text_center");
    } else {
        $("#chkAgree").parent().removeClass("text_center");
    }
}
