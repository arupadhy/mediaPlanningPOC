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
var http_1 = require('@angular/http');
var endpoints_1 = require('./endpoints');
var cycanswer_model_1 = require('../+suggestion/cycanswer.model');
var question_model_1 = require('../+suggestion/question.model');
var MrifieldService = (function () {
    function MrifieldService(_http) {
        this._http = _http;
        this.results = [];
        this.FIELDS = "Men ;Women;Single ;Married;Sep/Wid/Div;Engaged;Living with partner/Fiance/Boyfriend/Girlfriend;White;Spanish/Hispanic/Latino;Black/African American;Employed;Employed full time;Employed part time;Non employed;Grocery Stores # Times Shopped Last 30 Days – Dave Mart [6-9 or 10-14 or 15 or More];Food Prepared from Scratch # Times in the Last 30 Days (Principal Shopper) [L None];Food Prepared from Scratch # Times in the Last 30 Days (Principal Shopper) [L 1 - 2];Food Prepared from Scratch # Times in the Last 30 Days (Principal Shopper) [M 3 - 4];Food Prepared from Scratch # Times in the Last 30 Days (Principal Shopper) [M 5 - 7];Food Prepared from Scratch # Times in the Last 30 Days (Principal Shopper) [H 8 - 10];Food Prepared from Scratch # Times in the Last 30 Days (Principal Shopper) [H 11 - 15];I try to eat healthy these days and pay attention to my nutrition [Any Agree];I enjoy being creative in the kitchen [Any Agree];I try to buy foods that are grown or produced locally (in the region where I live) [Any Agree];I enjoy trying different types of food [Any Agree];Dinners in my home are usually planned ahead of time [Any Agree];I regularly eat organic foods [Any Agree];I often use recipes when preparing a meal [Any Agree];I’m always on the lookout for quick and easy meal options [Any Agree];Used Grocery Store Loyalty Card Last 30 Days (Principal Shopper) [Yes];Grocery Shopping Time of Day Shop for Groceries (Principal Shopper) [Morning];Grocery Shopping Time of Day Shop for Groceries (Principal Shopper) [Afternoon];Grocery Shopping Time of Day Shop for Groceries (Principal Shopper) [Evening];Grocery Shopping Time of Day Shop for Groceries (Principal Shopper) [Various Times];Grocery Shopping Day(s) Shopped in Past Week (Principal Shopper) [Sunday];Grocery Shopping Day(s) Shopped in Past Week (Principal Shopper) [Monday];Grocery Shopping Day(s) Shopped in Past Week (Principal Shopper) [Tuesday];Grocery Shopping Day(s) Shopped in Past Week (Principal Shopper) [Wednesday];Grocery Shopping Day(s) Shopped in Past Week (Principal Shopper) [Thursday];Grocery Shopping Day(s) Shopped in Past Week (Principal Shopper) [Friday];Grocery Shopping Day(s) Shopped in Past Week (Principal Shopper) [Saturday];Grocery Shopping # of Trips in Average Week (Principal Shopper) [None];Grocery Shopping # of Trips in Average Week (Principal Shopper) [1];Grocery Shopping # of Trips in Average Week (Principal Shopper) [2];Buying Styles - Any Agree [Buying American products is important to me];Buying Styles - Any Agree [I know the price I pay for most of the foods and packaged goods I buy];Buying Styles - Any Agree [I think shopping is a great way to relax];Buying Styles - Any Agree [I enjoy wandering the store looking for new, interesting products];Buying Styles - Any Agree [I only purchase products online when I have a coupon or promotional code for the site];Buying Styles - Any Agree [I don't make purchase decisions based on advertising];Buying Styles - Any Agree [I like to shop around before making a purchase];Buying Styles - Any Agree [If I really want something I will buy it on credit rather than wait];Buying Styles - Any Agree [I buy based on quality, not price];Buying Styles - Any Agree [I buy natural products because I am concerned about the environment];Buying Styles - Any Agree [The offer of “free shipping” attracts me to a shopping website]";
    }
    MrifieldService.prototype.extractAnswer = function (data) {
        var answers = [];
        data.answers.forEach(function (a) {
            answers.push(new cycanswer_model_1.Cycanswer(a.nl, a.cycl, a.rmicode, a.recommended));
        });
        return answers;
    };
    MrifieldService.prototype.getHardCodedFields = function () {
        return this.FIELDS;
    };
    MrifieldService.prototype.getMriFields = function (selectedQuestions) {
        var _this = this;
        var ref = this;
        var elem = document.getElementById('spinner');
        var spinner = new Spinner({ radius: 60, width: 20, length: 80, lines: 15, className: 'loader', color: 'wheat' }).spin(elem);
        var headers = new http_1.Headers();
        headers.append('Content-Type', 'application/json');
        this._http.post(endpoints_1.endpoints.GET_MRI_FIELDS, JSON.stringify({ 'queryTerms': selectedQuestions }), { headers: headers })
            .map(function (res) { return res.json(); })
            .subscribe(function (data) {
            data.mriFieldConstraints.forEach(function (constraints) {
                var q = constraints.question;
                var res = new question_model_1.Question(q.nl, q.cycl, _this.extractAnswer(constraints), constraints.index);
                ref.results.push(res);
            });
            spinner.stop();
        });
        return this.results;
    };
    MrifieldService = __decorate([
        core_1.Injectable(), 
        __metadata('design:paramtypes', [http_1.Http])
    ], MrifieldService);
    return MrifieldService;
}());
exports.MrifieldService = MrifieldService;
//# sourceMappingURL=C:/UI_STASH_REPO/LATEST/poc-development/tmp/broccoli_type_script_compiler-input_base_path-tYOsqpIh.tmp/0/app/shared/mrifield.service.js.map