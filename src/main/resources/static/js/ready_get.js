var notifDownload = 0, rotateDeg = 0, resolution, mainReady = $(".main"), transitSetting = "opacity .05s, transform .2s ease-in, box-shadow .2s ease-in", isResolution, body = $("body"), footer = $(".footer"), typeAnimation, isProcessing = false, resolutionElem = $("#resolution"), downloadPictureElem = $("#download-picture"), fileElem = $(".file"), arrowLeftElem = $("#arrow-left"), arrowRightElem = $("#arrow-right"), fileInfoElem = $("#file-info"), sizeElem = $("#size"), formatElem = $("#format"), infoElem = $(".info"), directLinkElem = $("#direct-link"), htmlLinkElem = $("#html-link"), bbcodeLinkElem = $("#bbcode-link"), px200ELem = $("#px200"), px500Elem = $("#px500"), notification = $(".notification");
notification.css("display", "block");

function settingPicture() {
    mainReady.css("top", "0");
    mainReady.css("bottom", "0");
    if ($(".main").length < 2) {
        mainReady.css("left", "0");
        mainReady.css("right", "0");
    } else {
        if (typeAnimation === "left") {
            mainReady.css("left", "-100%");
            mainReady.css("right", "100%");
        } else {
            mainReady.css("left", "100%");
            mainReady.css("right", "-100%");
        }
    }
    if (screen.width > 480) {
        footer.css("bottom", "-44px");
    }
    footer.css("background-color", "rgba(0,0,0,0.7)");
    body.css("background-color", "black");
    body.css("transition", "background-color .2s");
}

function setPicture(width, height) {
    picture.css("max-width", width);
    picture.css("max-height", height);
}

function calculateView(x, y) {
    isResolution = resolution[x] > resolution[y];
    if (window.innerWidth <= resolution[x] && window.innerHeight <= resolution[y]) {
        var isWindowInner = window.innerWidth > window.innerHeight, divideResolution = (resolution[x] / resolution[y] + "").substring(0, 3), isDivideResolutionAndWindow = divideResolution === (window.innerWidth / window.innerHeight + "").substring(0, 3);
        if (isResolution) {
            if (isWindowInner && (((screen.width / screen.height + "").substring(0, 3) === divideResolution && screen.width - window.innerWidth <= 211 && screen.height - window.innerHeight <= 211) || isDivideResolutionAndWindow)) {
                settingPicture();
                if (resolution[0] > resolution[1] && x === 0) {
                    setPicture("100%", "inherit");
                } else {
                    setPicture("inherit", (main.width() + (main.width() === picture.css("max-height") ? 0 : main.css("left") === "0px" ? 0 : 50)));
                }
                return true;
            }
        } else {
            if (!isWindowInner && isDivideResolutionAndWindow) {
                settingPicture();
                if (x === 0) {
                    setPicture("inherit", (main.height() + (screen.width < 480 ? 395 : main.height() === picture.css("max-width") ? 0 : main.css("left") === "0px" ? 0 : 140)));
                } else {
                    setPicture("calc", "inherit");
                }
                return true;
            }
        }
    }
    return false;
}

function setMainReady(element, isMinus) {
    element.css("left", "calc(" + (isMinus ? "-" : "+") + "100% + 25px)");
    element.css("right", "calc(" + (isMinus ? "+" : "-") + "100% + 25px)");
}

function setMainReadyPx(px) {
    body.css("background-color", px === "25px" ? "#34495E" : "black");
    mainReady.css("left", px);
    mainReady.css("right", px);
}

