"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
var core_1 = require('@angular/core');
var CycavatarComponent = (function () {
    function CycavatarComponent() {
    }
    CycavatarComponent.prototype.displayAvatar = function () {
        this.showAvatar = true;
    };
    CycavatarComponent.prototype.updateAvatar = function () {
        this.flag = false;
    };
    CycavatarComponent.prototype.startQueryAnimation = function () {
        //Query Animation
        var count;
        var CycQuery, currentHeight;
        $('.queryFade').fadeIn();
        if ($('#cycStateQuery').css('display') == 'none') {
            $('#cycStateQuery').fadeIn();
        }
        count = 0;
        CycQuery = new Vivus('cycQuery', {
            type: 'async',
            duration: 120,
            animTimingFunction: Vivus.EASE_OUT
        }, thinkTwice);
        function thinkTwice() {
            $('.queryFade').fadeOut(500, function () {
                CycQuery.reset().stop().play();
            });
            $('#cycStateQuery').delay().fadeOut();
            //$("#cycInput").focus();
        }
    };
    CycavatarComponent.prototype.startBubbleAnimation = function () {
        cycListen();
        $("#cycStateListen").show();
        function cycListen() {
            console.log("cyclisten function called");
            $("#listenCircle").delay(100).animate({ r: 140 }, 1500, 'easeInOutQuad', function () {
                $("#listenCircle").animate({ r: 100 }, 1500, 'easeOutQuad', cycListen());
            });
        }
    };
    CycavatarComponent.prototype.startAnimation = function () {
        var listenRadius, timer = null;
        var doneTypingInterval = 1000;
        $("#listenCircleTyping").fadeIn();
        listenRadius = parseInt($("#listenCircleTyping").attr('r'));
        listenRadius += (75 * Math.random()) * 1;
        $("#listenCircleTyping").animate({ r: listenRadius }, 100);
        clearTimeout(timer);
        timer = setTimeout(doStuff, 1000);
        function doStuff() {
            $("#listenCircle").fadeIn();
            $("#listenCircleTyping").fadeOut();
        }
    };
    CycavatarComponent.prototype.enableCycAvatarAnimation = function () {
        //   console.log($('#planNameInput'));
        //Resting State Animation
        var CycRest = new Vivus('cyc1', {
            type: 'async',
            duration: 120,
            animTimingFunction: Vivus.EASE_OUT
        }, fadeLine);
        function fadeLine() {
            $('#cyc1a,#cyc1b').fadeOut(500);
        }
        var refreshIntervalId = setInterval(function () {
            $('#cyc1a,#cyc1b').show();
            CycRest.reset().stop().play();
            //$('#cyc1a').fadeOut();
        }, 3000);
        //   console.log($('#planNameInput'));
        //Cyc listening and active typing
        var listenRadius, timer = null;
        var doneTypingInterval = 1000;
        //on keyup, start the countdown
        $('#planNameInput').keydown(function () {
            //$("#listenCircle").fadeOut();
            console.log($("#listenCircleTyping"));
            $("#listenCircleTyping").fadeIn();
            listenRadius = parseInt($("#listenCircleTyping").attr('r'));
            listenRadius += (75 * Math.random()) * 1;
            $("#listenCircleTyping").animate({ r: listenRadius }, 100);
            clearTimeout(timer);
            timer = setTimeout(doStuff, 1000);
        });
        function doStuff() {
            $("#listenCircle").fadeIn();
            $("#listenCircleTyping").fadeOut();
        }
        //Cyc listening but no active typing
        $("#planNameInput").focus(function () {
            console.log("cyc listening but no active typing yet");
            $("#cycStateListen").fadeIn();
            $('#cycStateQuery').fadeOut();
            isCycListening = 0;
            cycListen();
        });
        $("#planNameInput").focusout(function () {
            $("#cycStateListen").fadeOut();
            isCycListening = 1;
            cycListen();
        });
        //Listening but no typing animation
        var isCycListening = 0;
        function cycListen() {
            console.log("cyclisten function called");
            $("#listenCircle").delay(100).animate({ r: 140 }, 1500, 'easeInOutQuad', function () {
                $("#listenCircle").animate({ r: 100 }, 1500, 'easeOutQuad', cycListen());
            });
        }
    };
    CycavatarComponent.prototype.ngOnInit = function () {
        this.flag = true;
        this.enableCycAvatarAnimation();
    };
    __decorate([
        core_1.Input(), 
        __metadata('design:type', Boolean)
    ], CycavatarComponent.prototype, "showAvatar", void 0);
    CycavatarComponent = __decorate([
        core_1.Component({
            moduleId: module.id,
            selector: 'cyc-avatar',
            templateUrl: 'cycavatar.component.html',
            styleUrls: ['cycavatar.component.css']
        }), 
        __metadata('design:paramtypes', [])
    ], CycavatarComponent);
    return CycavatarComponent;
}());
exports.CycavatarComponent = CycavatarComponent;
//# sourceMappingURL=C:/UI_STASH_REPO/poc-development/tmp/broccoli_type_script_compiler-input_base_path-TRL9n9Hx.tmp/0/app/cycavatar/cycavatar.component.js.map