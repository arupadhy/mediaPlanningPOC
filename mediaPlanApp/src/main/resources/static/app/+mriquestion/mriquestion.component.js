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
var router_1 = require('@angular/router');
var cycquestion_service_1 = require('../shared/cycquestion.service');
var justification_service_1 = require('../shared/justification.service');
var MriquestionComponent = (function () {
    function MriquestionComponent(_justification, _router, _routeSeg, _cycQuestiontService) {
        this._justification = _justification;
        this._router = _router;
        this._routeSeg = _routeSeg;
        this._cycQuestiontService = _cycQuestiontService;
        this.editAudienceEnabled = false;
        this.selectedQuestions = [];
        this.selectedInputs = this._routeSeg.getParam('selectedInputs');
        this.sessionId = this._routeSeg.getParam('session');
    }
    MriquestionComponent.prototype.ngOnInit = function () {
        this.questions = this._cycQuestiontService.loadQuestionsFromCyc(this._routeSeg.getParam('target'), this.sessionId);
    };
    MriquestionComponent.prototype.selectQuestion = function (q) {
        this.selectedQuestions.push(q);
        q.selected = !q.selected;
    };
    MriquestionComponent.prototype.selectAnswer = function (answer) {
        answer.recommended = !answer.recommended;
    };
    MriquestionComponent.prototype.loadQuerypage = function () {
        this._router.navigate(['suggestion', { 'target': this.selectedQuestions, 'selectedInputs': this.selectedInputs, 'sessionId': this.sessionId }]);
    };
    MriquestionComponent.prototype.editTargetAudience = function (evt) {
        if (evt.which == 13) {
            this.editAudienceEnabled = false;
            this._router.navigate(['concepts', { target: this.selectedInputs }]);
        }
    };
    //  public loadCycConcepts() {
    //     let selectedInputs:any = {'client':this.client,'audience':this.audienceInput.name.replace(/,\s*$/, ""),'product':this.productInput.productName,'campaign':this.planInput.planName}
    //     this._router.navigate(['concepts',{target:selectedInputs}]);
    //   }
    MriquestionComponent.prototype.showRationale = function (question) {
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
    MriquestionComponent.prototype.enableEdit = function () {
        var ref = this;
        $("#alertDialog").dialog({
            resizable: false,
            minHeight: 140,
            minWidth: 450,
            title: "ALERT",
            modal: true,
            show: {
                effect: "blind",
                duration: 1000
            },
            hide: {
                effect: "explode",
                duration: 1000
            },
            buttons: {
                "Yes": function () {
                    ref.editAudienceEnabled = true;
                    $(this).dialog("close");
                },
                "No": function () {
                    $(this).dialog("close");
                }
            }
        });
    };
    MriquestionComponent = __decorate([
        core_1.Component({
            moduleId: module.id,
            selector: 'app-mriquestion',
            templateUrl: 'mriquestion.component.html',
            styleUrls: ['mriquestion.component.css'],
            providers: [cycquestion_service_1.CycquestionService, justification_service_1.JustificationService]
        }), 
        __metadata('design:paramtypes', [justification_service_1.JustificationService, router_1.Router, router_1.RouteSegment, cycquestion_service_1.CycquestionService])
    ], MriquestionComponent);
    return MriquestionComponent;
}());
exports.MriquestionComponent = MriquestionComponent;
//# sourceMappingURL=C:/UI_STASH_REPO/poc-development/tmp/broccoli_type_script_compiler-input_base_path-TRL9n9Hx.tmp/0/app/+mriquestion/mriquestion.component.js.map