function ajax_get(e, s) {
    var n = new XMLHttpRequest;
    s.hasClass("picture") && (n.onprogress = function () {
        0 == notifDownload && (main.append($("<div/>", {class: "flowspinner"}), $("<span/>", {class: "download-info"}).text("Downloading")), notifDownload = 1)
    }), n.onreadystatechange = function () {
        if (4 == this.readyState && 200 == this.status) {
            s.hasClass("picture") && ($(".flowspinner").remove(), $(".download-info").remove());
            var e = window.URL || window.webkitURL;
            s.attr("src", e.createObjectURL(this.response)), setTimeout(function () {
                s.hasClass("picture") || s.css("transition", "opacity .05s, transform .2s ease-in, box-shadow .2s ease-in"), s.css("transform", "rotateX(0deg)"), s.css("boxShadow", "none"), s.css("opacity", "1"), clearInterval(optimizeInterval), percentComplete = 0
            }, 100)
        }
    }, n.open("GET", e, !0), n.responseType = "blob", n.send()
}
var notifDownload = 0;