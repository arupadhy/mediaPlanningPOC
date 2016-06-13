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
var CycquestionService = (function () {
    function CycquestionService(_http) {
        this._http = _http;
        this.results = [];
    }
    CycquestionService.prototype.extractAnswer = function (data) {
        var answers = [];
        data.answers.forEach(function (a) {
            answers.push(new cycanswer_model_1.Cycanswer(a.nl, a.cycl, a.rmicode, a.recommended));
        });
        return answers;
    };
    CycquestionService.prototype.loadQuestionsFromCyc = function (sentences, sessionId) {
        var _this = this;
        var ref = this;
        var elem = document.getElementById('spinner');
        var spinner = new Spinner({ radius: 60, width: 20, length: 80, lines: 15, className: 'loader', color: 'wheat' }).spin(elem);
        var headers = new http_1.Headers();
        headers.append('Content-Type', 'application/json');
        this._http.post(endpoints_1.endpoints.GET_CYC_QUESTIONS_DEMO + "?sessionId=" + sessionId, JSON.stringify({ 'sentences': sentences }), { headers: headers })
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
    CycquestionService = __decorate([
        core_1.Injectable(), 
        __metadata('design:paramtypes', [http_1.Http])
    ], CycquestionService);
    return CycquestionService;
}());
exports.CycquestionService = CycquestionService;
//# sourceMappingURL=C:/UI_STASH_REPO/LATEST/poc-development/tmp/broccoli_type_script_compiler-input_base_path-tYOsqpIh.tmp/0/app/shared/cycquestion.service.js.map