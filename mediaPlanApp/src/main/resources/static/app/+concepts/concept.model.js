"use strict";
var Concept = (function () {
    function Concept(text, index, sentences, session, isNew, selected) {
        this.name = text;
        this.sentences = sentences;
        this.index = index;
        this.sessionId = session;
        this.isNew = isNew;
        this.selected = selected;
    }
    return Concept;
}());
exports.Concept = Concept;
//# sourceMappingURL=C:/UI_STASH_REPO/poc-development/tmp/broccoli_type_script_compiler-input_base_path-TRL9n9Hx.tmp/0/app/+concepts/concept.model.js.map