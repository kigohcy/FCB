$(function() {
    // 驗證身分證格式
    $.validator.addMethod("IDNO_CHECKER", function(value, element) {
        return this.optional(element) || /^[A-Z]{1}[0-9]{9}$/.test(value);
    }, "身分證格式不正確");
});
