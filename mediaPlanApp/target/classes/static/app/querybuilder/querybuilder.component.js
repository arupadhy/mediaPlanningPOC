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
var reachdata_service_1 = require('../shared/reachdata.service');
var QuerybuilderComponent = (function () {
    function QuerybuilderComponent(element, _reachDataService) {
        this._reachDataService = _reachDataService;
        this.existingQuestions = [];
        this.questions = [];
        this.questionsUsedInQuery = [];
        this.query = new core_1.EventEmitter();
        this.elem = element;
        console.log('consructor for querybuilder component');
        //$("#yyy").queryBuilder();
    }
    QuerybuilderComponent.prototype.test = function (e) {
        e.preventDefault();
    };
    QuerybuilderComponent.prototype.createQuery = function (data) {
        var query = "(";
        query = query.concat(data.nl);
        var answers = [];
        data.answers.forEach(function (a) {
            if (a.isAnswerSelected) {
                answers.push(a.nl);
            }
        });
        query = query.concat(":").concat(answers.toString());
        return query.concat(")");
    };
    QuerybuilderComponent.prototype.drop = function (e) {
        var ref = this;
        e.preventDefault();
        var data = e.dataTransfer.getData('custom');
        var index = e.dataTransfer.getData('index');
        this.questionsUsedInQuery.push(JSON.parse(data));
        if (!this.queryString) {
            console.log("querystring hasn't been init yet");
            this.queryString = this.createQuery(JSON.parse(data));
            this.fetchReachData(this.queryString);
            this.query.emit({ query: this.queryString, questions: this.questionsUsedInQuery });
        }
        if (this.q) {
            //check if this question has already been dropped
            console.log(this.existingQuestions);
            if (1 === 2) {
                alert("This question is already present in the query");
            }
            else {
                $('#and-or-query').dialog({
                    show: {
                        effect: "blind",
                        duration: 1000
                    },
                    hide: {
                        effect: "explode",
                        duration: 1000
                    },
                    modal: true,
                    width: 400,
                    buttons: {
                        "AND": function () {
                            ref.existingQuestions.push({ operand: "and" });
                            ref.existingQuestions.push({ value: "Q" + index });
                            ref.existingQuestions.push({ question: ref.getDroppedQuestionString(JSON.parse(data)) });
                            ref.questions.push("Q" + index);
                            ref.queryString = ref.queryString.concat("AND").concat(ref.createQuery(JSON.parse(data)));
                            ref.fetchReachData(ref.queryString);
                            ref.query.emit({ query: ref.queryString, questions: ref.questionsUsedInQuery });
                            $(this).dialog("close");
                        },
                        "OR": function () {
                            ref.existingQuestions.push({ operand: "or" });
                            ref.existingQuestions.push({ value: "Q" + index });
                            ref.existingQuestions.push({ question: ref.getDroppedQuestionString(JSON.parse(data)) });
                            ref.questions.push("Q" + index);
                            ref.queryString = ref.queryString.concat("OR").concat(ref.createQuery(JSON.parse(data)));
                            ref.fetchReachData(ref.queryString);
                            ref.query.emit({ query: ref.queryString, questions: ref.questionsUsedInQuery });
                            $(this).dialog("close");
                        }
                    }
                });
            }
        }
        else {
            this.q = "Q" + index;
            this.existingQuestions.push({ value: this.q });
            ref.existingQuestions.push({ question: ref.getDroppedQuestionString(JSON.parse(data)) });
            ref.questions.push("Q" + index);
        }
    };
    QuerybuilderComponent.prototype.getDroppedQuestionString = function (q) {
        var droppedQuestion = "";
        droppedQuestion = droppedQuestion.concat(q.nl).concat(": ");
        var droppedAnswers = "";
        q.answers.forEach(function (a) {
            if (a.isAnswerSelected) {
                droppedAnswers = droppedAnswers.concat(a.nl).concat(",");
            }
        });
        return droppedQuestion.concat(droppedAnswers);
    };
    QuerybuilderComponent.prototype.getQuery = function () {
        var s = '';
        this.existingQuestions.forEach(function (a) { return s = s + 'and'; });
        return s;
    };
    QuerybuilderComponent.prototype.droppedSomething = function (e) {
        var data = e.dataTransfer.getData('custom');
        var index = e.dataTransfer.getData('index');
        var queryString = this.createQuery(JSON.parse(data));
        this.fetchReachData(queryString);
    };
    QuerybuilderComponent.prototype.fetchReachData = function (str) {
        var _this = this;
        this._reachDataService.getReachData(str)
            .subscribe(function (data) {
            _this.reachData = data;
            _this.reachData.query = str;
        });
    };
    QuerybuilderComponent.prototype.ngOnInit = function () {
    };
    __decorate([
        core_1.Output(), 
        __metadata('design:type', core_1.EventEmitter)
    ], QuerybuilderComponent.prototype, "query", void 0);
    QuerybuilderComponent = __decorate([
        core_1.Component({
            moduleId: module.id,
            selector: 'query-builder',
            templateUrl: 'querybuilder.component.html',
            styleUrls: ['querybuilder.component.css'],
            providers: [reachdata_service_1.ReachdataService]
        }), 
        __metadata('design:paramtypes', [core_1.ElementRef, reachdata_service_1.ReachdataService])
    ], QuerybuilderComponent);
    return QuerybuilderComponent;
}());
exports.QuerybuilderComponent = QuerybuilderComponent;
//# sourceMappingURL=C:/UI_STASH_REPO/poc-development/tmp/broccoli_type_script_compiler-input_base_path-TRL9n9Hx.tmp/0/app/querybuilder/querybuilder.component.js.map