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
var question_model_1 = require('../+suggestion/question.model');
var justification_service_1 = require('../shared/justification.service');
var CycquestionComponent = (function () {
    function CycquestionComponent(_justification, element) {
        this._justification = _justification;
        this.isProofViewOpen = false;
        this.elementRef = element;
    }
    CycquestionComponent.prototype.startDraggingQuestion = function (e, question, index) {
        e.dataTransfer.setData('custom', JSON.stringify(question));
        e.dataTransfer.setData('index', index + 1);
    };
    CycquestionComponent.prototype.ngOnInit = function () {
    };
    CycquestionComponent.prototype.showRationale = function (question) {
        var _this = this;
        console.log(question);
        this.isProofViewOpen = true;
        var elem = document.getElementById('spinner');
        var spinner = new Spinner({ radius: 60, width: 20, length: 80, lines: 15, className: 'loader', color: 'wheat' }).spin(elem);
        var ref = this;
        this._justification.fetchJustificationForConcept(question.index, this.sessionId)
            .subscribe(function (data) {
            _this.rationale = data.proofs;
            spinner.stop();
            console.log(_this.rationale);
            console.log("open the dialog");
            $("#proofQuestionId").dialog({
                modal: true,
                position: 'center',
                width: 1000,
                height: 500,
                title: "Rational for :" + question.nl,
                show: {
                    effect: "blind",
                    duration: 1000
                },
                hide: {
                    effect: "explode",
                    duration: 1000
                },
                close: function (event, ui) {
                    ref.isProofViewOpen = false;
                }
            });
        });
    };
    __decorate([
        core_1.Input(), 
        __metadata('design:type', question_model_1.Question)
    ], CycquestionComponent.prototype, "question", void 0);
    __decorate([
        core_1.Input(), 
        __metadata('design:type', Number)
    ], CycquestionComponent.prototype, "index", void 0);
    __decorate([
        core_1.Input(), 
        __metadata('design:type', String)
    ], CycquestionComponent.prototype, "sessionId", void 0);
    CycquestionComponent = __decorate([
        core_1.Component({
            moduleId: module.id,
            selector: 'cyc-question',
            templateUrl: 'cycquestion.component.html',
            styleUrls: ['cycquestion.component.css'],
            providers: [justification_service_1.JustificationService]
        }), 
        __metadata('design:paramtypes', [justification_service_1.JustificationService, core_1.ElementRef])
    ], CycquestionComponent);
    return CycquestionComponent;
}());
exports.CycquestionComponent = CycquestionComponent;
//# sourceMappingURL=C:/UI_STASH_REPO/LATEST/poc-development/tmp/broccoli_type_script_compiler-input_base_path-tYOsqpIh.tmp/0/app/cycquestion/cycquestion.component.js.map