function addListenerDownload(element, type) {
    var isPicture = type === "picture";
    if (isPicture) {
        resolution = resolutionElem.text().split("x");
        setTimeout(function () {
            if (picture) {
                if (notifDownload === 0) {
                    mainReady.append($("<div/>", {class: "flowspinner"}), $("<span/>", {class: "download-info"}).text("Downloading"));
                    notifDownload = 1;
                }
            } else {
                if ($(".main").length > 1) {
                    $(".picture").first().css("filter", "brightness(40%)");
                    $(".main").first().append($("<div/>", {class: "flowspinner"}));
                    notifDownload = 1;
                }
            }
        }, 400);
    }
    var img = $("<img />", {
        "class": type,
        "alt": "Image"
    }).attr("src", (isPicture ? "/img/" : "/img_min/") + element.data("key") + "." + element.data("format"))
        .on("load", function () {
            if (!this.complete || typeof this.naturalWidth === "undefined" || this.naturalWidth === 0) {
                element.append($("<span />").text("Broken image"));
            } else {
                if (isPicture) {
                    notifDownload = 1;
                    if ($(".flowspinner").length) {
                        $(".flowspinner").remove();
                        $(".download-info").remove();
                    }
                }
                element.append(img[0]);
                if (isPicture) {
                    picture = $(".picture").last();
                    calculateView(0, 1);
                    if ($(".main").length > 1 && mainReady.css("top") !== "0px") {
                        if (typeAnimation === "right") {
                            setMainReady(mainReady, false);
                        } else {
                            setMainReady(mainReady, true);
                        }
                    }
                    if (mainReady.css("top") !== "0px") {
                        footer.css("bottom", $(".arrow-box").length ? "-44px" : "0px");
                        footer.css("background-color", "transparent");
                    }
                }
                setTimeout(function () {
                    if ($(".main").length < 2) {
                        if (isPicture) {
                            img[0].style.transition = transitSetting + ",  max-width .2s ease-in, max-height .2s ease-in";
                        } else {
                            img[0].style.transition = "filter .2s ease-in, " + transitSetting;
                        }
                    }
                    img[0].style.transform = "rotateX(0deg)";
                    img[0].style.boxShadow = "none";
                    img[0].style.opacity = "1";
                    if (isPicture) {
                        setTimeout(function () {
                            mainReady.css("transition", "top .2s, bottom .2s, left .2s, right .2s");
                            img[0].style.transition = transitSetting + ",  max-width .2s ease-in, max-height .2s ease-in";
                            var mainForFirst = $(".main");
                            if (mainForFirst.length > 1) {
                                if (typeAnimation === "left") {
                                    setMainReady($(mainForFirst[0]), false);
                                } else {
                                    setMainReady($(mainForFirst[0]), true);
                                }
                                if (mainReady.css("top") === "0px") {
                                    setMainReadyPx("0px");
                                } else {
                                    setMainReadyPx("25px");
                                }
                                setTimeout(function () {
                                    $(mainForFirst[0]).remove();
                                    isProcessing = false;
                                    main = $(".main");
                                    rotateDeg = 0;
                                }, 200);
                            } else {
                                isProcessing = false;
                            }
                        }, 40);
                        $(window).resize(function() {
                            calcViewRotateDeg();
                        });
                    }
                }, 20);
            }
        });
}

setTimeout(function () {
    if ($(".arrow-box").length) {
        if (screen.width < 480) {
            $(".footer").css("display", "none");
        } else {
            $(".footer").css("bottom", "-44px");
        }
    }
    if (downloadPictureElem.length) {
        addListenerDownload(mainReady, "picture");
    } else {
        for (var i = 0; i < fileElem.length; i++) {
            addListenerDownload($(fileElem[i]), "image-folder");
        }
    }
}, 20);

if (mainReady.data("left") !== "") {
    document.onkeydown = function (evt) {
        evt = evt || window.event;
        if (evt.keyCode === 37) {
            arrowLeftElem.click();
        } else if (evt.keyCode === 39) {
            arrowRightElem.click();
        }
    };
}

function settingNewPicture(message) {
    var newMainReady = $("<div>", {
        "class": "main"
    });
    mainReady.after(newMainReady);
    mainReady = $(".main").last();
    window.history.pushState("html", "OSPicture - Hosting the images", "/image/" + message.key);
    mainReady.data("key", message.key);
    mainReady.data("format", message.format);
    infoElem.css("bottom", "-44px");
    setTimeout(function () {
        resolutionElem.text(message.resolution);
        fileInfoElem.text(message.name);
        fileInfoElem.click(function () {
            actionCopyToClipboard(message.name);
        });
        sizeElem.text(message.size);
        formatElem.text(message.isOctetStream === "true" ? "octet-stream" : message.format);
        addListenerDownload(mainReady, "picture");
        setTimeout(function () {
            infoElem.css("bottom", "10px");
        }, 20);
    }, 200);
    downloadPictureElem.attr("href", "/img/" + message.key);
    downloadPictureElem.attr("download", message.name + (message.isOctetStream === "true" ? "" : ".") + (message.isOctetStream === "true" ? "" : message.format));
    directLinkElem.click(function () {
        actionCopyToClipboard(site + "img/" + message.key + "." + message.format);
    });
    htmlLinkElem.click(function () {
        actionCopyToClipboard("<a href=\"" + window.location.href + "\"><img src=\"" + site + "img/" + message.key + "." + message.format + "\" alt=\"Image from OSPicture\"></a>");
    });
    bbcodeLinkElem.click(function () {
        actionCopyToClipboard("[url=" + window.location.href + "][img]" + site + "img/" + message.key + "." + message.format + "[/img][/url]");
    });
    px200ELem.attr("href", "/" + message.px200Path);
    px500Elem.attr("href", "/" + message.px500Path);
    mainReady.data("left", message.folderLeft);
    mainReady.data("right", message.folderRight);
}

function actionDoLeft(message) {
    typeAnimation = "left";
    settingNewPicture(message);
}

function actionDoRight(message) {
    typeAnimation = "right";
    settingNewPicture(message);
}