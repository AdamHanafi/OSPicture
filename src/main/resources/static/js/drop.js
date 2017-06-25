var dropZone;

function dropEnter(o) {
    o.stopPropagation(), o.preventDefault(), dropZone.addClass("hover");
}

function dropLeave() {
    dropZone.removeClass("hover");
}

function doDrop(o) {
    o.stopPropagation(), o.preventDefault(), dropZone.removeClass("hover");
    var e = o.dataTransfer;
    if (!e && !e.files) {
        return !1;
    }
    var r = e.files;
    return e.dropEffect = "copy", r.length < 2 ? ajaxUpload(r[0]) : ajaxUploads(r), !1;
}